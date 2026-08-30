import { useCallback, useEffect, useRef, useState } from 'react';
import { useLocalSearchParams } from 'expo-router';

import { useAuth } from '@/context/AuthContext';
import * as focusSessionStorage from '@/features/study/focusSessionStorage';
import * as studyService from '@/features/study/studyService';
import type { CriarSessaoEstudoInput } from '@/features/study/studyService';

const DURACAO_PADRAO_MINUTOS = 25;

type IntencaoEstudo = CriarSessaoEstudoInput['intencao'];

function formatTime(seconds: number): string {
  const minutes = Math.floor(seconds / 60);
  const secs = seconds % 60;
  return `${minutes.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`;
}

function resolveIntencao(value: string | string[] | undefined): IntencaoEstudo {
  const raw = Array.isArray(value) ? value[0] : value;
  if (raw === 'TRILHA' || raw === 'REVISAO' || raw === 'LEITURA_LIVRE') {
    return raw;
  }
  return 'LEITURA_LIVRE';
}

export function useFocusSession() {
  const { user } = useAuth();
  const params = useLocalSearchParams<{ intencao?: string | string[] }>();
  const intencao = resolveIntencao(params.intencao);

  const [secondsLeft, setSecondsLeft] = useState(DURACAO_PADRAO_MINUTOS * 60);
  const [isTimerRunning, setIsTimerRunning] = useState(false);

  const sessaoIdRef = useRef<string | null>(null);
  const concluidaRef = useRef(false);
  const duracaoPlanejadaRef = useRef(DURACAO_PADRAO_MINUTOS);

  const limparSessaoLocal = useCallback(async () => {
    sessaoIdRef.current = null;
    concluidaRef.current = false;
    await focusSessionStorage.clearActiveSessionId();
  }, []);

  const concluirSessaoAtiva = useCallback(async () => {
    const sessaoId = sessaoIdRef.current;
    if (!sessaoId || concluidaRef.current) {
      return;
    }

    concluidaRef.current = true;
    setIsTimerRunning(false);

    try {
      await studyService.concluirSessao(sessaoId, duracaoPlanejadaRef.current);
    } finally {
      await limparSessaoLocal();
      setSecondsLeft(duracaoPlanejadaRef.current * 60);
    }
  }, [limparSessaoLocal]);

  const interromperSessaoAtiva = useCallback(async () => {
    const sessaoId = sessaoIdRef.current;
    if (!sessaoId || concluidaRef.current) {
      return;
    }

    try {
      await studyService.interromperSessao(sessaoId);
    } finally {
      await limparSessaoLocal();
    }
  }, [limparSessaoLocal]);

  useEffect(() => {
    if (!isTimerRunning) {
      return;
    }

    const intervalId = setInterval(() => {
      setSecondsLeft((current) => {
        if (current <= 1) {
          clearInterval(intervalId);
          void concluirSessaoAtiva();
          return 0;
        }
        return current - 1;
      });
    }, 1000);

    return () => clearInterval(intervalId);
  }, [isTimerRunning, concluirSessaoAtiva]);

  useEffect(() => {
    return () => {
      if (sessaoIdRef.current && !concluidaRef.current) {
        void interromperSessaoAtiva();
      }
    };
  }, [interromperSessaoAtiva]);

  const toggleSession = useCallback(async () => {
    if (isTimerRunning) {
      setIsTimerRunning(false);
      return;
    }

    if (!user?.usuarioId) {
      return;
    }

    if (!sessaoIdRef.current) {
      try {
        const criada = await studyService.criarSessao({
          usuarioId: user.usuarioId,
          intencao,
          modoFoco: 'FLEXIVEL',
          duracaoPlanejadaMinutos: duracaoPlanejadaRef.current,
          status: 'CRIADA',
        });
        const iniciada = await studyService.iniciarSessao(criada.id);
        sessaoIdRef.current = iniciada.id;
        await focusSessionStorage.saveActiveSessionId(iniciada.id);
      } catch {
        return;
      }
    }

    setIsTimerRunning(true);
  }, [intencao, isTimerRunning, user?.usuarioId]);

  return {
    secondsLeft,
    isTimerRunning,
    toggleSession,
    formatTime,
  };
}
