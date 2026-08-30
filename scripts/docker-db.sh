#!/usr/bin/env bash
# Sobe PostgreSQL via Docker Compose para testes locais.
# Uso:
#   ./scripts/docker-db.sh
#   ./scripts/docker-db.sh --reset
#   ./scripts/docker-db.sh --with-backend

set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT"

RESET=false
WITH_BACKEND=false

for arg in "$@"; do
  case "$arg" in
    --reset) RESET=true ;;
    --with-backend) WITH_BACKEND=true ;;
    -h|--help)
      echo "Uso: $0 [--reset] [--with-backend]"
      exit 0
      ;;
    *)
      echo "Opção desconhecida: $arg"
      exit 1
      ;;
  esac
done

if ! command -v docker >/dev/null 2>&1; then
  echo "Docker não encontrado."
  exit 1
fi

if [[ ! -f .env ]]; then
  cp .env.example .env
  echo "Criado .env a partir de .env.example"
fi

# shellcheck disable=SC1091
set -a
source .env
set +a

POSTGRES_DB="${POSTGRES_DB:-app}"
POSTGRES_USER="${POSTGRES_USER:-app}"
POSTGRES_PASSWORD="${POSTGRES_PASSWORD:-change_me}"
POSTGRES_PORT="${POSTGRES_PORT:-5432}"
BACKEND_PORT="${BACKEND_PORT:-8080}"

echo ""
echo "Corujas da Sabedoria — Docker DB"
echo "================================="

if [[ "$RESET" == true ]]; then
  echo "Removendo containers e volume postgres_data..."
  docker compose down -v
fi

if [[ "$WITH_BACKEND" == true ]]; then
  echo "Subindo PostgreSQL + backend (build)..."
  docker compose up -d --build
else
  echo "Subindo apenas PostgreSQL..."
  docker compose up -d postgres
fi

echo "Aguardando PostgreSQL..."
for _ in $(seq 1 30); do
  if docker compose exec -T postgres pg_isready -U "$POSTGRES_USER" -d "$POSTGRES_DB" >/dev/null 2>&1; then
    break
  fi
  sleep 2
done

echo ""
echo "PostgreSQL disponível!"
echo ""
echo "Conexão:"
echo "  jdbc:postgresql://localhost:${POSTGRES_PORT}/${POSTGRES_DB}"
echo "  user=$POSTGRES_USER password=$POSTGRES_PASSWORD"
echo ""

if [[ "$WITH_BACKEND" == true ]]; then
  echo "API: http://localhost:${BACKEND_PORT}"
  echo "Health: http://localhost:${BACKEND_PORT}/actuator/health"
else
  echo "Backend local: cd backend && mvn spring-boot:run"
  echo "Ou: ./scripts/docker-db.sh --with-backend"
fi

echo ""
echo "Parar: docker compose down"
echo "Recriar: ./scripts/docker-db.sh --reset"
