# Etapa 1: build con Maven y JDK 17
FROM maven:3.9.0-eclipse-temurin-17 AS build
WORKDIR /app

# Copiar solo lo necesario para cachear dependencias
COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# Copiar el código fuente y compilar en modo prod
COPY src src
RUN ./mvnw -Pprod -DskipTests clean package

# Etapa 2: runtime con JRE ligero
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copiar solo el jar generado
COPY --from=build /app/target/*.jar app.jar

# Exponer el puerto interno
EXPOSE 8080

# Railway asigna el puerto con $PORT
CMD ["sh", "-c", "java -Djava.security.egd=file:/dev/./urandom -jar app.jar --server.port=$PORT"]
