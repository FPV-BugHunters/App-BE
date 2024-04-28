FROM eclipse-temurin:17-jdk-alpine

VOLUME /opt/app

WORKDIR /opt/app
ARG JAR_FILE

COPY target/trading-app-0.0.1-SNAPSHOT.jar app.jar
COPY ./static ./static


ENTRYPOINT ["java", "-jar","/opt/app/app.jar", "--spring.config.location=file:///config/application.properties"]