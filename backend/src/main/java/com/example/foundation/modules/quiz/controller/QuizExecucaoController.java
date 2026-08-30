package com.example.foundation.modules.quiz.controller;

import java.util.UUID;

import com.example.foundation.modules.auth.controller.AuthController;
import com.example.foundation.modules.quiz.dto.FinalizarTentativaQuizRequest;
import com.example.foundation.modules.quiz.dto.FinalizarTentativaQuizResponse;
import com.example.foundation.modules.quiz.dto.IniciarTentativaQuizResponse;
import com.example.foundation.modules.quiz.dto.QuizJogarResponse;
import com.example.foundation.modules.quiz.service.QuizExecucaoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/quizzes")
public class QuizExecucaoController {

    private final QuizExecucaoService quizExecucaoService;

    public QuizExecucaoController(QuizExecucaoService quizExecucaoService) {
        this.quizExecucaoService = quizExecucaoService;
    }

    @GetMapping("/{id}/jogar")
    public QuizJogarResponse jogar(@PathVariable UUID id) {
        return quizExecucaoService.montarQuizParaJogar(id);
    }

    @PostMapping("/{id}/tentativas")
    public IniciarTentativaQuizResponse iniciarTentativa(
            @PathVariable UUID id,
            @RequestAttribute(AuthController.ATRIBUTO_USUARIO_ID) UUID usuarioId
    ) {
        return quizExecucaoService.iniciarTentativa(id, usuarioId);
    }
}
