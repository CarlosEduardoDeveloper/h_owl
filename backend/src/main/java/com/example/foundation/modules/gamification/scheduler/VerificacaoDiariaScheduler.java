package com.example.foundation.modules.gamification.scheduler;

import com.example.foundation.modules.gamification.service.VerificacaoDiariaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class VerificacaoDiariaScheduler {

    private static final Logger log = LoggerFactory.getLogger(VerificacaoDiariaScheduler.class);

    private final VerificacaoDiariaService verificacaoDiariaService;

    public VerificacaoDiariaScheduler(VerificacaoDiariaService verificacaoDiariaService) {
        this.verificacaoDiariaService = verificacaoDiariaService;
    }

    @Scheduled(cron = "0 5 0 * * *")
    public void verificacaoDiaria() {
        log.info("Iniciando verificação diária de biscoitos e corujas");
        verificacaoDiariaService.executarParaTodosUsuarios();
    }
}
