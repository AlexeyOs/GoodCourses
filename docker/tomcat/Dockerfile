FROM tomcat:9

EXPOSE 8080

RUN rm -fr /usr/local/tomcat/webapps/ROOT

COPY ./target/GoodCourses.war /usr/local/tomcat/webapps/ROOT.war
COPY ./src/main/webapp/WEB-INF/lib/ /usr/local/tomcat/lib/

CMD ["catalina.sh", "run"]