# Fundação Mobile + Backend

Base técnica para um aplicativo React Native com API Spring Boot e PostgreSQL. A etapa atual contém somente a infraestrutura mínima para iniciar o sistema e verificar a comunicação entre mobile e backend.

## Pré-requisitos

- Java 17
- Maven 3.9+
- Node.js 20+
- pnpm
- Expo Go (para testar no dispositivo) ou um emulador Android/iOS
- Docker e Docker Compose

## Executar PostgreSQL e backend

Na raiz:

```bash
cp .env.example .env
docker compose up --build
```

O backend ficará disponível em `http://localhost:8080`.

Endpoints de verificação:

- `GET /actuator/health`
- `GET /api/v1/system/status`

Resposta do endpoint de integração:

```json
{
  "status": "UP"
}
```

Para executar o backend fora do Docker, mantenha um PostgreSQL acessível e rode:

```bash
cd backend
mvn spring-boot:run
```

## Executar o mobile

Instale as dependências do workspace e inicie o app pelo workflow Expo configurado no Replit:

```bash
pnpm install
pnpm --filter @workspace/mobile run dev
```

Defina `EXPO_PUBLIC_API_BASE_URL` com uma URL que o emulador ou dispositivo consiga alcançar. Exemplos:

- web/emulador no mesmo ambiente: `http://localhost:8080`
- emulador Android apontando para o host local: `http://10.0.2.2:8080`
- dispositivo físico: IP da máquina na rede local, como `http://192.168.x.x:8080`

O mobile possui apenas a tela descartável de inicialização e consulta `GET /api/v1/system/status`, exibindo `API Online` ou `API Offline`.

## Variáveis de ambiente

| Variável | Uso | Padrão |
| --- | --- | --- |
| `POSTGRES_DB` | Banco criado pelo Compose | `app` |
| `POSTGRES_USER` | Usuário do PostgreSQL | `app` |
| `POSTGRES_PASSWORD` | Senha local do PostgreSQL | `change_me` |
| `POSTGRES_PORT` | Porta publicada do PostgreSQL | `5432` |
| `DB_HOST` | Host do banco para o backend | `localhost` |
| `DB_PORT` | Porta do banco para o backend | `5432` |
| `DB_NAME` | Nome do banco para o backend | `app` |
| `DB_USERNAME` | Usuário do banco para o backend | `app` |
| `DB_PASSWORD` | Senha do banco para o backend | `change_me` |
| `BACKEND_PORT` | Porta publicada da API | `8080` |
| `PORT` | Porta interna do Spring Boot | `8080` |
| `CORS_ALLOWED_ORIGINS` | Origens permitidas, separadas por vírgula | localhost para desenvolvimento |
| `EXPO_PUBLIC_API_BASE_URL` | URL base usada pelo app mobile | `http://localhost:8080` |

Não commite `.env` nem credenciais reais.

## Estrutura

```text
backend/
├── src/main/java/com/example/foundation/
│   ├── config/
│   ├── modules/system/
│   │   ├── controller/
│   │   ├── dto/
│   │   └── service/
│   └── shared/exception/
└── src/main/resources/
    ├── application.yml
    └── db/migration/

artifacts/mobile/
├── app/
├── components/
├── constants/
├── hooks/
└── services/
```

O backend é um monólito modularizado por feature. Controllers recebem requisições e devolvem DTOs; services concentram orquestração; futuras entidades e repositories ficarão dentro do módulo correspondente. O Flyway controla alterações de schema e o Hibernate apenas valida o schema existente.

## Validação local

```bash
cd backend
mvn test
cd ..
pnpm run typecheck
```

O teste inicial valida o controller do endpoint de status. A validação completa de banco ocorre com `docker compose up --build`, que aguarda o PostgreSQL saudável antes de iniciar o Spring Boot.