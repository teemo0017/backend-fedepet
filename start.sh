#!/bin/bash
set -e

if [ ! -f .env ]; then
  echo "Error: no se encontró el archivo .env en la raíz del proyecto"
  exit 1
fi

set -a
source .env
set +a

echo "Variables de entorno cargadas. Iniciando backend en puerto ${SERVER_PORT:-4000}..."
./mvnw spring-boot:run
