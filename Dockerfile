FROM maven:3.8.5-openjdk-11-slim AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests -Dproject.build.sourceEncoding=UTF-8 -Dmaven.resources.encoding=UTF-8

FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ARG APP_PORT=9100
ENV SERVER_PORT=${APP_PORT}
EXPOSE ${APP_PORT}
CMD java ${JAVA_OPTS} -Dspring.profiles.active=docker -Dserver.port=${SERVER_PORT} -jar app.jar