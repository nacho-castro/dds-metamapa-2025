#!/bin/bash

BASE_DIR="2025-tpa-ma-ma-grupo-05"
DIRS=("estadisticas" "frontend" "fuenteDinamica" "fuenteEstatica" "fuenteProxy" "servicioAgregador" "servicioAutenticacion" "servicioGateway")

DOCKERFILE_TEMPLATE=$'FROM eclipse-temurin:17\nLABEL maintainer="pepe"\nWORKDIR /app\nCOPY target/DIRACT-0.0.1-SNAPSHOT.jar /app/DIRACT.jar\n\nENTRYPOINT ["java", "-jar", "DIRACT.jar"]'
ENV_CONTENT=$'DISENO_DB_USERNAME="root"\nDISENO_DB_PASSWORD="pepe"'
DOCKERIGNORE_CONTENT=$'.env'

for dir in "${DIRS[@]}"; do
    TARGET="$BASE_DIR/$dir"

    mkdir -p "$TARGET"

    DOCKERFILE_CONTENT="${DOCKERFILE_TEMPLATE//DIRACT/$dir}"

    echo "$ENV_CONTENT" > "$TARGET/.env"
    echo "$DOCKERIGNORE_CONTENT" > "$TARGET/.dockerignore"
    echo "$DOCKERFILE_CONTENT" > "$TARGET/Dockerfile"

    echo "Archivos creados en $TARGET"
done


