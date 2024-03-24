# Use the Maven image with OpenJDK to build the project
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app
COPY . .
ARG ENV
RUN cp .env.${ENV} .env
RUN mvn clean package && \
    mv target/*.jar app.jar && \
    mv app.jar /tmp/ && \
    mv .env.${ENV} /tmp/.env

FROM openjdk:17-slim
WORKDIR /app
COPY --from=build /tmp/ ./
EXPOSE 5000
CMD ["java", "-jar", "app.jar"]
