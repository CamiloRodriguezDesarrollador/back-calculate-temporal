FROM maven:3.8.4-openjdk-17-slim AS build

WORKDIR /app

COPY . .

RUN mvn package -DskipTests

FROM eclipse-temurin:17-jdk-alpine

VOLUME /tmp

COPY --from=build /app/target/*.jar client.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "client.jar"]