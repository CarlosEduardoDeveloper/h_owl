import React, { useState, useEffect } from 'react';
import {
  Image,
  Modal,
  Pressable,
  ScrollView,
  StyleSheet,
  Text,
  View,
} from 'react-native';
import { useLocalSearchParams, useRouter } from 'expo-router';
import { Feather } from '@expo/vector-icons';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import { getQuizByTopicId, QuizQuestion } from '@/data/quizData';
import { useOwlSanctuary, OwlKey } from '@/context/OwlContext';

const OWL_MAP: Record<OwlKey, any> = {
  coruja1: require('@/assets/images/coruja1.png'),
  coruja2: require('@/assets/images/coruja2.png'),
  coruja3: require('@/assets/images/coruja3.png'),
};

const OWL_KEYS: OwlKey[] = ['coruja1', 'coruja2', 'coruja3'];

export default function QuizScreen() {
  const router = useRouter();
  const insets = useSafeAreaInsets();
  const params = useLocalSearchParams<{ topicId?: string; topicTitle?: string }>();
  const { addHatchedOwl, addCoins } = useOwlSanctuary();

  const quizSet = getQuizByTopicId(params.topicId);
  const questions = quizSet.questions;

  const [currentIndex, setCurrentIndex] = useState(0);
  const [selectedOption, setSelectedOption] = useState<'A' | 'B' | 'C' | 'D' | null>(null);
  const [isSubmitted, setIsSubmitted] = useState(false);
  const [secondsLeft, setSecondsLeft] = useState(300); // 5 min timer

  const [showOwlHatchedModal, setShowOwlHatchedModal] = useState(false);
  const [hatchedOwlImage, setHatchedOwlImage] = useState<any>(null);

  const currentQuestion: QuizQuestion = questions[currentIndex] || questions[0];
  const totalQuestions = questions.length;

  // Countdown timer effect
  useEffect(() => {
    const timer = setInterval(() => {
      setSecondsLeft((prev) => (prev > 0 ? prev - 1 : 0));
    }, 1000);
    return () => clearInterval(timer);
  }, []);

  const formatTimer = (secs: number) => {
    const m = Math.floor(secs / 60);
    const s = secs % 60;
    return `${m.toString().padStart(2, '0')}:${s.toString().padStart(2, '0')}`;
  };

  const handleSelectOption = (key: 'A' | 'B' | 'C' | 'D') => {
    if (isSubmitted) return;
    setSelectedOption(key);
  };

  const handleSubmitOrNext = () => {
    if (!selectedOption) return;

    if (!isSubmitted) {
      setIsSubmitted(true);
    } else {
      // Move to next question or complete quiz
      if (currentIndex < totalQuestions - 1) {
        setCurrentIndex((prev) => prev + 1);
        setSelectedOption(null);
        setIsSubmitted(false);
      } else {
        // Complete Quiz -> Trigger Owl Hatching Reward
        const randomKey = OWL_KEYS[Math.floor(Math.random() * OWL_KEYS.length)];
        addHatchedOwl(randomKey);
        addCoins(5);

        setHatchedOwlImage(OWL_MAP[randomKey]);
        setShowOwlHatchedModal(true);
      }
    }
  };

  const handleFinishAndReturn = () => {
    setShowOwlHatchedModal(false);
    router.replace('/(tabs)');
  };

  return (
    <View style={styles.container}>
      {/* Top Header Section */}
      <View style={[styles.headerContainer, { paddingTop: insets.top + 12 }]}>
        {/* Left: Back Arrow + Owl Circle */}
        <View style={styles.headerLeftGroup}>
          <Pressable
            style={({ pressed }) => [
              styles.backButton,
              { opacity: pressed ? 0.7 : 1 },
            ]}
            onPress={() => router.back()}
          >
            <Feather name="chevron-left" size={24} color="#1E1B38" />
          </Pressable>

          <View style={styles.owlAvatarCircle}>
            <Text style={styles.owlAvatarEmoji}>🦉</Text>
          </View>
        </View>

        {/* Center: Progress Bar & Step Text */}
        <View style={styles.headerCenterGroup}>
          <View style={styles.progressTrack}>
            <View
              style={[
                styles.progressFill,
                { width: `${((currentIndex + 1) / totalQuestions) * 100}%` },
              ]}
            />
          </View>
          <Text style={styles.progressStepText}>
            {currentIndex + 1} de {totalQuestions}
          </Text>
        </View>

        {/* Right: Timer Badge & Label */}
        <View style={styles.headerRightGroup}>
          <View style={styles.timerBadge}>
            <Text style={styles.timerBadgeText}>{formatTimer(secondsLeft)}</Text>
          </View>
          <Text style={styles.timerLabel}>TEMPO</Text>
        </View>
      </View>

      {/* Main Content Scroll Area */}
      <ScrollView
        contentContainerStyle={styles.scrollContent}
        showsVerticalScrollIndicator={false}
      >
        {/* Chapter Summary Card if present */}
        {quizSet.summaryText && (
          <View style={styles.summaryCard}>
            <View style={styles.summaryBadgeRow}>
              <Feather name="book-open" size={16} color="#ED5B0A" />
              <Text style={styles.summaryBadgeTitle}>RESUMO DO ESTUDO — JOÃO 1</Text>
            </View>
            <Text style={styles.summaryTextBody}>{quizSet.summaryText}</Text>
          </View>
        )}

        {/* Question Card */}
        <View style={styles.questionCard}>
          <Text style={styles.categoryTitle}>{currentQuestion.category}</Text>
          <Text style={styles.questionText}>{currentQuestion.question}</Text>
        </View>

        {/* Multiple Choice Options */}
        <View style={styles.optionsList}>
          {currentQuestion.options.map((opt) => {
            const isSelected = selectedOption === opt.key;
            const isCorrect = isSubmitted && opt.key === currentQuestion.correctOption;
            const isWrong = isSubmitted && isSelected && !isCorrect;

            let cardStyle = styles.optionCardDefault;
            let badgeStyle = styles.badgeDefault;
            let badgeTextStyle = styles.badgeTextDefault;

            if (isSubmitted) {
              if (isCorrect) {
                cardStyle = styles.optionCardCorrect;
                badgeStyle = styles.badgeCorrect;
                badgeTextStyle = styles.badgeTextWhite;
              } else if (isWrong) {
                cardStyle = styles.optionCardWrong;
                badgeStyle = styles.badgeWrong;
                badgeTextStyle = styles.badgeTextWhite;
              }
            } else if (isSelected) {
              cardStyle = styles.optionCardSelected;
              badgeStyle = styles.badgeSelected;
              badgeTextStyle = styles.badgeTextWhite;
            }

            return (
              <Pressable
                key={opt.key}
                style={({ pressed }) => [
                  styles.optionCard,
                  cardStyle,
                  { opacity: pressed ? 0.9 : 1, transform: [{ scale: pressed ? 0.99 : 1 }] },
                ]}
                onPress={() => handleSelectOption(opt.key)}
              >
                {/* Option Badge A, B, C */}
                <View style={[styles.badgeBase, badgeStyle]}>
                  <Text style={[styles.badgeTextBase, badgeTextStyle]}>
                    {opt.key}
                  </Text>
                </View>

                {/* Option Text */}
                <Text style={styles.optionText}>{opt.text}</Text>
              </Pressable>
            );
          })}
        </View>
      </ScrollView>

      {/* Fixed Bottom Action Bar */}
      <View style={[styles.bottomBar, { paddingBottom: Math.max(insets.bottom, 16) }]}>
        <Pressable
          style={({ pressed }) => [
            styles.submitButton,
            { opacity: !selectedOption ? 0.5 : pressed ? 0.9 : 1 },
          ]}
          disabled={!selectedOption}
          onPress={handleSubmitOrNext}
        >
          <Text style={styles.submitButtonText}>
            {!isSubmitted
              ? 'Enviar Resposta'
              : currentIndex < totalQuestions - 1
              ? 'Próxima Pergunta'
              : 'Concluir Quiz 🎉'}
          </Text>
        </Pressable>
      </View>

      {/* Owl Hatched Pop-up Modal */}
      <Modal
        visible={showOwlHatchedModal}
        transparent
        animationType="fade"
        onRequestClose={handleFinishAndReturn}
      >
        <Pressable style={styles.modalOverlayCenter} onPress={handleFinishAndReturn}>
          <Pressable style={styles.owlHatchedCard} onPress={(e) => e.stopPropagation()}>
            <View style={styles.celebrationBadge}>
              <Text style={styles.celebrationBadgeText}>✨ QUIZ CONCLUÍDO! ✨</Text>
            </View>

            <View style={styles.owlImageWrapper}>
              {hatchedOwlImage && (
                <Image
                  source={hatchedOwlImage}
                  style={styles.hatchedOwlImage}
                  resizeMode="contain"
                />
              )}
            </View>

            <Text style={styles.owlHatchedTitle}>Parabéns, sua coruja nasceu!</Text>
            <Text style={styles.owlHatchedSubtitle}>
              Você concluiu o quiz de estudo com sucesso!
            </Text>

            <View style={styles.rewardBadge}>
              <Text style={styles.rewardCoinEmoji}>🪙</Text>
              <Text style={styles.rewardText}>+5 moedas</Text>
            </View>

            <Pressable
              style={({ pressed }) => [
                styles.collectButton,
                { opacity: pressed ? 0.9 : 1, transform: [{ scale: pressed ? 0.98 : 1 }] },
              ]}
              onPress={handleFinishAndReturn}
            >
              <Text style={styles.collectButtonText}>Incrível! 🎉</Text>
            </Pressable>
          </Pressable>
        </Pressable>
      </Modal>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#FFF6E5',
  },
  headerContainer: {
    backgroundColor: '#FFFFFF',
    paddingHorizontal: 20,
    paddingBottom: 16,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.04,
    shadowRadius: 8,
    elevation: 3,
  },
  headerLeftGroup: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 10,
  },
  backButton: {
    width: 42,
    height: 42,
    borderRadius: 21,
    backgroundColor: '#F5ECE0',
    alignItems: 'center',
    justifyContent: 'center',
  },
  owlAvatarCircle: {
    width: 42,
    height: 42,
    borderRadius: 21,
    backgroundColor: '#0F7B6C',
    alignItems: 'center',
    justifyContent: 'center',
  },
  owlAvatarEmoji: {
    fontSize: 20,
  },
  headerCenterGroup: {
    alignItems: 'center',
    flex: 1,
    marginHorizontal: 12,
  },
  progressTrack: {
    width: '100%',
    maxWidth: 140,
    height: 10,
    backgroundColor: '#F3EDE2',
    borderRadius: 5,
    overflow: 'hidden',
    marginBottom: 6,
  },
  progressFill: {
    height: '100%',
    backgroundColor: '#0F7B6C',
    borderRadius: 5,
  },
  progressStepText: {
    fontSize: 12,
    fontWeight: '600',
    color: '#766E65',
  },
  headerRightGroup: {
    alignItems: 'center',
  },
  timerBadge: {
    backgroundColor: '#F4EDE2',
    paddingHorizontal: 12,
    paddingVertical: 6,
    borderRadius: 16,
  },
  timerBadgeText: {
    fontSize: 14,
    fontWeight: '800',
    color: '#0F7B6C',
  },
  timerLabel: {
    fontSize: 10,
    fontWeight: '800',
    letterSpacing: 1,
    color: '#766E65',
    marginTop: 2,
  },
  scrollContent: {
    paddingHorizontal: 20,
    paddingTop: 20,
    paddingBottom: 40,
  },
  summaryCard: {
    backgroundColor: '#FFFFFF',
    borderRadius: 22,
    padding: 20,
    marginBottom: 16,
    borderWidth: 1.5,
    borderColor: '#ED5B0A',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 3 },
    shadowOpacity: 0.04,
    shadowRadius: 8,
    elevation: 2,
  },
  summaryBadgeRow: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 8,
    marginBottom: 10,
  },
  summaryBadgeTitle: {
    fontSize: 12,
    fontWeight: '900',
    color: '#ED5B0A',
    letterSpacing: 0.6,
  },
  summaryTextBody: {
    fontSize: 14,
    lineHeight: 22,
    fontWeight: '500',
    color: '#2C2A4A',
  },
  questionCard: {
    backgroundColor: '#FFFFFF',
    borderRadius: 22,
    padding: 22,
    marginBottom: 16,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 3 },
    shadowOpacity: 0.04,
    shadowRadius: 8,
    elevation: 2,
  },
  categoryTitle: {
    fontSize: 12,
    fontWeight: '800',
    letterSpacing: 0.8,
    color: '#8C7C6D',
    marginBottom: 12,
  },
  questionText: {
    fontSize: 17,
    fontWeight: '700',
    color: '#1E1B38',
    lineHeight: 25,
  },
  optionsList: {
    gap: 12,
  },
  optionCard: {
    borderRadius: 20,
    paddingVertical: 16,
    paddingHorizontal: 16,
    flexDirection: 'row',
    alignItems: 'center',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.03,
    shadowRadius: 6,
    elevation: 1,
  },
  optionCardDefault: {
    backgroundColor: '#FFFFFF',
    borderWidth: 2,
    borderColor: 'transparent',
  },
  optionCardSelected: {
    backgroundColor: '#FFF8F3',
    borderWidth: 2,
    borderColor: '#ED5B0A',
  },
  optionCardCorrect: {
    backgroundColor: '#ECFDF5',
    borderWidth: 2,
    borderColor: '#10B981',
  },
  optionCardWrong: {
    backgroundColor: '#FEF2F2',
    borderWidth: 2,
    borderColor: '#EF4444',
  },
  badgeBase: {
    width: 34,
    height: 34,
    borderRadius: 17,
    alignItems: 'center',
    justifyContent: 'center',
    marginRight: 14,
  },
  badgeDefault: {
    borderWidth: 2,
    borderColor: '#1E1B38',
    backgroundColor: 'transparent',
  },
  badgeSelected: {
    borderWidth: 2,
    borderColor: '#ED5B0A',
    backgroundColor: '#ED5B0A',
  },
  badgeCorrect: {
    borderWidth: 2,
    borderColor: '#10B981',
    backgroundColor: '#10B981',
  },
  badgeWrong: {
    borderWidth: 2,
    borderColor: '#EF4444',
    backgroundColor: '#EF4444',
  },
  badgeTextBase: {
    fontSize: 15,
    fontWeight: '800',
  },
  badgeTextDefault: {
    color: '#1E1B38',
  },
  badgeTextWhite: {
    color: '#FFFFFF',
  },
  optionText: {
    flex: 1,
    fontSize: 15,
    fontWeight: '600',
    color: '#1E1B38',
    lineHeight: 21,
  },
  bottomBar: {
    backgroundColor: '#FFFFFF',
    paddingHorizontal: 20,
    paddingTop: 16,
    borderTopWidth: 1,
    borderTopColor: '#F3EDE2',
  },
  submitButton: {
    backgroundColor: '#ED5B0A',
    height: 54,
    borderRadius: 22,
    alignItems: 'center',
    justifyContent: 'center',
    shadowColor: '#ED5B0A',
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.25,
    shadowRadius: 8,
    elevation: 4,
  },
  submitButtonText: {
    color: '#FFFFFF',
    fontSize: 17,
    fontWeight: '800',
  },

  /* Modal Styles */
  modalOverlayCenter: {
    flex: 1,
    backgroundColor: 'rgba(0, 0, 0, 0.65)',
    alignItems: 'center',
    justifyContent: 'center',
    paddingHorizontal: 24,
  },
  owlHatchedCard: {
    backgroundColor: '#FFFFFF',
    borderRadius: 28,
    paddingVertical: 28,
    paddingHorizontal: 24,
    width: '100%',
    maxWidth: 340,
    alignItems: 'center',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 10 },
    shadowOpacity: 0.18,
    shadowRadius: 20,
    elevation: 10,
  },
  celebrationBadge: {
    backgroundColor: '#FEF3D7',
    borderRadius: 16,
    paddingHorizontal: 16,
    paddingVertical: 6,
    marginBottom: 16,
  },
  celebrationBadgeText: {
    fontSize: 12,
    fontWeight: '800',
    color: '#D97706',
    letterSpacing: 0.5,
  },
  owlImageWrapper: {
    width: 200,
    height: 200,
    alignItems: 'center',
    justifyContent: 'center',
    marginBottom: 16,
  },
  hatchedOwlImage: {
    width: '100%',
    height: '100%',
    borderRadius: 16,
  },
  owlHatchedTitle: {
    fontSize: 22,
    fontWeight: '900',
    color: '#1E1B38',
    textAlign: 'center',
    marginBottom: 6,
  },
  owlHatchedSubtitle: {
    fontSize: 14,
    fontWeight: '500',
    color: '#8C7C6D',
    textAlign: 'center',
    marginBottom: 18,
  },
  rewardBadge: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: '#FEF9C3',
    borderWidth: 1.5,
    borderColor: '#FDE047',
    borderRadius: 20,
    paddingHorizontal: 20,
    paddingVertical: 10,
    gap: 8,
    marginBottom: 24,
  },
  rewardCoinEmoji: {
    fontSize: 22,
  },
  rewardText: {
    fontSize: 18,
    fontWeight: '800',
    color: '#854D0E',
  },
  collectButton: {
    backgroundColor: '#ED5B0A',
    paddingVertical: 16,
    borderRadius: 24,
    width: '100%',
    alignItems: 'center',
    shadowColor: '#ED5B0A',
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.3,
    shadowRadius: 8,
    elevation: 4,
  },
  collectButtonText: {
    color: '#FFFFFF',
    fontSize: 16,
    fontWeight: '800',
  },
});
