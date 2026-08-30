package com.example.foundation.modules.gamification.service;

import com.example.foundation.modules.gamification.domain.CorujaUsuario;
import com.example.foundation.modules.gamification.domain.Viveiro;
import com.example.foundation.modules.gamification.repository.CorujaUsuarioRepository;
import com.example.foundation.modules.gamification.repository.ViveiroRepository;
import com.example.foundation.modules.user.domain.Usuario;
import com.example.foundation.modules.user.repository.UsuarioRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VerificacaoDiariaService {

    private final UsuarioRepository usuarioRepository;
    private final CorujaUsuarioRepository corujaUsuarioRepository;
    private final ViveiroRepository viveiroRepository;
    private final BiscoitoService biscoitoService;
    private final FlorestaStreakService florestaStreakService;

    public VerificacaoDiariaService(
            UsuarioRepository usuarioRepository,
            CorujaUsuarioRepository corujaUsuarioRepository,
            ViveiroRepository viveiroRepository,
            BiscoitoService biscoitoService,
            FlorestaStreakService florestaStreakService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.corujaUsuarioRepository = corujaUsuarioRepository;
        this.viveiroRepository = viveiroRepository;
        this.biscoitoService = biscoitoService;
        this.florestaStreakService = florestaStreakService;
    }

    public void executarParaTodosUsuarios() {
        usuarioRepository.findByAtivoTrue().forEach(this::executarParaUsuario);
    }

    public void executarParaUsuario(Usuario usuario) {
        LocalDate hoje = florestaStreakService.hoje(usuario);
        LocalDate ultima = usuario.getUltimaVerificacaoDiaria();
        if (ultima != null && !ultima.isBefore(hoje)) {
            return;
        }

        Viveiro viveiro = viveiroRepository
                .findFirstByUsuario_IdAndAtivoTrueOrderByCriadoEmDesc(usuario.getId())
                .orElse(null);

        List<CorujaUsuario> corujas = corujaUsuarioRepository.findByUsuario_IdAndAtivoTrue(usuario.getId());
        int corujasAtivas = corujas.size();

        if (viveiro != null && corujasAtivas > 0) {
            int consumidos = Math.min(biscoitoService.saldo(viveiro), corujasAtivas);
            if (consumidos > 0) {
                biscoitoService.consumirBiscoitos(viveiro, consumidos);
            }

            int indice = 0;
            for (CorujaUsuario coruja : corujas) {
                if (indice < consumidos) {
                    coruja.setDiasSemBiscoito(0);
                    coruja.setFeliz(true);
                } else {
                    int dias = coruja.getDiasSemBiscoito() != null ? coruja.getDiasSemBiscoito() : 0;
                    dias++;
                    coruja.setDiasSemBiscoito(dias);
                    coruja.setFeliz(false);
                    if (dias >= 3) {
                        coruja.excluirLogicamente();
                    }
                }
                indice++;
                corujaUsuarioRepository.save(coruja);
            }
        }

        usuario.setUltimaVerificacaoDiaria(hoje);
        usuarioRepository.save(usuario);
    }
}
