FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} client-microservices.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/client-microservices.jar"]