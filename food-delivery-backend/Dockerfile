# Stage 1: Build stage with Maven and JDK 17
FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /app

# Copy Maven wrapper and POM file first for efficient caching
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Grant execute permission for Maven wrapper and download dependencies
RUN chmod +x ./mvnw && ./mvnw dependency:go-offline -B || true

# Copy source files and package application
COPY src ./src
RUN ./mvnw clean package -DskipTests -B

# Stage 2: Runtime runner with JRE 17
FROM eclipse-temurin:17-jre-alpine AS runner

WORKDIR /app

# Create a non-root group and user for security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Copy compiled JAR from builder stage
COPY --from=builder /app/target/*.jar app.jar

RUN chown -R appuser:appgroup /app
USER appuser

# Expose internal application port
EXPOSE 8080

# Run Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
