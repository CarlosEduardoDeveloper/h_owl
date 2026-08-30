#!/usr/bin/env python3
"""Gerador temporario de codigo do dominio. Executar uma vez na raiz do backend."""
from __future__ import annotations

import os
from pathlib import Path

BASE = Path(__file__).resolve().parent / "src/main/java/com/example/foundation"
PKG = "com.example.foundation"

ENUMS = {
    "modules/user/domain/enums/UsuarioStatus.java": ["ATIVO", "INATIVO", "BLOQUEADO"],
    "modules/user/domain/enums/TemaAplicacao.java": ["CLARO", "ESCURO", "SISTEMA"],
    "modules/study/domain/enums/IntencaoEstudo.java": ["LEITURA_LIVRE", "TRILHA", "REVISAO"],
    "modules/study/domain/enums/SessaoEstudoStatus.java": [
        "CRIADA", "EM_ANDAMENTO", "CONCLUIDA", "INTERROMPIDA", "CANCELADA"
    ],
    "modules/study/domain/enums/TipoConteudoSessao.java": ["BIBLIA", "ARTIGO", "TRILHA_MODULO", "REVISAO"],
    "modules/study/domain/enums/TipoDestaque.java": ["BIBLIA", "ARTIGO", "TRILHA_MODULO"],
    "modules/learning/domain/enums/NivelDificuldade.java": ["INICIANTE", "INTERMEDIARIO", "AVANCADO"],
    "modules/learning/domain/enums/TipoConteudo.java": ["TEXTO", "BIBLIA", "VIDEO", "IMAGEM", "PDF"],
    "modules/learning/domain/enums/ProgressoTrilhaStatus.java": ["NAO_INICIADO", "EM_ANDAMENTO", "CONCLUIDO"],
    "modules/gamification/domain/enums/Raridade.java": ["COMUM", "INCOMUM", "RARA", "EPICA", "LENDARIA"],
    "modules/gamification/domain/enums/OvoStatus.java": ["INCUBANDO", "CHOCADO", "QUEBRADO", "CANCELADO"],
    "modules/gamification/domain/enums/TipoRecompensa.java": ["CORUJA", "CONQUISTA", "PROGRESSO", "BONUS", "OUTRA"],
    "modules/gamification/domain/enums/TipoDecoracao.java": ["POLEIRO", "PLANTA", "CENARIO", "OBJETO", "OUTRO"],
    "modules/review/domain/enums/TipoItemRevisao.java": [
        "VERSICULO", "NOTA", "CONCEITO", "QUIZ_ERRO", "DESTAQUE", "CONSULTA_SABIO"
    ],
}

# (java_field, java_type, column, enum_pkg_suffix or None, relation or None)
# relation: (field_name, EntitySimpleName, column, module_path for import)
ENTITIES = [
    {
        "module": "user", "entity": "Pessoa", "table": "pessoa", "label": "Pessoa",
        "fields": [
            ("nome", "String", "nome", None),
            ("dataNascimento", "LocalDate", "data_nascimento", None),
            ("genero", "String", "genero", None),
            ("fotoUrl", "String", "foto_url", None),
        ],
        "relations": [],
    },
    {
        "module": "user", "entity": "Usuario", "table": "usuario", "label": "Usuario",
        "fields": [
            ("email", "String", "email", None),
            ("senhaHash", "String", "senha_hash", None),
            ("status", "UsuarioStatus", "status", "modules.user.domain.enums.UsuarioStatus"),
        ],
        "relations": [("pessoa", "Pessoa", "pessoa_id", "user")],
    },
    {
        "module": "user", "entity": "PreferenciaUsuario", "table": "preferencia_usuario", "label": "PreferenciaUsuario",
        "fields": [
            ("tema", "TemaAplicacao", "tema", "modules.user.domain.enums.TemaAplicacao"),
            ("modoFocoPadrao", "ModoFoco", "modo_foco_padrao", "shared.domain.enums.ModoFoco"),
            ("notificacoesAtivas", "Boolean", "notificacoes_ativas", None),
            ("duracaoFocoPadrao", "Integer", "duracao_foco_padrao", None),
            ("versaoBibliaPreferida", "String", "versao_biblia_preferida", None),
        ],
        "relations": [("usuario", "Usuario", "usuario_id", "user")],
    },
    {
        "module": "study", "entity": "SessaoEstudo", "table": "sessao_estudo", "label": "SessaoEstudo",
        "fields": [
            ("intencao", "IntencaoEstudo", "intencao", "modules.study.domain.enums.IntencaoEstudo"),
            ("modoFoco", "ModoFoco", "modo_foco", "shared.domain.enums.ModoFoco"),
            ("duracaoPlanejadaMinutos", "Integer", "duracao_planejada_minutos", None),
            ("duracaoRealMinutos", "Integer", "duracao_real_minutos", None),
            ("inicioEm", "Instant", "inicio_em", None),
            ("fimEm", "Instant", "fim_em", None),
            ("status", "SessaoEstudoStatus", "status", "modules.study.domain.enums.SessaoEstudoStatus"),
        ],
        "relations": [("usuario", "Usuario", "usuario_id", "user")],
    },
    {
        "module": "study", "entity": "ConteudoSessao", "table": "conteudo_sessao", "label": "ConteudoSessao",
        "fields": [
            ("tipo", "TipoConteudoSessao", "tipo", "modules.study.domain.enums.TipoConteudoSessao"),
            ("referenciaId", "UUID", "referencia_id", None),
            ("titulo", "String", "titulo", None),
            ("descricao", "String", "descricao", None),
            ("ordem", "Integer", "ordem", None),
        ],
        "relations": [("sessaoEstudo", "SessaoEstudo", "sessao_estudo_id", "study")],
    },
    {
        "module": "study", "entity": "Nota", "table": "nota", "label": "Nota",
        "fields": [
            ("titulo", "String", "titulo", None),
            ("conteudo", "String", "conteudo", None),
        ],
        "relations": [
            ("usuario", "Usuario", "usuario_id", "user"),
            ("sessaoEstudo", "SessaoEstudo", "sessao_estudo_id", "study"),
        ],
    },
    {
        "module": "study", "entity": "Destaque", "table": "destaque", "label": "Destaque",
        "fields": [
            ("tipo", "TipoDestaque", "tipo", "modules.study.domain.enums.TipoDestaque"),
            ("referenciaId", "UUID", "referencia_id", None),
            ("texto", "String", "texto", None),
            ("cor", "String", "cor", None),
        ],
        "relations": [
            ("usuario", "Usuario", "usuario_id", "user"),
            ("sessaoEstudo", "SessaoEstudo", "sessao_estudo_id", "study"),
        ],
    },
    {
        "module": "learning", "entity": "Trilha", "table": "trilha", "label": "Trilha",
        "fields": [
            ("titulo", "String", "titulo", None),
            ("descricao", "String", "descricao", None),
            ("imagemUrl", "String", "imagem_url", None),
            ("nivelDificuldade", "NivelDificuldade", "nivel_dificuldade", "modules.learning.domain.enums.NivelDificuldade"),
            ("ordem", "Integer", "ordem", None),
        ],
        "relations": [],
    },
    {
        "module": "learning", "entity": "Modulo", "table": "modulo", "label": "Modulo",
        "fields": [
            ("titulo", "String", "titulo", None),
            ("descricao", "String", "descricao", None),
            ("ordem", "Integer", "ordem", None),
            ("tempoSugeridoMinutos", "Integer", "tempo_sugerido_minutos", None),
        ],
        "relations": [("trilha", "Trilha", "trilha_id", "learning")],
    },
    {
        "module": "learning", "entity": "Conteudo", "table": "conteudo", "label": "Conteudo",
        "fields": [
            ("tipo", "TipoConteudo", "tipo", "modules.learning.domain.enums.TipoConteudo"),
            ("titulo", "String", "titulo", None),
            ("conteudo", "String", "conteudo", None),
            ("url", "String", "url", None),
            ("ordem", "Integer", "ordem", None),
        ],
        "relations": [("modulo", "Modulo", "modulo_id", "learning")],
    },
    {
        "module": "learning", "entity": "ProgressoTrilha", "table": "progresso_trilha", "label": "ProgressoTrilha",
        "fields": [
            ("status", "ProgressoTrilhaStatus", "status", "modules.learning.domain.enums.ProgressoTrilhaStatus"),
            ("progressoPercentual", "Integer", "progresso_percentual", None),
            ("ultimoAcessoEm", "Instant", "ultimo_acesso_em", None),
            ("concluidoEm", "Instant", "concluido_em", None),
        ],
        "relations": [
            ("usuario", "Usuario", "usuario_id", "user"),
            ("trilha", "Trilha", "trilha_id", "learning"),
            ("moduloAtual", "Modulo", "modulo_atual_id", "learning"),
        ],
    },
    {
        "module": "quiz", "entity": "Quiz", "table": "quiz", "label": "Quiz",
        "fields": [
            ("titulo", "String", "titulo", None),
            ("descricao", "String", "descricao", None),
            ("ordem", "Integer", "ordem", None),
        ],
        "relations": [("modulo", "Modulo", "modulo_id", "learning")],
    },
    {
        "module": "quiz", "entity": "Questao", "table": "questao", "label": "Questao",
        "fields": [
            ("enunciado", "String", "enunciado", None),
            ("ordem", "Integer", "ordem", None),
        ],
        "relations": [("quiz", "Quiz", "quiz_id", "quiz")],
    },
    {
        "module": "quiz", "entity": "Alternativa", "table": "alternativa", "label": "Alternativa",
        "fields": [
            ("texto", "String", "texto", None),
            ("correta", "Boolean", "correta", None),
            ("ordem", "Integer", "ordem", None),
        ],
        "relations": [("questao", "Questao", "questao_id", "quiz")],
    },
    {
        "module": "quiz", "entity": "TentativaQuiz", "table": "tentativa_quiz", "label": "TentativaQuiz",
        "fields": [
            ("pontuacao", "Integer", "pontuacao", None),
            ("acertos", "Integer", "acertos", None),
            ("totalQuestoes", "Integer", "total_questoes", None),
            ("realizadoEm", "Instant", "realizado_em", None),
        ],
        "relations": [
            ("usuario", "Usuario", "usuario_id", "user"),
            ("quiz", "Quiz", "quiz_id", "quiz"),
            ("sessaoEstudo", "SessaoEstudo", "sessao_estudo_id", "study"),
        ],
    },
    {
        "module": "quiz", "entity": "RespostaQuestao", "table": "resposta_questao", "label": "RespostaQuestao",
        "fields": [
            ("correta", "Boolean", "correta", None),
        ],
        "relations": [
            ("tentativaQuiz", "TentativaQuiz", "tentativa_quiz_id", "quiz"),
            ("questao", "Questao", "questao_id", "quiz"),
            ("alternativa", "Alternativa", "alternativa_id", "quiz"),
        ],
    },
    {
        "module": "gamification", "entity": "TipoOvo", "table": "tipo_ovo", "label": "TipoOvo",
        "fields": [
            ("nome", "String", "nome", None),
            ("raridade", "Raridade", "raridade", "modules.gamification.domain.enums.Raridade"),
            ("duracaoMinimaMinutos", "Integer", "duracao_minima_minutos", None),
            ("duracaoMaximaMinutos", "Integer", "duracao_maxima_minutos", None),
            ("imagemUrl", "String", "imagem_url", None),
        ],
        "relations": [],
    },
    {
        "module": "gamification", "entity": "OvoUsuario", "table": "ovo_usuario", "label": "OvoUsuario",
        "fields": [
            ("status", "OvoStatus", "status", "modules.gamification.domain.enums.OvoStatus"),
            ("chocadoEm", "Instant", "chocado_em", None),
        ],
        "relations": [
            ("usuario", "Usuario", "usuario_id", "user"),
            ("tipoOvo", "TipoOvo", "tipo_ovo_id", "gamification"),
            ("sessaoEstudo", "SessaoEstudo", "sessao_estudo_id", "study"),
        ],
    },
    {
        "module": "gamification", "entity": "Coruja", "table": "coruja", "label": "Coruja",
        "fields": [
            ("nome", "String", "nome", None),
            ("especie", "String", "especie", None),
            ("raridade", "Raridade", "raridade", "modules.gamification.domain.enums.Raridade"),
            ("descricao", "String", "descricao", None),
            ("imagemUrl", "String", "imagem_url", None),
        ],
        "relations": [],
    },
    {
        "module": "gamification", "entity": "CorujaUsuario", "table": "coruja_usuario", "label": "CorujaUsuario",
        "fields": [
            ("adquiridaEm", "Instant", "adquirida_em", None),
            ("nivel", "Integer", "nivel", None),
            ("experiencia", "Integer", "experiencia", None),
            ("observacoes", "String", "observacoes", None),
        ],
        "relations": [
            ("usuario", "Usuario", "usuario_id", "user"),
            ("coruja", "Coruja", "coruja_id", "gamification"),
            ("sessaoEstudo", "SessaoEstudo", "sessao_estudo_id", "study"),
        ],
    },
    {
        "module": "gamification", "entity": "Recompensa", "table": "recompensa", "label": "Recompensa",
        "fields": [
            ("tipo", "TipoRecompensa", "tipo", "modules.gamification.domain.enums.TipoRecompensa"),
            ("titulo", "String", "titulo", None),
            ("descricao", "String", "descricao", None),
            ("concedidaEm", "Instant", "concedida_em", None),
        ],
        "relations": [
            ("usuario", "Usuario", "usuario_id", "user"),
            ("coruja", "Coruja", "coruja_id", "gamification"),
            ("sessaoEstudo", "SessaoEstudo", "sessao_estudo_id", "study"),
            ("ovoUsuario", "OvoUsuario", "ovo_usuario_id", "gamification"),
        ],
    },
    {
        "module": "gamification", "entity": "Viveiro", "table": "viveiro", "label": "Viveiro",
        "fields": [
            ("nome", "String", "nome", None),
            ("nivel", "Integer", "nivel", None),
            ("xpTotal", "Long", "xp_total", None),
            ("temaVisual", "String", "tema_visual", None),
        ],
        "relations": [("usuario", "Usuario", "usuario_id", "user")],
    },
    {
        "module": "gamification", "entity": "DecoracaoViveiro", "table": "decoracao_viveiro", "label": "DecoracaoViveiro",
        "fields": [
            ("nome", "String", "nome", None),
            ("tipo", "TipoDecoracao", "tipo", "modules.gamification.domain.enums.TipoDecoracao"),
            ("imagemUrl", "String", "imagem_url", None),
            ("adquiridaEm", "Instant", "adquirida_em", None),
        ],
        "relations": [("viveiro", "Viveiro", "viveiro_id", "gamification")],
    },
    {
        "module": "gamification", "entity": "Conquista", "table": "conquista", "label": "Conquista",
        "fields": [
            ("nome", "String", "nome", None),
            ("descricao", "String", "descricao", None),
            ("iconeUrl", "String", "icone_url", None),
            ("categoria", "String", "categoria", None),
            ("regra", "String", "regra", None),
        ],
        "relations": [],
    },
    {
        "module": "gamification", "entity": "ConquistaUsuario", "table": "conquista_usuario", "label": "ConquistaUsuario",
        "fields": [
            ("conquistadaEm", "Instant", "conquistada_em", None),
            ("progresso", "Integer", "progresso", None),
        ],
        "relations": [
            ("usuario", "Usuario", "usuario_id", "user"),
            ("conquista", "Conquista", "conquista_id", "gamification"),
        ],
    },
    {
        "module": "sage", "entity": "ConsultaSabio", "table": "consulta_sabio", "label": "ConsultaSabio",
        "fields": [
            ("pergunta", "String", "pergunta", None),
            ("resposta", "String", "resposta", None),
            ("contextoReferencia", "String", "contexto_referencia", None),
        ],
        "relations": [
            ("usuario", "Usuario", "usuario_id", "user"),
            ("sessaoEstudo", "SessaoEstudo", "sessao_estudo_id", "study"),
        ],
    },
    {
        "module": "review", "entity": "ItemRevisao", "table": "item_revisao", "label": "ItemRevisao",
        "fields": [
            ("tipo", "TipoItemRevisao", "tipo", "modules.review.domain.enums.TipoItemRevisao"),
            ("referenciaId", "UUID", "referencia_id", None),
            ("proximaRevisaoEm", "Instant", "proxima_revisao_em", None),
            ("intervaloDias", "Integer", "intervalo_dias", None),
            ("facilidade", "Integer", "facilidade", None),
            ("repeticoes", "Integer", "repeticoes", None),
        ],
        "relations": [("usuario", "Usuario", "usuario_id", "user")],
    },
]

MODULE_PKG = {
    "user": f"{PKG}.modules.user",
    "study": f"{PKG}.modules.study",
    "learning": f"{PKG}.modules.learning",
    "quiz": f"{PKG}.modules.quiz",
    "gamification": f"{PKG}.modules.gamification",
    "sage": f"{PKG}.modules.sage",
    "review": f"{PKG}.modules.review",
}

REL_PKG = {
    "user": f"{PKG}.modules.user.domain",
    "study": f"{PKG}.modules.study.domain",
    "learning": f"{PKG}.modules.learning.domain",
    "quiz": f"{PKG}.modules.quiz.domain",
    "gamification": f"{PKG}.modules.gamification.domain",
    "sage": f"{PKG}.modules.sage.domain",
    "review": f"{PKG}.modules.review.domain",
}


def pkg_from_enum(enum_ref: str) -> tuple[str, str]:
    if enum_ref.startswith("shared."):
        p = f"{PKG}." + enum_ref.replace(".", ".")
        # shared.domain.enums.ModoFoco -> com.example.foundation.shared.domain.enums
        parts = enum_ref.split(".")
        simple = parts[-1]
        pkg = PKG + "." + ".".join(parts[:-1])
        return pkg, simple
    parts = enum_ref.split(".")
    simple = parts[-1]
    pkg = PKG + "." + ".".join(parts[:-1])
    return pkg, simple


def write(path: Path, content: str) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(content, encoding="utf-8")


def gen_enum(rel_path: str, values: list[str]) -> None:
    name = Path(rel_path).stem
    pkg = PKG + "." + Path(rel_path).parent.as_posix().replace("/", ".")
    body = "    " + ",\n    ".join(values) + "\n"
    content = f"""package {pkg};

public enum {name} {{
{body}}}
"""
    write(BASE / rel_path, content)


def is_text_field(column: str, java_field: str) -> bool:
    return column in {"descricao", "conteudo", "enunciado", "texto", "pergunta", "resposta", "observacoes", "regra"} or java_field in {"descricao", "conteudo", "enunciado", "texto", "pergunta", "resposta", "observacoes", "regra"}


def gen_entity(meta: dict) -> None:
    module = meta["module"]
    entity = meta["entity"]
    table = meta["table"]
    mod_pkg = MODULE_PKG[module]
    imports = {
        "com.example.foundation.shared.domain.BaseEntity",
        "jakarta.persistence.Column",
        "jakarta.persistence.Entity",
        "jakarta.persistence.EnumType",
        "jakarta.persistence.Enumerated",
        "jakarta.persistence.FetchType",
        "jakarta.persistence.JoinColumn",
        "jakarta.persistence.ManyToOne",
        "jakarta.persistence.Table",
    }
    fields_code = []
    getters_setters = []

    for java_field, java_type, column, enum_ref in meta["fields"]:
        if enum_ref:
            pkg, simple = pkg_from_enum(enum_ref)
            imports.add(f"{pkg}.{simple}")
        if java_type == "UUID":
            imports.add("java.util.UUID")
        if java_type == "Instant":
            imports.add("java.time.Instant")
        if java_type == "LocalDate":
            imports.add("java.time.LocalDate")

        col_ann = f'@Column(name = "{column}"'
        if is_text_field(column, java_field):
            col_ann += ', columnDefinition = "TEXT"'
        col_ann += ")"
        enum_ann = ""
        if enum_ref:
            enum_ann = "\n    @Enumerated(EnumType.STRING)"
        fields_code.append(f"{enum_ann}\n    {col_ann}\n    private {java_type} {java_field};")
        cap = java_field[0].upper() + java_field[1:]
        getters_setters.append(f"""
    public {java_type} get{cap}() {{
        return {java_field};
    }}

    public void set{cap}({java_type} {java_field}) {{
        this.{java_field} = {java_field};
    }}""")

    for rel_field, rel_entity, column, rel_module in meta["relations"]:
        rel_pkg = REL_PKG[rel_module]
        imports.add(f"{rel_pkg}.{rel_entity}")
        fields_code.append(f"""
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "{column}")
    private {rel_entity} {rel_field};""")
        cap = rel_field[0].upper() + rel_field[1:]
        getters_setters.append(f"""
    public {rel_entity} get{cap}() {{
        return {rel_field};
    }}

    public void set{cap}({rel_entity} {rel_field}) {{
        this.{rel_field} = {rel_field};
    }}""")

    import_block = "\n".join(f"import {i};" for i in sorted(imports))
    gs = "\n".join(getters_setters)
    fields = "\n".join(fields_code)
    content = f"""package {mod_pkg}.domain;

{import_block}

@Entity
@Table(name = "{table}")
public class {entity} extends BaseEntity {{
{fields}
{gs}
}}
"""
    write(BASE / f"modules/{module}/domain/{entity}.java", content)


def fk_id_field(rel_field: str) -> str:
    return rel_field + "Id"


def gen_dto(meta: dict) -> None:
    module = meta["module"]
    entity = meta["entity"]
    mod_pkg = MODULE_PKG[module]
    imports = {"java.util.UUID", "java.time.Instant", "java.time.LocalDate"}

    dto_fields = ["        UUID id,"]
    for java_field, java_type, column, enum_ref in meta["fields"]:
        if enum_ref:
            pkg, simple = pkg_from_enum(enum_ref)
            imports.add(f"{pkg}.{simple}")
        dto_fields.append(f"        {java_type} {java_field},")

    for rel_field, rel_entity, column, rel_module in meta["relations"]:
        dto_fields.append(f"        UUID {fk_id_field(rel_field)},")

    dto_fields.extend([
        "        Boolean ativo,",
        "        Instant criadoEm,",
        "        Instant atualizadoEm,",
        "        Instant excluidoEm",
    ])

    req_fields = []
    for java_field, java_type, column, enum_ref in meta["fields"]:
        if enum_ref:
            pkg, simple = pkg_from_enum(enum_ref)
            imports.add(f"{pkg}.{simple}")
        req_fields.append(f"        {java_type} {java_field},")
    for rel_field, _, _, _ in meta["relations"]:
        req_fields.append(f"        UUID {fk_id_field(rel_field)},")

    import_block = "\n".join(f"import {i};" for i in sorted(imports))
    response = "\n".join(dto_fields)
    request = "\n".join(req_fields)

    write(
        BASE / f"modules/{module}/dto/{entity}Response.java",
        f"""package {mod_pkg}.dto;

{import_block}

public record {entity}Response(
{response}
) {{
}}
""",
    )
    write(
        BASE / f"modules/{module}/dto/{entity}Request.java",
        f"""package {mod_pkg}.dto;

{import_block}

public record {entity}Request(
{request}
) {{
}}
""",
    )


def gen_repository(meta: dict) -> None:
    module = meta["module"]
    entity = meta["entity"]
    mod_pkg = MODULE_PKG[module]
    content = f"""package {mod_pkg}.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import {mod_pkg}.domain.{entity};
import org.springframework.data.jpa.repository.JpaRepository;

public interface {entity}Repository extends JpaRepository<{entity}, UUID> {{

    List<{entity}> findByAtivoTrue();

    Optional<{entity}> findByIdAndAtivoTrue(UUID id);
}}
"""
    write(BASE / f"modules/{module}/repository/{entity}Repository.java", content)


def gen_mapper(meta: dict) -> None:
    module = meta["module"]
    entity = meta["entity"]
    label = meta["label"]
    mod_pkg = MODULE_PKG[module]
    imports = {
        f"{mod_pkg}.domain.{entity}",
        f"{mod_pkg}.dto.{entity}Request",
        f"{mod_pkg}.dto.{entity}Response",
    }
    for _, rel_entity, _, rel_module in meta["relations"]:
        if rel_module != module:
            imports.add(f"{REL_PKG[rel_module]}.{rel_entity}")

    to_response_lines = ["                entity.getId(),"]
    for java_field, _, _, _ in meta["fields"]:
        to_response_lines.append(f"                entity.get{java_field[0].upper() + java_field[1:]}(),")
    for rel_field, _, _, _ in meta["relations"]:
        cap = rel_field[0].upper() + rel_field[1:]
        to_response_lines.append(
            f"                entity.get{cap}() != null ? entity.get{cap}().getId() : null,"
        )
    to_response_lines.extend([
        "                entity.getAtivo(),",
        "                entity.getCriadoEm(),",
        "                entity.getAtualizadoEm(),",
        "                entity.getExcluidoEm()",
    ])

    apply_lines = []
    for java_field, _, _, _ in meta["fields"]:
        cap = java_field[0].upper() + java_field[1:]
        apply_lines.append(f"        entity.set{cap}(request.{java_field}());")

    import_block = "\n".join(f"import {i};" for i in sorted(imports))
    content = f"""package {mod_pkg}.mapper;

{import_block}

public final class {entity}Mapper {{

    private {entity}Mapper() {{
    }}

    public static {entity}Response toResponse({entity} entity) {{
        return new {entity}Response(
{chr(10).join(to_response_lines)}
        );
    }}

    public static {entity} toEntity({entity}Request request) {{
        {entity} entity = new {entity}();
        applyRequest(entity, request);
        return entity;
    }}

    public static void applyRequest({entity} entity, {entity}Request request) {{
{chr(10).join(apply_lines)}
    }}
}}
"""
    write(BASE / f"modules/{module}/mapper/{entity}Mapper.java", content)


def gen_service(meta: dict) -> None:
    module = meta["module"]
    entity = meta["entity"]
    label = meta["label"]
    mod_pkg = MODULE_PKG[module]
    rel_repo_injects = []
    rel_set_lines = []
    imports = {
        "java.util.List",
        "java.util.UUID",
        f"{mod_pkg}.domain.{entity}",
        f"{mod_pkg}.dto.{entity}Request",
        f"{mod_pkg}.dto.{entity}Response",
        f"{mod_pkg}.mapper.{entity}Mapper",
        f"{mod_pkg}.repository.{entity}Repository",
        "com.example.foundation.shared.exception.RecursoNaoEncontradoException",
        "org.springframework.stereotype.Service",
        "org.springframework.transaction.annotation.Transactional",
    }

    constructor_params = [f"{entity}Repository repository"]
    constructor_assign = ["        this.repository = repository;"]
    fields_decl = [f"    private final {entity}Repository repository;"]

    idx = 0
    seen_repos = set()
    for rel_field, rel_entity, _, rel_module in meta["relations"]:
        repo_key = (rel_module, rel_entity)
        if repo_key in seen_repos:
            continue
        seen_repos.add(repo_key)
        var = rel_entity[0].lower() + rel_entity[1:] + "Repository"
        rel_mod_pkg = MODULE_PKG[rel_module]
        imports.add(f"{rel_mod_pkg}.repository.{rel_entity}Repository")
        imports.add(f"{REL_PKG[rel_module]}.{rel_entity}")
        fields_decl.append(f"    private final {rel_entity}Repository {var};")
        constructor_params.append(f"{rel_entity}Repository {var}")
        constructor_assign.append(f"        this.{var} = {var};")

    for rel_field, rel_entity, _, rel_module in meta["relations"]:
        var = rel_entity[0].lower() + rel_entity[1:] + "Repository"
        cap = rel_field[0].upper() + rel_field[1:]
        rel_set_lines.append(f"""
        if (request.{fk_id_field(rel_field)}() != null) {{
            {rel_entity} {rel_field} = {var}.findById(request.{fk_id_field(rel_field)}())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("{rel_entity}", request.{fk_id_field(rel_field)}()));
            entity.set{cap}({rel_field});
        }} else {{
            entity.set{cap}(null);
        }}""")

    import_block = "\n".join(f"import {i};" for i in sorted(imports))
    params = ",\n            ".join(constructor_params)
    assigns = "\n".join(constructor_assign)
    fields = "\n".join(fields_decl)
    rel_apply = "\n".join(rel_set_lines)

    content = f"""package {mod_pkg}.service;

{import_block}

@Service
@Transactional
public class {entity}Service {{

{fields}

    public {entity}Service(
            {params}
    ) {{
{assigns}
    }}

    @Transactional(readOnly = true)
    public List<{entity}Response> listarAtivos() {{
        return repository.findByAtivoTrue().stream()
                .map({entity}Mapper::toResponse)
                .toList();
    }}

    @Transactional(readOnly = true)
    public {entity}Response buscarAtivo(UUID id) {{
        return {entity}Mapper.toResponse(buscarEntidadeAtiva(id));
    }}

    public {entity}Response criar({entity}Request request) {{
        {entity} entity = {entity}Mapper.toEntity(request);
        aplicarRelacionamentos(entity, request);
        return {entity}Mapper.toResponse(repository.save(entity));
    }}

    public {entity}Response atualizar(UUID id, {entity}Request request) {{
        {entity} entity = buscarEntidadeAtiva(id);
        {entity}Mapper.applyRequest(entity, request);
        aplicarRelacionamentos(entity, request);
        return {entity}Mapper.toResponse(repository.save(entity));
    }}

    public void excluir(UUID id) {{
        {entity} entity = buscarEntidadeAtiva(id);
        entity.excluirLogicamente();
        repository.save(entity);
    }}

    private {entity} buscarEntidadeAtiva(UUID id) {{
        return repository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("{label}", id));
    }}

    private void aplicarRelacionamentos({entity} entity, {entity}Request request) {{
{rel_apply if rel_apply else "        // sem relacionamentos externos"}
    }}
}}
"""
    write(BASE / f"modules/{module}/service/{entity}Service.java", content)


def main() -> None:
    for rel, values in ENUMS.items():
        gen_enum(rel, values)
    for meta in ENTITIES:
        gen_entity(meta)
        gen_dto(meta)
        gen_repository(meta)
        gen_mapper(meta)
        gen_service(meta)
    print(f"Gerados {len(ENTITIES)} entidades em {BASE}")


if __name__ == "__main__":
    main()
