#!/bin/sh
set -eu

APP_PORT="${PORT:-8080}"
case "$APP_PORT" in
    ''|*[!0-9]*)
        echo "PORT must be a number" >&2
        exit 1
        ;;
esac

if [ "$APP_PORT" -lt 1024 ] || [ "$APP_PORT" -gt 65535 ]; then
    echo "PORT must be between 1024 and 65535" >&2
    exit 1
fi

sed -i "s/port=\"8080\"/port=\"${APP_PORT}\"/" /usr/local/tomcat/conf/server.xml
exec catalina.sh run
