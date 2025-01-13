FROM openjdk:17-jdk


RUN mkdir /app

WORKDIR /app

COPY ./auth-api/target/auth-api-1.0.0-SNAPSHOT.jar /app

EXPOSE  8085

ENTRYPOINT ["java", "-jar", "auth-api-1.0.0-SNAPSHOT.jar"]


CMD ["java", "-jar", "auth-api-1.0.0-SNAPSHOT.jar"]


