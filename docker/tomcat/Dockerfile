FROM maven:3.9.13-eclipse-temurin-8 AS build

WORKDIR /workspace
COPY pom.xml ./
RUN mvn -B -Pproduction dependency:go-offline

COPY src ./src
RUN mvn -B -Pproduction clean package -DskipTests

FROM tomcat:9.0.123-jre8-temurin-jammy

RUN apt-get update \
    && apt-get install --yes --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/* \
    && rm -rf /usr/local/tomcat/webapps/* \
    && groupadd --system tomcat-app \
    && useradd --system --gid tomcat-app --home-dir /usr/local/tomcat --shell /usr/sbin/nologin tomcat-app \
    && chown -R tomcat-app:tomcat-app /usr/local/tomcat

COPY --from=build --chown=tomcat-app:tomcat-app /workspace/target/GoodCourses.war /usr/local/tomcat/webapps/ROOT.war
COPY --chown=tomcat-app:tomcat-app docker/tomcat/entrypoint.sh /usr/local/bin/goodcourses-entrypoint
RUN chmod 0755 /usr/local/bin/goodcourses-entrypoint

USER tomcat-app
EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=5s --start-period=45s --retries=3 \
    CMD curl --fail --silent --show-error "http://127.0.0.1:${PORT:-8080}/health/live" || exit 1

ENTRYPOINT ["/usr/local/bin/goodcourses-entrypoint"]
