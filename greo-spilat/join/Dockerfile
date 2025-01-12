FROM eclipse-temurin:17-jdk


RUN mkdir /app

WORKDIR /app

COPY ./join-api/target/join-api-1.0.0-SNAPSHOT.jar /app

EXPOSE  8403

ENTRYPOINT ["java", "-jar", "join-api-1.0.0-SNAPSHOT.jar"]

CMD ["java", "-jar", "join-api-1.0.0-SNAPSHOT.jar"]
