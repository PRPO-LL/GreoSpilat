FROM eclipse-temurin:17-jdk


RUN mkdir /app

WORKDIR /app

COPY ./comments-api/target/comments-api-1.0.0-SNAPSHOT.jar /app

EXPOSE  8402

ENTRYPOINT ["java", "-jar", "comments-api-1.0.0-SNAPSHOT.jar"]

CMD ["java", "-jar", "comments-api-1.0.0-SNAPSHOT.jar"]
