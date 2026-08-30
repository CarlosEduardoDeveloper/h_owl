# Gera controllers CRUD para todas as entidades do dominio.
$ErrorActionPreference = "Stop"
$Base = Join-Path $PSScriptRoot "src/main/java/com/example/foundation"
$Pkg = "com.example.foundation"

$Controllers = @(
    @("user", "Pessoa", "pessoas"),
    @("user", "Usuario", "usuarios"),
    @("user", "PreferenciaUsuario", "preferencias-usuario"),
    @("study", "SessaoEstudo", "sessoes-estudo"),
    @("study", "ConteudoSessao", "conteudos-sessao"),
    @("study", "Nota", "notas"),
    @("study", "Destaque", "destaques"),
    @("learning", "Trilha", "trilhas"),
    @("learning", "Modulo", "modulos"),
    @("learning", "Conteudo", "conteudos"),
    @("learning", "ProgressoTrilha", "progressos-trilha"),
    @("quiz", "Quiz", "quizzes"),
    @("quiz", "Questao", "questoes"),
    @("quiz", "Alternativa", "alternativas"),
    @("quiz", "TentativaQuiz", "tentativas-quiz"),
    @("quiz", "RespostaQuestao", "respostas-questao"),
    @("gamification", "TipoOvo", "tipos-ovo"),
    @("gamification", "OvoUsuario", "ovos-usuario"),
    @("gamification", "Coruja", "corujas"),
    @("gamification", "CorujaUsuario", "corujas-usuario"),
    @("gamification", "Recompensa", "recompensas"),
    @("gamification", "Viveiro", "viveiros"),
    @("gamification", "DecoracaoViveiro", "decoracoes-viveiro"),
    @("gamification", "Conquista", "conquistas"),
    @("gamification", "ConquistaUsuario", "conquistas-usuario"),
    @("sage", "ConsultaSabio", "consultas-sabio"),
    @("review", "ItemRevisao", "itens-revisao")
)

foreach ($c in $Controllers) {
    $module, $entity, $route = $c
    $modPkg = "$Pkg.modules.$module"
    $dir = Join-Path $Base "modules/$module/controller"
    New-Item -ItemType Directory -Force -Path $dir | Out-Null
    $path = Join-Path $dir "${entity}Controller.java"
    $content = @"
package $modPkg.controller;

import $modPkg.dto.${entity}Request;
import $modPkg.dto.${entity}Response;
import $modPkg.service.${entity}Service;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/$route")
public class ${entity}Controller {

    private final ${entity}Service service;

    public ${entity}Controller(${entity}Service service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<${entity}Response> cadastrar(@RequestBody ${entity}Request request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(request));
    }

    @GetMapping
    public List<${entity}Response> listar() {
        return service.listarAtivos();
    }

    @GetMapping("/{id}")
    public ${entity}Response buscar(@PathVariable UUID id) {
        return service.buscarAtivo(id);
    }

    @PutMapping("/{id}")
    public ${entity}Response atualizar(@PathVariable UUID id, @RequestBody ${entity}Request request) {
        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable UUID id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
"@
    [System.IO.File]::WriteAllText($path, $content, [System.Text.UTF8Encoding]::new($false))
}

# Corrigir FK lookups para considerar apenas registros ativos
Get-ChildItem -Path (Join-Path $Base "modules") -Recurse -Filter "*Service.java" | ForEach-Object {
    $text = [System.IO.File]::ReadAllText($_.FullName)
    $updated = $text -replace '\.findById\(', '.findByIdAndAtivoTrue('
    if ($updated -ne $text) {
        [System.IO.File]::WriteAllText($_.FullName, $updated, [System.Text.UTF8Encoding]::new($false))
    }
}

Write-Host "Gerados $($Controllers.Count) controllers"
