FROM eclipse-temurin:17-jdk-alpine

VOLUME /tmp

ARG JAR_FILE

COPY target/trading-app-0.0.1-SNAPSHOT.jar app.jar
COPY target/classes/static /static


ENTRYPOINT ["java", "-jar","/app.jar", "--spring.config.location=file:///config/application.properties"]