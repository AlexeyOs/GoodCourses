FROM postgres:11

ENV POSTGRES_USER goodcourses
ENV POSTGRES_PASSWORD 1234
ENV POSTGRES_DB goodcourses
ENV POSTGRES_DBNAME: goodcourses
#ENV POSTGRES_HOST=localhost:5432

#EXPOSE 5432
#VOLUME create pg_data

#RUN psql -h postgresql://192.168.99.100 -p 5432 -U goodcourses -d goodcourses -a -f goodcourses-db.sql
#RUN psql -U goodcourses -d goodcourses -c goodcourses-db.sql


ADD init-user-db.sh /docker-entrypoint-initdb.d
#ADD goodcourses-db.sql /docker-entrypoint-initdb.d

