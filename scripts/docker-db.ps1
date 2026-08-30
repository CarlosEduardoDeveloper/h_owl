# Sobe PostgreSQL via Docker Compose para testes locais.
# Uso:
#   .\scripts\docker-db.ps1
#   .\scripts\docker-db.ps1 -Reset          # recria volume do zero
#   .\scripts\docker-db.ps1 -WithBackend   # sobe Postgres + API Spring Boot

param(
    [switch]$Reset,
    [switch]$WithBackend
)

$ErrorActionPreference = "Stop"

$Root = Split-Path -Parent (Split-Path -Parent $MyInvocation.MyCommand.Path)
Set-Location $Root

function Ensure-EnvFile {
    $envFile = Join-Path $Root ".env"
    $envExample = Join-Path $Root ".env.example"
    if (-not (Test-Path $envFile)) {
        if (-not (Test-Path $envExample)) {
            throw "Arquivo .env.example não encontrado em $Root"
        }
        Copy-Item $envExample $envFile
        Write-Host "Criado .env a partir de .env.example" -ForegroundColor Yellow
    }
}

function Load-DotEnv {
    param([string]$Path)
    $vars = @{}
    Get-Content $Path | ForEach-Object {
        $line = $_.Trim()
        if ($line -eq "" -or $line.StartsWith("#")) { return }
        $idx = $line.IndexOf("=")
        if ($idx -lt 1) { return }
        $key = $line.Substring(0, $idx).Trim()
        $val = $line.Substring($idx + 1).Trim()
        $vars[$key] = $val
    }
    return $vars
}

Write-Host ""
Write-Host "Corujas da Sabedoria — Docker DB" -ForegroundColor Cyan
Write-Host "=================================" -ForegroundColor Cyan

if (-not (Get-Command docker -ErrorAction SilentlyContinue)) {
    throw "Docker não encontrado. Instale Docker Desktop e tente novamente."
}

docker info *> $null
if ($LASTEXITCODE -ne 0) {
    throw @"
Docker Desktop não está rodando.
1. Abra o Docker Desktop e aguarde iniciar
2. Execute novamente: .\scripts\docker-db.ps1
"@
}

Ensure-EnvFile
$envVars = Load-DotEnv (Join-Path $Root ".env")

$dbName = if ($envVars["POSTGRES_DB"]) { $envVars["POSTGRES_DB"] } else { "app" }
$dbUser = if ($envVars["POSTGRES_USER"]) { $envVars["POSTGRES_USER"] } else { "app" }
$dbPass = if ($envVars["POSTGRES_PASSWORD"]) { $envVars["POSTGRES_PASSWORD"] } else { "change_me" }
$dbPort = if ($envVars["POSTGRES_PORT"]) { $envVars["POSTGRES_PORT"] } else { "5432" }
$backendPort = if ($envVars["BACKEND_PORT"]) { $envVars["BACKEND_PORT"] } else { "8080" }

if ($Reset) {
    Write-Host "Removendo containers e volume postgres_data..." -ForegroundColor Yellow
    docker compose down -v
}

if ($WithBackend) {
    Write-Host "Subindo PostgreSQL + backend (build)..." -ForegroundColor Green
    docker compose up -d --build
    $service = "backend"
} else {
    Write-Host "Subindo apenas PostgreSQL..." -ForegroundColor Green
    docker compose up -d postgres
    $service = "postgres"
}

Write-Host "Aguardando PostgreSQL..." -ForegroundColor Gray

$ready = $false
for ($i = 1; $i -le 30; $i++) {
    docker compose exec -T postgres pg_isready -U $dbUser -d $dbName 2>$null | Out-Null
    if ($LASTEXITCODE -eq 0) {
        $ready = $true
        break
    }
    Start-Sleep -Seconds 2
}

if ($WithBackend) {
    Write-Host "Backend subindo — Flyway roda na inicialização. Logs: docker compose logs -f backend" -ForegroundColor Gray
}

Write-Host ""
if (-not $ready) {
    throw "PostgreSQL não respondeu a tempo. Verifique: docker compose logs postgres"
}

Write-Host ""
Write-Host "PostgreSQL disponível!" -ForegroundColor Green
Write-Host ""
Write-Host "Conexão JDBC (backend local / IntelliJ):" -ForegroundColor Cyan
Write-Host "  DB_HOST=localhost"
Write-Host "  DB_PORT=$dbPort"
Write-Host "  DB_NAME=$dbName"
Write-Host "  DB_USERNAME=$dbUser"
Write-Host "  DB_PASSWORD=$dbPass"
Write-Host ""
Write-Host "URL: jdbc:postgresql://localhost:${dbPort}/${dbName}"
Write-Host ""

if ($WithBackend) {
    Write-Host "API:" -ForegroundColor Cyan
    Write-Host "  http://localhost:${backendPort}/actuator/health"
    Write-Host "  http://localhost:${backendPort}/api/v1/system/status"
    Write-Host ""
    Write-Host "Registrar usuário:" -ForegroundColor Cyan
    Write-Host @"
  curl -X POST http://localhost:${backendPort}/api/v1/auth/registrar ^
    -H "Content-Type: application/json" ^
    -d "{\"usuario\":\"teste\",\"senha\":\"123\"}"
"@
    Write-Host ""
    Write-Host "Bíblia (com auth):" -ForegroundColor Cyan
    Write-Host @"
  curl http://localhost:${backendPort}/api/v1/biblias?idioma=por ^
    -H "X-Usuario: teste" -H "X-Senha: 123"
"@
} else {
    Write-Host "Próximo passo — backend fora do Docker:" -ForegroundColor Cyan
    Write-Host "  cd backend"
    Write-Host "  mvn spring-boot:run"
    Write-Host ""
    Write-Host "Flyway aplica as migrations na subida da API."
    Write-Host ""
    Write-Host "Subir API também via Docker:" -ForegroundColor Cyan
    Write-Host "  .\scripts\docker-db.ps1 -WithBackend"
}

Write-Host ""
Write-Host "Parar:" -ForegroundColor Gray
Write-Host "  docker compose down"
Write-Host "Recriar banco do zero:" -ForegroundColor Gray
Write-Host "  .\scripts\docker-db.ps1 -Reset"
Write-Host ""
