FROM openjdk:21-jdk-slim

WORKDIR /app
# Copy academic service .jar
COPY build/libs/dss-gateway-1.0.0.jar  dss-gateway.jar

# Copy wait-for-it.sh script
# COPY ./wait-for-it.sh  app/wait-for-it.sh

# Make wait-for-it executable
#RUN chmod  +x  app/wait-for-it.sh

# Command to execute the JAR, but it will be overridden by entrypoint in docker-compose.yml
ENTRYPOINT ["java", "-jar", "dss-gateway.jar"]
