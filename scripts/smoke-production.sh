#!/usr/bin/env bash
set -euo pipefail

image=${1:?Provide the production image to test}
base="goodcourses-smoke-$$"
db="$base-db"
app="$base-app"
ca="$base-ca"
cleanup() {
    docker rm -f "$app" "$db" >/dev/null 2>&1 || true
    docker volume rm "$ca" >/dev/null 2>&1 || true
    docker network rm "$base" >/dev/null 2>&1 || true
}
trap cleanup EXIT
trap 'docker logs --tail 120 "$app" >&2 || true' ERR

docker network create --internal "$base" >/dev/null
docker volume create "$ca" >/dev/null
docker run -d --rm --name "$db" --network "$base" --network-alias gc-db \
    -v "$ca:/ca" -e POSTGRES_DB=goodcourses -e POSTGRES_PASSWORD=fixture-admin \
    --entrypoint sh postgres:16 -ec '
    umask 077
    openssl req -new -x509 -newkey rsa:2048 -nodes -days 1 \
        -subj /CN=gc-db -addext subjectAltName=DNS:gc-db \
        -keyout /tmp/server.key -out /tmp/server.crt >/dev/null 2>&1
    chown postgres:postgres /tmp/server.key
    chmod 644 /tmp/server.crt
    cp /tmp/server.crt /ca/ca.crt
    chmod 644 /ca/ca.crt
    exec docker-entrypoint.sh postgres -c ssl=on \
        -c ssl_cert_file=/tmp/server.crt -c ssl_key_file=/tmp/server.key' >/dev/null
for attempt in {1..45}; do
    if docker exec "$db" pg_isready -h 127.0.0.1 -U postgres -d goodcourses >/dev/null; then
        break
    fi
    sleep 1
done
docker exec -i "$db" psql -q -v ON_ERROR_STOP=1 -U postgres -d goodcourses <<'SQL'
CREATE ROLE gc_migrator LOGIN PASSWORD 'fixture-migrator';
CREATE ROLE gc_runtime LOGIN PASSWORD 'fixture-runtime';
GRANT CONNECT ON DATABASE goodcourses TO gc_migrator, gc_runtime;
GRANT USAGE, CREATE ON SCHEMA public TO gc_migrator;
GRANT USAGE ON SCHEMA public TO gc_runtime;
ALTER DEFAULT PRIVILEGES FOR ROLE gc_migrator IN SCHEMA public
    GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO gc_runtime;
ALTER DEFAULT PRIVILEGES FOR ROLE gc_migrator IN SCHEMA public
    GRANT USAGE, SELECT ON SEQUENCES TO gc_runtime;
SQL

docker run -d --rm --name "$app" --network "$base" --cpus=0.1 --memory=512m \
    -v "$ca:/ca:ro" -e PORT=10000 -e APP_HOST=https://goodcourses.example.invalid \
    -e REMEMBER_ME_KEY=fixture-local-only-key \
    -e 'DATABASE_URL=postgresql://gc_runtime:fixture-runtime@gc-db/goodcourses?sslmode=verify-full&sslrootcert=/ca/ca.crt' \
    -e 'MIGRATION_DATABASE_URL=postgresql://gc_migrator:fixture-migrator@gc-db/goodcourses?sslmode=verify-full&sslrootcert=/ca/ca.crt' \
    "$image" >/dev/null

base_url=http://localhost:10000
request() { docker exec "$app" curl -sS --max-time 120 "$@"; }
expect_status() {
    local expected=$1 actual
    shift
    actual=$(request -o /dev/null -w '%{http_code}' "$@")
    printf 'HTTP %s (expected %s): %s\n' "$actual" "$expected" "${*: -1}"
    [[ $actual == "$expected" ]]
}
start=$SECONDS
for attempt in {1..120}; do
    code=$(docker exec "$app" curl -s -o /dev/null -w '%{http_code}' \
        --max-time 3 "$base_url/health/live" || true)
    [[ $code != 404 ]] || exit 1
    [[ $code != 200 ]] || break
    sleep 3
done
printf 'Cold start: %ss\n' "$((SECONDS - start))"
for route in /health/live /health/ready /courses /sign-in /sign-up /static/js/jquery.js; do
    expect_status 200 "$base_url$route"
done
expect_status 302 "$base_url/my-reviews"
expect_status 403 -X POST "$base_url/sign-up"

csrf_token() {
    sed -nE 's/.*name="_csrf" value="([^"]+)".*/\1/p'
}
cookie=/tmp/goodcourses-sign-up.cookie
csrf=$(request -f -c "$cookie" "$base_url/sign-up" | csrf_token)
test -n "$csrf"
expect_status 302 -b "$cookie" -c "$cookie" --data-urlencode "_csrf=$csrf" \
    --data-urlencode first_name=Smoke --data-urlencode last_name=Test \
    --data-urlencode uid=smokefixture --data-urlencode mail=smoke@example.invalid \
    --data-urlencode password=FixtureSmoke2026 "$base_url/sign-up"
expect_status 200 -b "$cookie" "$base_url/my-reviews"

cookie=/tmp/goodcourses-sign-in.cookie
csrf=$(request -f -c "$cookie" "$base_url/sign-in" | csrf_token)
test -n "$csrf"
expect_status 302 -b "$cookie" -c "$cookie" --data-urlencode "_csrf=$csrf" \
    --data-urlencode uid=smokefixture --data-urlencode password=FixtureSmoke2026 \
    "$base_url/sign-in-handler"
expect_status 200 -b "$cookie" "$base_url/my-reviews"
docker stats --no-stream --format 'Memory: {{.MemUsage}}' "$app"
