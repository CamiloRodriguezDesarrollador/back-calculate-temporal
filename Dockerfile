# Etapa de construcción
FROM gradle:8.2.1-jdk17 AS build

WORKDIR /app

# Copia los archivos de Gradle y el archivo de configuración
COPY gradle gradle
COPY settings.gradle.kts .
COPY build.gradle.kts .

# Copia el código fuente
COPY src src

# Ejecuta el build usando Gradle
RUN gradle build --no-daemon

# Etapa de ejecución
FROM eclipse-temurin:17-jdk-alpine

VOLUME /tmp

# Copia el JAR construido desde la etapa de construcción
COPY --from=build /app/build/libs/*.jar client.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "client.jar"]
