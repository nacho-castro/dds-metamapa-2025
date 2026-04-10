FROM eclipse-temurin:17
LABEL maintainer="pepe"
WORKDIR /app
COPY target/servicioAutenticacion-0.0.1-SNAPSHOT.jar /app/servicioAutenticacion.jar

ENTRYPOINT ["java", "-jar", "servicioAutenticacion.jar"]
