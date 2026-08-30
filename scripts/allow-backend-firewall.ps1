# Libera a porta 8080 do backend na rede privada (Wi-Fi local).
# Execute como Administrador:
#   PowerShell → clique direito → Executar como administrador
#   cd c:\Users\eduar\OneDrive\Documentos\repos\h_owl
#   .\scripts\allow-backend-firewall.ps1

$ErrorActionPreference = "Stop"
$RuleName = "H Owl Backend 8080"

$existing = netsh advfirewall firewall show rule name="$RuleName" 2>$null
if ($LASTEXITCODE -eq 0) {
    Write-Host "Regra '$RuleName' já existe." -ForegroundColor Green
    exit 0
}

netsh advfirewall firewall add rule name="$RuleName" dir=in action=allow protocol=TCP localport=8080 profile=private
Write-Host "Porta 8080 liberada na rede privada." -ForegroundColor Green
Write-Host "Teste no celular: http://SEU_IP:8080/actuator/health" -ForegroundColor Cyan
