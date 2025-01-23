FROM amazoncorretto:17
ENV DB_HOST gymsystem-db
ENV MQ_HOST activemq
ENV MYSQL_ROOT_PASSWORD rootpassword
ENV MYSQL_USER dev
ENV MYSQL_PASSWORD DevPassword
WORKDIR /app
COPY target/gym-system-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
