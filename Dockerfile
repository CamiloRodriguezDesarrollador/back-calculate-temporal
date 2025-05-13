FROM maven:3.9.6-eclipse-temurin-22 AS build

WORKDIR /app
COPY . .
RUN mvn package -DskipTests

FROM eclipse-temurin:22-jdk-alpine

VOLUME /tmp
COPY --from=build /app/target/*.jar client.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "client.jar"]
