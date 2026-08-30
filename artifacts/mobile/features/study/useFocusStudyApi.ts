import { useCallback, useRef } from 'react';

import { useAuth } from '@/context/AuthContext';
import * as studyService from '@/features/study/studyService';
import type { CriarSessaoEstudoInput } from '@/features/study/studyService';

type IntencaoEstudo = CriarSessaoEstudoInput['intencao'];

export const DURACOES_PERMITIDAS = [10, 15, 30] as const;
export type DuracaoPermitida = (typeof DURACOES_PERMITIDAS)[number];

export function snapDuracao(minutos: number): DuracaoPermitida {
  return DURACOES_PERMITIDAS.reduce((melhor, atual) =>
    Math.abs(atual - minutos) < Math.abs(melhor - minutos) ? atual : melhor,
  );
}

export function resolveIntencao(
  mode: 'livre' | 'direcionado' | null,
  param?: string | string[],
): IntencaoEstudo {
  const raw = Array.isArray(param) ? param[0] : param;
  if (raw === 'TRILHA' || raw === 'REVISAO' || raw === 'QUIZ' || raw === 'LEITURA_LIVRE') {
    return raw;
  }
  if (mode === 'direcionado') {
    return 'TRILHA';
  }
  return 'LEITURA_LIVRE';
}

export function useFocusStudyApi() {
  const { user } = useAuth();
  const sessaoIdRef = useRef<string | null>(null);
  const concluidaRef = useRef(false);

  const iniciarSessao = useCallback(
    async (intencao: IntencaoEstudo, duracaoMinutos: DuracaoPermitida) => {
      if (!user?.usuarioId || sessaoIdRef.current) {
        return sessaoIdRef.current;
      }

      const criada = await studyService.criarSessao({
        usuarioId: user.usuarioId,
        intencao,
        modoFoco: 'FLEXIVEL',
        duracaoPlanejadaMinutos: duracaoMinutos,
        status: 'CRIADA',
      });
      const iniciada = await studyService.iniciarSessao(criada.id);
      sessaoIdRef.current = iniciada.id;
      concluidaRef.current = false;
      return iniciada.id;
    },
    [user?.usuarioId],
  );

  const concluirSessao = useCallback(async (duracaoMinutos: DuracaoPermitida) => {
    const sessaoId = sessaoIdRef.current;
    if (!sessaoId || concluidaRef.current) {
      return null;
    }

    concluidaRef.current = true;
    const resultado = await studyService.concluirSessao(sessaoId, duracaoMinutos);
    sessaoIdRef.current = null;
    return resultado;
  }, []);

  const interromperSessao = useCallback(async () => {
    const sessaoId = sessaoIdRef.current;
    if (!sessaoId || concluidaRef.current) {
      return;
    }

    await studyService.interromperSessao(sessaoId);
    sessaoIdRef.current = null;
    concluidaRef.current = false;
  }, []);

  return {
    iniciarSessao,
    concluirSessao,
    interromperSessao,
  };
}
