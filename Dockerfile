FROM openjdk:11-jdk-slim
RUN apt-get update && apt-get install -y maven
WORKDIR /app
COPY target/*.jar /app/4CARCTIC-G1-tpAchatProject1-0.0.jar
EXPOSE 8089
ENTRYPOINT ["java", "-jar", "/app/4CARCTIC-G1-tpAchatProject1-0.0.jar"]
