# Inicia o mobile Expo apontando para o backend local.
# Uso:
#   .\scripts\mobile-dev.ps1
#   .\scripts\mobile-dev.ps1 -ApiUrl "http://192.168.0.10:8080"
#   .\scripts\mobile-dev.ps1 -AndroidEmulator

param(
    [string]$ApiUrl,
    [switch]$AndroidEmulator
)

$ErrorActionPreference = "Stop"

$Root = Split-Path -Parent (Split-Path -Parent $MyInvocation.MyCommand.Path)
$MobileDir = Join-Path $Root "artifacts\mobile"
$EnvFile = Join-Path $MobileDir ".env"

if (-not $ApiUrl) {
    if ($AndroidEmulator) {
        $ApiUrl = "http://10.0.2.2:8080"
    } else {
        $lanIp = Get-NetIPAddress -AddressFamily IPv4 -ErrorAction SilentlyContinue |
            Where-Object {
                $_.IPAddress -notlike '127.*' -and
                $_.IPAddress -notlike '169.254.*' -and
                $_.PrefixOrigin -ne 'WellKnown'
            } |
            Sort-Object -Property InterfaceMetric |
            Select-Object -First 1 -ExpandProperty IPAddress
        if ($lanIp) {
            $ApiUrl = "http://${lanIp}:8080"
        } else {
            $ApiUrl = "http://localhost:8080"
        }
    }
}

$envContent = "EXPO_PUBLIC_API_BASE_URL=$ApiUrl"
Set-Content -Path $EnvFile -Value $envContent -Encoding UTF8

Write-Host ""
Write-Host "Corujas da Sabedoria — Mobile (Expo)" -ForegroundColor Cyan
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "API: $ApiUrl" -ForegroundColor Green
Write-Host ""

if ($ApiUrl -match "localhost" -and -not $AndroidEmulator) {
    Write-Host "Celular físico / Expo Go na rede:" -ForegroundColor Yellow
    Write-Host "  Use o IP da sua máquina, não localhost."
    Write-Host "  Exemplo: .\scripts\mobile-dev.ps1 -ApiUrl `"http://192.168.0.10:8080`""
    Write-Host ""
    Write-Host "Emulador Android:" -ForegroundColor Yellow
    Write-Host "  .\scripts\mobile-dev.ps1 -AndroidEmulator"
    Write-Host ""
}

Write-Host "Backend deve estar em: $ApiUrl/actuator/health" -ForegroundColor Gray
Write-Host ""

Set-Location $Root

if (-not (Get-Command pnpm -ErrorAction SilentlyContinue)) {
    throw "pnpm não encontrado. Rode: npm install -g pnpm"
}

if (-not (Test-Path (Join-Path $Root "node_modules"))) {
    Write-Host "Instalando dependências (pnpm install)..." -ForegroundColor Yellow
    pnpm install
}

Set-Location $MobileDir
Write-Host "Iniciando Expo... (Escaneie o QR no Expo Go ou pressione a/i para emulador)" -ForegroundColor Cyan
Write-Host ""

pnpm exec expo start
