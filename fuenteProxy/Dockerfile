FROM eclipse-temurin:17
LABEL maintainer="pepe"
WORKDIR /app
COPY target/fuenteProxy-0.0.1-SNAPSHOT.jar /app/fuenteProxy.jar

ENTRYPOINT ["java", "-jar", "fuenteProxy.jar"]
