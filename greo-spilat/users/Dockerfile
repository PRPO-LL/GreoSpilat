FROM openjdk:17-jdk

RUN mkdir /app

WORKDIR /app

COPY ./users-api/target/users-api-1.0.0-SNAPSHOT.jar /app

EXPOSE  8084

ENTRYPOINT ["java", "-jar", "users-api-1.0.0-SNAPSHOT.jar"]

CMD ["java", "-jar", "users-api-1.0.0-SNAPSHOT.jar"]