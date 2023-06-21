FROM openjdk:17-jdk-slim
COPY target/flighter*.jar /app/service.jar
EXPOSE 8080

WORKDIR /app
CMD ["sh", "-c", "java -jar service.jar"]
