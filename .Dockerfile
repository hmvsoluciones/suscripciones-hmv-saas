# ============================
# Etapa 1: Build con Maven y JDK 17
# ============================
FROM maven:3.9.0-eclipse-temurin-17 AS build
WORKDIR /app

# Copiar lo mínimo para cachear dependencias
COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# Copiar código fuente y compilar en modo producción
COPY src src
RUN ./mvnw -Pprod -DskipTests clean package

# ============================
# Etapa 2: Runtime con JRE ligero
# ============================
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copiar el JAR generado en la etapa build
COPY --from=build /app/target/*.jar app.jar

# Railway asigna dinámicamente el puerto en $PORT
EXPOSE 8080

# Variables de entorno recomendadas para prod en Railway
ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Usar $PORT en tiempo de ejecución
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dserver.port=${PORT} -Djava.security.egd=file:/dev/./urandom -jar app.jar"]
