FROM openjdk:17-alpine

WORKDIR /app

COPY ./target/groceries-0.0.1-SNAPSHOT.jar /app/app.jar

ENTRYPOINT java -jar app.jar
