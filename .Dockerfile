# Etapa de compilación
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .

# Verificar estructura del proyecto y archivos de configuración
RUN echo "=== Verificando estructura del proyecto ===" && \
    ls -la && \
    echo "=== Verificando recursos ===" && \
    ls -la src/main/resources/ && \
    echo "=== Verificando archivos de configuración ===" && \
    find src/main/resources -name "application*.yml" -o -name "application*.properties" | sort

# Compilar con perfil prod y verificación
RUN chmod +x mvnw && \
    ./mvnw clean package -Pprod -DskipTests && \
    echo "=== Verificando JAR generado ===" && \
    ls -la target/ && \
    jar tf target/*.jar | grep application-prod && \
    echo "=== JAR verificado correctamente ==="

# Etapa de ejecución
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Crear usuario no-root para seguridad
RUN groupadd -r spring && useradd -r -g spring spring && \
    chown -R spring:spring /app
USER spring

COPY --from=build --chown=spring:spring /app/target/*.jar app.jar

EXPOSE 8080

# Variables de entorno críticas
ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS="-Xmx512m -Xms256m -Dspring.profiles.active=prod"
ENV SERVER_PORT=8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:8080/management/health || exit 1

# Script de entrada optimizado para Railway
CMD ["sh", "-c", " \
    echo '=== Iniciando JHipster en Railway ==='; \
    echo 'Perfil activo: $SPRING_PROFILES_ACTIVE'; \
    echo 'Java OPTS: $JAVA_OPTS'; \
    echo 'Puerto: $PORT'; \
    echo 'Variables DB disponibles:'; \
    echo 'PGHOST: $PGHOST'; \
    echo 'PGPORT: $PGPORT'; \
    echo 'PGUSER: $PGUSER'; \
    echo 'PGDATABASE: $PGDATABASE'; \
    \
    if [ -n \"$DATABASE_URL\" ]; then \
        echo 'DATABASE_URL detectada, procesando...'; \
        DB_URL=$(echo $DATABASE_URL | sed 's/postgres:/postgresql:/g'); \
        echo 'URL procesada: $DB_URL'; \
        java $JAVA_OPTS -Dserver.port=${PORT:-8080} -Dspring.datasource.url=$DB_URL -Dspring.datasource.username=${PGUSER} -Dspring.datasource.password=${PGPASSWORD} -Dspring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect -jar app.jar; \
    else \
        echo 'Usando configuración por defecto del application-prod.yml'; \
        java $JAVA_OPTS -Dserver.port=${PORT:-8080} -jar app.jar; \
    fi \
"]