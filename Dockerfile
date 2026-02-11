# Etapa 1: Compilació
FROM maven:3.8.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
# Fem la compilació, eliminem el fitxer 'plain' i renomenem el correcte a 'app.jar'
# Així el següent pas de COPY serà net i sense errors.
RUN mvn clean package -DskipTests && \
    rm -f target/*-plain.jar && \
    cp target/*.jar target/app.jar

# Etapa 2: Execució
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
# Ara copiem un fitxer amb un nom fix, sense caràcters que confonguin a Render
COPY --from=build /app/target/app.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]