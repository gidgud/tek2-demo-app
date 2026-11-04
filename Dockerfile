# ---------- Stage 1: byg jar-filen ----------
# Stort image med Maven + JDK. Bruges kun til at bygge.
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Build-cache-optimering: kopiér pom.xml FØRST og hent dependencies.
# Dette lag genbruges så længe pom.xml ikke ændrer sig - også når src/ ændres.
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Kildekoden ændrer sig ofte -> kopieres sidst
COPY src ./src
RUN mvn -B package -DskipTests

# ---------- Stage 2: kør jar-filen ----------
# Lille image med kun JRE - ingen Maven, ingen kildekode.
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/app.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
