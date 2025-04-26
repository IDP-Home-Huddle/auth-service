FROM maven:3.9.0-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

ENV TASK_MANAGEMENT_SERVICE_URL="http://task-management-service:8192"

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY --from=build /app/target/springbackend-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8191

ENTRYPOINT ["java", "-jar", "app.jar"]