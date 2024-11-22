FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/demo-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} /api_rest.jar
EXPOSE 8080

ENTRYPOINT ["java","-jar","/api_rest.jar"]


