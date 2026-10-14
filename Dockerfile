# ==============================================================================
# Multi-Stage Dockerfile for Jenkins-Based Research Paper Repository (US-015)
# Stage 1: Build and package using Maven and Eclipse Temurin JDK 21
# Stage 2: Minimal hardened Eclipse Temurin JRE 21 runtime with non-root user
# ==============================================================================

# --- Stage 1: Build Stage ---
FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder

WORKDIR /build

# Cache Maven dependencies by copying pom.xml first
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy project source code
COPY src ./src

# Compile and package production executable JAR (skipping test execution during image build)
RUN mvn clean package -DskipTests -B

# --- Stage 2: Runtime Stage ---
FROM eclipse-temurin:21-jre-alpine AS runtime

# Metadata labels
LABEL maintainer="Muawiyah Narvel <23108A0020>"
LABEL description="Spring Boot Research Paper Repository Container"
LABEL version="1.0.0"

WORKDIR /app

# Create unprivileged system group and user for security compliance
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Copy executable fat JAR from build stage
COPY --from=builder --chown=appuser:appgroup /build/target/*.jar app.jar

# Run as non-root user
USER appuser:appgroup

# Expose default HTTP application port
EXPOSE 8081

# Configure container health check
HEALTHCHECK --interval=30s --timeout=5s --start-period=20s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8081/ || exit 1

# Environment configurations
ENV SERVER_PORT=8081 \
    SPRING_PROFILES_ACTIVE=prod \
    JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC"

# Launch Spring Boot application
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar"]
