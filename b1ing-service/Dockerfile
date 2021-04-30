# Use official base image of Java Runtime
FROM openjdk:8-jre-alpine

WORKDIR /app

EXPOSE 8080

# Set application's JAR file
ARG JAR_FILE=/build/libs/b1ing-service.jar

# Add the application's JAR file to the container
COPY ${JAR_FILE} /app

# Run the JAR file
ENTRYPOINT ["java", "-Xmx1536m", "-Djava.security.egd=file:/dev/./urandom", "-jar", "b1ing-service.jar"]
