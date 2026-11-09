#!/bin/sh
set -eu

APP_PORT="${PORT:-8080}"
TOMCAT_MAX_THREADS_VALUE="${TOMCAT_MAX_THREADS:-20}"
TOMCAT_ACCEPT_COUNT_VALUE="${TOMCAT_ACCEPT_COUNT:-20}"

validate_positive_integer() {
    value="$1"
    name="$2"
    case "$value" in
        ''|*[!0-9]*)
            echo "$name must be a positive integer" >&2
            exit 1
            ;;
    esac
    if [ "$value" -le 0 ]; then
        echo "$name must be a positive integer" >&2
        exit 1
    fi
}

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

validate_positive_integer "$TOMCAT_MAX_THREADS_VALUE" "TOMCAT_MAX_THREADS"
validate_positive_integer "$TOMCAT_ACCEPT_COUNT_VALUE" "TOMCAT_ACCEPT_COUNT"

if [ -z "${JAVA_TOOL_OPTIONS+x}" ]; then
    export JAVA_TOOL_OPTIONS="-Xms64m -Xmx256m -XX:MaxMetaspaceSize=96m -Xss512k -XX:+UseSerialGC"
fi

SERVER_XML="/usr/local/tomcat/conf/server.xml"
sed -i "s/port=\"8080\"/port=\"${APP_PORT}\"/" "$SERVER_XML"
sed -i "0,/<Connector port=\"${APP_PORT}\"/s//<Connector port=\"${APP_PORT}\" maxThreads=\"${TOMCAT_MAX_THREADS_VALUE}\" acceptCount=\"${TOMCAT_ACCEPT_COUNT_VALUE}\"/" "$SERVER_XML"
exec catalina.sh run
