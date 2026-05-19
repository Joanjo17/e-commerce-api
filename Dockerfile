# BUILD java 25
FROM maven:3.9.12-eclipse-temurin-25-alpine AS build
WORKDIR /app

# Copia solo el pom.xml para optimizar la caché de dependencias.
COPY pom.xml .

#Descarga todas las dependencias
RUN mvn dependency:go-offline -B


COPY src ./src

# Compila el proyecto y genera el JAR, saltándose los tests.
RUN mvn clean package -DskipTests -B


#  RUNTIME
FROM eclipse-temurin:25-jre-alpine

WORKDIR /app

EXPOSE 8080

COPY --from=build /app/target/*.jar app.jar

ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=80"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]