FROM eclipse-temurin:17
LABEL maintainer="pepe"
WORKDIR /app
COPY target/estadisticas-0.0.1-SNAPSHOT.jar /app/estadisticas.jar

ENTRYPOINT ["java", "-jar", "estadisticas.jar"]
