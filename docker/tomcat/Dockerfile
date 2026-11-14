# syntax=docker/dockerfile:1
FROM maven:3.9.13-eclipse-temurin-21@sha256:850d3575de1cf32ee99e6da31acd943242113ee9b7d320564784a30b2f9b0a42 AS build

WORKDIR /workspace
ARG BUILD_PROFILE=production
COPY pom.xml ./
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn -B -ntp "-P${BUILD_PROFILE}" clean package -DskipTests \
    && grep -Fxq "configProfile=${BUILD_PROFILE}" target/classes/application.properties

FROM tomcat:11.0-jre21-temurin-noble@sha256:29383617e4ad5fb3724b9c198b1622cb5034d29d9d4d65e00ec817be59d07529

RUN apt-get update \
    && apt-get install --yes --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/* \
    && rm -rf /usr/local/tomcat/webapps/* \
    && groupadd --system tomcat-app \
    && useradd --system --gid tomcat-app --home-dir /usr/local/tomcat --shell /usr/sbin/nologin tomcat-app \
    && chown -R tomcat-app:tomcat-app /usr/local/tomcat

COPY --from=build --chown=tomcat-app:tomcat-app /workspace/target/GoodCourses.war /usr/local/tomcat/webapps/ROOT.war
COPY --chown=tomcat-app:tomcat-app docker/tomcat/entrypoint.sh /usr/local/bin/goodcourses-entrypoint
RUN sed -i 's/\r$//' /usr/local/bin/goodcourses-entrypoint \
    && chmod 0755 /usr/local/bin/goodcourses-entrypoint

USER tomcat-app
EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=5s --start-period=45s --retries=3 \
    CMD curl --fail --silent --show-error "http://127.0.0.1:${PORT:-8080}/health/live" || exit 1

ENTRYPOINT ["/usr/local/bin/goodcourses-entrypoint"]
