import { useCallback, useEffect, useMemo, useState } from 'react';
import { Alert } from 'react-native';
import { useRouter } from 'expo-router';
import { useQueryClient } from '@tanstack/react-query';

import * as quizService from '@/features/quiz/quizService';
import type { QuizJogar, RespostaQuizItem } from '@/features/quiz/quizService';

export function useQuizPlayer(quizId: string | undefined) {
  const router = useRouter();
  const queryClient = useQueryClient();
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [tentativaId, setTentativaId] = useState<string | null>(null);
  const [quiz, setQuiz] = useState<QuizJogar | null>(null);
  const [respostas, setRespostas] = useState<Record<string, string>>({});

  useEffect(() => {
    if (!quizId) {
      setLoading(false);
      return;
    }

    let active = true;

    async function load() {
      if (!quizId) {
        return;
      }

      setLoading(true);
      try {
        const iniciada = await quizService.iniciarTentativa(quizId);
        if (!active) {
          return;
        }
        setTentativaId(iniciada.tentativaId);
        setQuiz(iniciada.quiz);
      } catch {
        if (active) {
          Alert.alert('Erro', 'Não foi possível carregar o quiz.');
          router.back();
        }
      } finally {
        if (active) {
          setLoading(false);
        }
      }
    }

    void load();

    return () => {
      active = false;
    };
  }, [quizId, router]);

  const questoesRespondidas = useMemo(() => Object.keys(respostas).length, [respostas]);
  const totalQuestoes = quiz?.questoes?.length ?? 0;
  const todasRespondidas = totalQuestoes > 0 && questoesRespondidas === totalQuestoes;

  const selecionarAlternativa = useCallback((questaoId: string, alternativaId: string) => {
    setRespostas((atual) => ({ ...atual, [questaoId]: alternativaId }));
  }, []);

  const finalizar = useCallback(async () => {
    if (!tentativaId || !quiz || !todasRespondidas) {
      return;
    }

    setSubmitting(true);
    try {
      const payload: RespostaQuizItem[] = quiz.questoes.map((questao) => ({
        questaoId: questao.id,
        alternativaId: respostas[questao.id],
      }));

      const resultado = await quizService.finalizarTentativa(tentativaId, payload);
      await queryClient.invalidateQueries({ queryKey: ['me', 'resumo'] });
      Alert.alert(
        'Quiz concluído! 🎉',
        `Você acertou ${resultado.acertos} de ${resultado.totalQuestoes}.${
          resultado.biscoitoConcedido ? '\n+1 biscoito 🍪' : ''
        }`,
        [{ text: 'OK', onPress: () => router.back() }],
      );
    } catch (error) {
      Alert.alert(
        'Erro',
        error instanceof Error ? error.message : 'Não foi possível finalizar o quiz.',
      );
    } finally {
      setSubmitting(false);
    }
  }, [quiz, queryClient, respostas, router, tentativaId, todasRespondidas]);

  return {
    loading,
    submitting,
    quiz,
    respostas,
    selecionarAlternativa,
    finalizar,
    todasRespondidas,
    questoesRespondidas,
    totalQuestoes,
  };
}
