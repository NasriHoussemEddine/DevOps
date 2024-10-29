FROM openjdk:11-jdk-slim
WORKDIR /app
ARG JAR_FILE
COPY ${JAR_FILE} /app/app.jar
EXPOSE 8089
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
