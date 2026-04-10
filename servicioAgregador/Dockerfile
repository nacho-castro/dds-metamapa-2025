FROM eclipse-temurin:17
LABEL maintainer="pepe"
WORKDIR /app
COPY target/servicioAgregador-0.0.1-SNAPSHOT.jar /app/servicioAgregador.jar

ENTRYPOINT ["java", "-jar", "servicioAgregador.jar"]
