# ============================
# Etapa 1: Build con Maven y JDK 23 (para dev)
# ============================
FROM maven:3.9.9-eclipse-temurin-23 AS build
WORKDIR /app

# Copiar lo mínimo para cachear dependencias
COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# Copiar código fuente y compilar en modo producción
COPY src src
# Usamos -DskipTests para no correr tests en build
RUN ./mvnw clean package -DskipTests

# ============================
# Etapa 2: Runtime con JRE 23 ligero
# ============================
FROM eclipse-temurin:23-jre-jammy
WORKDIR /app

# Copiar JAR generado en la etapa build
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Variables de entorno para prod
ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Usar $PORT en tiempo de ejecución
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dserver.port=${PORT} -Djava.security.egd=file:/dev/./urandom -jar app.jar"]
