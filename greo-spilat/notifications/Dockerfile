FROM eclipse-temurin:17-jdk


RUN mkdir /app

WORKDIR /app

COPY ./notifications-api/target/notifications-api-1.0.0-SNAPSHOT.jar /app

EXPOSE  8401

CMD ["java", "-jar", "notifications-api-1.0.0-SNAPSHOT.jar"]
