FROM eclipse-temurin:17-jdk


RUN mkdir /app

WORKDIR /app

COPY ./auth-api/target/auth-api-1.0.0-SNAPSHOT.jar /app

EXPOSE  8085

CMD ["java", "-jar", "auth-api-1.0.0-SNAPSHOT.jar"]