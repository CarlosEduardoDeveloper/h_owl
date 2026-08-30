import React, { useEffect, useState } from 'react';
import {
  ActivityIndicator,
  Pressable,
  ScrollView,
  StyleSheet,
  Text,
  View,
} from 'react-native';
import { useRouter, type Href } from 'expo-router';
import { Feather } from '@expo/vector-icons';
import { useSafeAreaInsets } from 'react-native-safe-area-context';

import * as quizService from '@/features/quiz/quizService';
import type { QuizResumo } from '@/features/quiz/quizService';

export default function QuizListScreen() {
  const router = useRouter();
  const insets = useSafeAreaInsets();
  const [loading, setLoading] = useState(true);
  const [quizzes, setQuizzes] = useState<QuizResumo[]>([]);

  useEffect(() => {
    async function load() {
      try {
        const lista = await quizService.listarQuizzes();
        setQuizzes(lista);
      } finally {
        setLoading(false);
      }
    }

    void load();
  }, []);

  return (
    <View style={[styles.container, { paddingTop: insets.top + 12 }]}>
      <View style={styles.header}>
        <Pressable onPress={() => router.back()} style={styles.backButton}>
          <Feather name="chevron-left" size={24} color="#1E1B38" />
        </Pressable>
        <Text style={styles.title}>Quizzes</Text>
        <View style={styles.backButton} />
      </View>

      {loading ? (
        <View style={styles.center}>
          <ActivityIndicator size="large" color="#FF6E00" />
        </View>
      ) : (
        <ScrollView contentContainerStyle={styles.list}>
          {quizzes.length === 0 ? (
            <Text style={styles.empty}>Nenhum quiz disponível.</Text>
          ) : (
            quizzes.map((quiz) => (
              <Pressable
                key={quiz.id}
                style={({ pressed }) => [styles.card, { opacity: pressed ? 0.85 : 1 }]}
                onPress={() => router.push(`/quiz/${quiz.id}` as Href)}
              >
                <Text style={styles.cardTitle}>{quiz.titulo ?? 'Quiz'}</Text>
                {quiz.descricao ? <Text style={styles.cardDesc}>{quiz.descricao}</Text> : null}
              </Pressable>
            ))
          )}
        </ScrollView>
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
    marginBottom: 16,
  },
  backButton: {
    width: 40,
    height: 40,
    alignItems: 'center',
    justifyContent: 'center',
  },
  title: {
    fontSize: 20,
    fontWeight: '800',
    color: '#1E1B38',
  },
  center: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  list: {
    paddingHorizontal: 20,
    paddingBottom: 40,
    gap: 12,
  },
  empty: {
    textAlign: 'center',
    color: '#8C7C6D',
    marginTop: 40,
  },
  card: {
    backgroundColor: '#FFFFFF',
    borderRadius: 16,
    padding: 16,
    borderWidth: 1,
    borderColor: '#F0E4D6',
  },
  cardTitle: {
    fontSize: 17,
    fontWeight: '700',
    color: '#1E1B38',
    marginBottom: 6,
  },
  cardDesc: {
    fontSize: 14,
    color: '#8C7C6D',
    lineHeight: 20,
  },
});
