# Use official base image of Java Runtim
FROM openjdk:8-jre-alpine

# Set volume point to /tmp
VOLUME /tmp

# Make port 6565 available to the world outside container
EXPOSE 8080

# Set application's JAR file
ARG JAR_FILE=build/libs/b1ing-service.jar

# Add the application's JAR file to the container
ADD ${JAR_FILE} b1ing-service.jar

# Run the JAR file
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/b1ing-service.jar"]
