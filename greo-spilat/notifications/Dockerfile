FROM  openjdk:17-jdk


RUN mkdir /app

WORKDIR /app

COPY ./notifications-api/target/notifications-api-1.0.0-SNAPSHOT.jar /app

EXPOSE  8401

ENTRYPOINT ["java", "-jar", "notifications-api-1.0.0-SNAPSHOT.jar"]

CMD ["java", "-jar", "notifications-api-1.0.0-SNAPSHOT.jar"]
