FROM openjdk:11-jdk-slim

ADD ./target/trending-service.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/trending-service.jar"]

EXPOSE 8065