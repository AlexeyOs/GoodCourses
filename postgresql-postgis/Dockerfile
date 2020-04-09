FROM postgres:11

ENV POSTGRES_USER goodcourses
ENV POSTGRES_PASSWORD 1234
ENV POSTGRES_DB goodcourses
ENV POSTGRES_DBNAME: goodcourses


ADD init-user-db.sh /docker-entrypoint-initdb.d
#ADD goodcourses-db.sql /docker-entrypoint-initdb.d

