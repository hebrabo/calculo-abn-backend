# Etapa 1: Compilació
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: Execució
FROM openjdk:17-jdk-slim
WORKDIR /app
# Copiem només el fitxer .jar principal (evitem el -plain.jar)
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]