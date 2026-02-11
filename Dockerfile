# Etapa 1: Compilació (fem servir Eclipse Temurin per a Java 21)
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
# Fem la compilació, eliminem el fitxer 'plain' i renomenem el correcte a 'app.jar'
RUN mvn clean package -DskipTests && \
    rm -f target/*-plain.jar && \
    cp target/*.jar target/app.jar

# Etapa 2: Execució
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
# Copiem el fitxer .jar generat a l'etapa anterior
COPY --from=build /app/target/app.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]