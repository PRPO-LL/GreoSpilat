FROM eclipse-temurin:17-jdk

RUN mkdir /app

WORKDIR /app

COPY ./users-api/target/users-api-1.0.0-SNAPSHOT.jar /app

EXPOSE  8084

CMD ["java", "-jar", "users-api-1.0.0-SNAPSHOT.jar"]