# Etapa de compilación
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN chmod +x mvnw && ./mvnw clean package -Pprod -DskipTests

# Variables de entorno para runtime
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Etapa de ejecución
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Variables DB (Railway) - se recomienda definirlas en Railway directamente
ENV PGHOST=${PGHOST}
ENV PGPORT=${PGPORT}
ENV PGUSER=${PGUSER}
ENV PGPASSWORD=${PGPASSWORD}

CMD ["sh", "-c", "java $JAVA_OPTS -Dspring.profiles.active=prod -Dserver.port=${PORT} -jar app.jar"]
