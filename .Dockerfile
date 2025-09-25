# Etapa 1: build con Maven y JDK 17
FROM maven:3.9.0-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN ./mvnw -Pprod -DskipTests clean verify

# Etapa 2: runtime con JRE ligero
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Exponer el puerto interno
EXPOSE 8080

# Railway asigna el puerto con $PORT
CMD ["sh", "-c", "java -Djava.security.egd=file:/dev/./urandom -jar app.jar --server.port=$PORT"]
