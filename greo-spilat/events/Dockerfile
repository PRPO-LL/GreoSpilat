FROM  openjdk:17-jdk

RUN mkdir /app

WORKDIR /app

COPY ./events-api/target/events-api-1.0.0-SNAPSHOT.jar /app

EXPOSE  8083

ENTRYPOINT ["java", "-jar", "events-api-1.0.0-SNAPSHOT.jar"]


CMD ["java", "-jar", "events-api-1.0.0-SNAPSHOT.jar"]