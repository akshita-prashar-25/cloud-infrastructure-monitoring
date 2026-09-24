# ---------- Build stage ----------
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Copy Maven wrapper and project configuration
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Make Maven wrapper executable
RUN chmod +x mvnw

# Download dependencies first
RUN ./mvnw dependency:go-offline -DskipTests

# Copy source code
COPY src/ src/

# Build the Spring Boot application
RUN ./mvnw clean package -DskipTests


# ---------- Runtime stage ----------
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the generated Spring Boot JAR
COPY --from=build /app/target/*.jar app.jar

# Spring Boot runs on port 8080
EXPOSE 8080

# Start the application
ENTRYPOINT ["java", "-jar", "app.jar"]