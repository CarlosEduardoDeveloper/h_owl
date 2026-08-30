package com.example.foundation.modules.quiz.controller;

import java.util.UUID;

import com.example.foundation.modules.auth.controller.AuthController;
import com.example.foundation.modules.quiz.dto.FinalizarTentativaQuizRequest;
import com.example.foundation.modules.quiz.dto.FinalizarTentativaQuizResponse;
import com.example.foundation.modules.quiz.service.QuizExecucaoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tentativas-quiz")
public class TentativaQuizExecucaoController {

    private final QuizExecucaoService quizExecucaoService;

    public TentativaQuizExecucaoController(QuizExecucaoService quizExecucaoService) {
        this.quizExecucaoService = quizExecucaoService;
    }

    @PostMapping("/{id}/finalizar")
    public FinalizarTentativaQuizResponse finalizar(
            @PathVariable UUID id,
            @RequestAttribute(AuthController.ATRIBUTO_USUARIO_ID) UUID usuarioId,
            @Valid @RequestBody FinalizarTentativaQuizRequest request
    ) {
        return quizExecucaoService.finalizarTentativa(id, usuarioId, request);
    }
}
