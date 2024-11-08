# Stage 1: Build the application
FROM maven:3.8.4-eclipse-temurin-17-alpine AS builder

# Set the working directory
WORKDIR /app

# Copy the pom.xml and the source code
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean install -Pprod -DskipTests

# Stage 2: Run the application
FROM openjdk:17-jdk-alpine

# Set the working directory
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=builder /app/target/*.jar devops.jar
# Expose the application port
EXPOSE 8989

# Run the JAR file
ENTRYPOINT ["java", "-jar","-Djava.net.preferIPv4Stack=true", "-Dspring.profiles.active=prod", "devops.jar"]