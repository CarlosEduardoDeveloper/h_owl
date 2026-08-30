import React from 'react';
import {
  ActivityIndicator,
  Pressable,
  ScrollView,
  StyleSheet,
  Text,
  View,
} from 'react-native';
import { useLocalSearchParams, useRouter } from 'expo-router';
import { Feather } from '@expo/vector-icons';
import { useSafeAreaInsets } from 'react-native-safe-area-context';

import { useQuizPlayer } from '@/features/quiz/useQuizPlayer';

export default function QuizPlayerScreen() {
  const router = useRouter();
  const insets = useSafeAreaInsets();
  const { id } = useLocalSearchParams<{ id: string }>();

  const {
    loading,
    submitting,
    quiz,
    respostas,
    selecionarAlternativa,
    finalizar,
    todasRespondidas,
    questoesRespondidas,
    totalQuestoes,
  } = useQuizPlayer(typeof id === 'string' ? id : undefined);

  return (
    <View style={[styles.container, { paddingTop: insets.top + 12, paddingBottom: insets.bottom + 16 }]}>
      <View style={styles.header}>
        <Pressable onPress={() => router.back()} style={styles.backButton}>
          <Feather name="chevron-left" size={24} color="#1E1B38" />
        </Pressable>
        <Text style={styles.title}>{quiz?.titulo ?? 'Quiz'}</Text>
        <View style={styles.backButton} />
      </View>

      {loading || !quiz ? (
        <View style={styles.center}>
          <ActivityIndicator size="large" color="#FF6E00" />
        </View>
      ) : (
        <>
          {quiz.descricao ? <Text style={styles.descricao}>{quiz.descricao}</Text> : null}
          <Text style={styles.progresso}>
            {questoesRespondidas}/{totalQuestoes} respondidas
          </Text>

          <ScrollView contentContainerStyle={styles.scroll} showsVerticalScrollIndicator={false}>
            {quiz.questoes.map((questao, index) => (
              <View key={questao.id} style={styles.questaoCard}>
                <Text style={styles.questaoNumero}>Questão {index + 1}</Text>
                <Text style={styles.enunciado}>{questao.enunciado}</Text>

                {questao.alternativas.map((alternativa) => {
                  const selecionada = respostas[questao.id] === alternativa.id;
                  return (
                    <Pressable
                      key={alternativa.id}
                      style={({ pressed }) => [
                        styles.alternativa,
                        selecionada && styles.alternativaSelecionada,
                        { opacity: pressed ? 0.85 : 1 },
                      ]}
                      onPress={() => selecionarAlternativa(questao.id, alternativa.id)}
                    >
                      <Text
                        style={[
                          styles.alternativaTexto,
                          selecionada && styles.alternativaTextoSelecionada,
                        ]}
                      >
                        {alternativa.texto}
                      </Text>
                    </Pressable>
                  );
                })}
              </View>
            ))}
          </ScrollView>

          <Pressable
            style={({ pressed }) => [
              styles.finalizarButton,
              (!todasRespondidas || submitting) && styles.finalizarButtonDisabled,
              { opacity: pressed ? 0.9 : 1 },
            ]}
            disabled={!todasRespondidas || submitting}
            onPress={() => void finalizar()}
          >
            <Text style={styles.finalizarTexto}>
              {submitting ? 'Enviando...' : 'Finalizar quiz'}
            </Text>
          </Pressable>
        </>
      )}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#FFF6E5',
  },
  header: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    paddingHorizontal: 16,
    marginBottom: 8,
  },
  backButton: {
    width: 40,
    height: 40,
    alignItems: 'center',
    justifyContent: 'center',
  },
  title: {
    fontSize: 18,
    fontWeight: '800',
    color: '#1E1B38',
    flex: 1,
    textAlign: 'center',
  },
  center: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  descricao: {
    paddingHorizontal: 20,
    color: '#8C7C6D',
    marginBottom: 8,
    textAlign: 'center',
  },
  progresso: {
    textAlign: 'center',
    color: '#FF6E00',
    fontWeight: '700',
    marginBottom: 12,
  },
  scroll: {
    paddingHorizontal: 20,
    paddingBottom: 20,
    gap: 16,
  },
  questaoCard: {
    backgroundColor: '#FFFFFF',
    borderRadius: 16,
    padding: 16,
    borderWidth: 1,
    borderColor: '#F0E4D6',
  },
  questaoNumero: {
    color: '#FF6E00',
    fontWeight: '700',
    marginBottom: 8,
    fontSize: 13,
  },
  enunciado: {
    fontSize: 16,
    fontWeight: '700',
    color: '#1E1B38',
    marginBottom: 12,
    lineHeight: 22,
  },
  alternativa: {
    borderWidth: 1,
    borderColor: '#E8DDD1',
    borderRadius: 12,
    paddingVertical: 12,
    paddingHorizontal: 14,
    marginBottom: 8,
    backgroundColor: '#FFFCF7',
  },
  alternativaSelecionada: {
    borderColor: '#FF6E00',
    backgroundColor: '#FFF0E6',
  },
  alternativaTexto: {
    color: '#1E1B38',
    fontSize: 15,
  },
  alternativaTextoSelecionada: {
    fontWeight: '700',
    color: '#FF6E00',
  },
  finalizarButton: {
    marginHorizontal: 20,
    backgroundColor: '#FF6E00',
    borderRadius: 999,
    paddingVertical: 16,
    alignItems: 'center',
  },
  finalizarButtonDisabled: {
    backgroundColor: '#D8A985',
  },
  finalizarTexto: {
    color: '#FFFFFF',
    fontWeight: '800',
    fontSize: 16,
  },
});
