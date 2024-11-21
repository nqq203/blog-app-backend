# Stage 1: Build the application
FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /app

# Copy the pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:resolve

# Copy the source code and build the application
COPY src ./src
RUN mvn clean package -DskipTests

# Check to list the JAR file (for debugging purposes)
RUN ls /app/target

# Start a new stage for the final image
FROM eclipse-temurin:21
WORKDIR /app

# Copy the JAR file from the build stage to the final image
COPY --from=build /app/target/*.jar app.jar

# Expose port 8761 to match the docker-compose settings
EXPOSE 8080

# Set the command to execute the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]