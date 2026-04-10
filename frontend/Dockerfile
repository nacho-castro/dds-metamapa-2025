FROM eclipse-temurin:17
LABEL maintainer="pepe"
WORKDIR /app
COPY target/frontend-0.0.1-SNAPSHOT.jar /app/frontend.jar

ENTRYPOINT ["java", "-jar", "frontend.jar"]
