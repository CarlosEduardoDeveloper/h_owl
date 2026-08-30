import React from 'react';
import {
  Pressable,
  ScrollView,
  StyleSheet,
  Text,
  View,
} from 'react-native';
import { useRouter } from 'expo-router';
import { Feather } from '@expo/vector-icons';
import { useSafeAreaInsets } from 'react-native-safe-area-context';

interface TopicItem {
  id: string;
  title: string;
  subtitle: string;
  subtitleColor: string;
  iconBgColor: string;
  emoji: string;
  completed: boolean;
  reference: string;
}

const TOPICS: TopicItem[] = [
  {
    id: '1',
    title: 'Jesus, nosso socorro nas crises',
    subtitle: 'O socorro de Deus nunca falha',
    subtitleColor: '#D97706',
    iconBgColor: '#FDF3D7',
    emoji: '🙏',
    completed: true,
    reference: 'MAT.14.22',
  },
  {
    id: '2',
    title: 'Amizades improváveis',
    subtitle: 'Olhar além das aparências',
    subtitleColor: '#8B5CF6',
    iconBgColor: '#EDE9FE',
    emoji: '👥',
    completed: false,
    reference: '1SAM.18.1',
  },
  {
    id: '3',
    title: 'Relacionamento em Santidade',
    subtitle: 'Limites emocionais e respeito mútuo',
    subtitleColor: '#0F7B6C',
    iconBgColor: '#CCFBF1',
    emoji: '❤️',
    completed: false,
    reference: '1THES.4.3',
  },
  {
    id: '4',
    title: 'A Coragem vem da Fé',
    subtitle: 'Afé transforma crises em oportunidades',
    subtitleColor: '#0284C7',
    iconBgColor: '#DBEAFE',
    emoji: '🛡️',
    completed: false,
    reference: 'JOS.1.9',
  },
];

export default function StudyScreen() {
  const router = useRouter();
  const insets = useSafeAreaInsets();

  const completedCount = TOPICS.filter((t) => t.completed).length;
  const totalCount = TOPICS.length;
  const progressPercent = Math.round((completedCount / totalCount) * 100);

  const handleTopicPress = (topic: TopicItem) => {
    router.push({
      pathname: '/(tabs)/focus',
      params: {
        topicId: topic.id,
        topicTitle: topic.title,
        intencao: 'TRILHA',
        referenciaUsfm: topic.reference,
        autoStart: 'true',
      },
    });
  };

  return (
    <View style={styles.container}>
      {/* Top Header Card */}
      <View style={[styles.headerContainer, { paddingTop: insets.top + 12 }]}>
        <View style={styles.headerTopRow}>
          {/* Back Button & Screen Title */}
          <View style={styles.leftHeaderGroup}>
            <Pressable
              style={({ pressed }) => [
                styles.backButton,
                { opacity: pressed ? 0.7 : 1 },
              ]}
              onPress={() => router.back()}
            >
              <Feather name="chevron-left" size={24} color="#1E1B38" />
            </Pressable>
            <Text style={styles.headerTitle}>Estudo Direcionado</Text>
          </View>

          {/* Right Progress Percentage */}
          <View style={styles.progressPercentBadge}>
            <Text style={styles.progressPercentText}>{progressPercent}%</Text>
            <Text style={styles.progressPercentSublabel}>completo</Text>
          </View>
        </View>

        {/* Horizontal Progress Bar */}
        <View style={styles.progressBarTrack}>
          <View
            style={[styles.progressBarFill, { width: `${progressPercent}%` }]}
          />
        </View>

        {/* Subtitle count */}
        <Text style={styles.topicsCountText}>
          {completedCount} de {totalCount} tópicos explorados
        </Text>
      </View>

      {/* Main Content Area */}
      <ScrollView
        contentContainerStyle={styles.scrollContent}
        showsVerticalScrollIndicator={false}
      >
        {TOPICS.map((topic) => (
          <Pressable
            key={topic.id}
            style={({ pressed }) => [
              styles.topicCard,
              { opacity: pressed ? 0.92 : 1, transform: [{ scale: pressed ? 0.98 : 1 }] },
            ]}
            onPress={() => handleTopicPress(topic)}
          >
            {/* Left Emoji Icon Circle */}
            <View style={[styles.iconCircle, { backgroundColor: topic.iconBgColor }]}>
              <Text style={styles.emojiText}>{topic.emoji}</Text>
            </View>

            {/* Middle Text Container */}
            <View style={styles.topicTextContainer}>
              <Text style={styles.topicTitle}>{topic.title}</Text>
              <Text style={[styles.topicSubtitle, { color: topic.subtitleColor }]}>
                {topic.subtitle}
              </Text>
            </View>

            {/* Right Action / Status */}
            <View style={styles.rightGroup}>
              {topic.completed && (
                <View style={styles.checkBadge}>
                  <Feather name="check" size={14} color="#059669" />
                </View>
              )}
              <Feather name="chevron-right" size={20} color="#9CA3AF" />
            </View>
          </Pressable>
        ))}
      </ScrollView>
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
    paddingBottom: 20,
    borderBottomLeftRadius: 24,
    borderBottomRightRadius: 24,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.04,
    shadowRadius: 10,
    elevation: 4,
  },
  headerTopRow: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    marginBottom: 20,
  },
  leftHeaderGroup: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 12,
    flex: 1,
  },
  backButton: {
    width: 44,
    height: 44,
    borderRadius: 22,
    backgroundColor: '#F5ECE0',
    alignItems: 'center',
    justifyContent: 'center',
  },
  headerTitle: {
    fontSize: 22,
    fontWeight: '800',
    color: '#1E1B38',
    letterSpacing: -0.3,
  },
  progressPercentBadge: {
    alignItems: 'flex-end',
  },
  progressPercentText: {
    fontSize: 24,
    fontWeight: '900',
    color: '#0F7B6C',
    lineHeight: 28,
  },
  progressPercentSublabel: {
    fontSize: 12,
    fontWeight: '500',
    color: '#766E65',
  },
  progressBarTrack: {
    height: 10,
    backgroundColor: '#F3EDE2',
    borderRadius: 5,
    overflow: 'hidden',
    marginBottom: 10,
  },
  progressBarFill: {
    height: '100%',
    backgroundColor: '#0F7B6C',
    borderRadius: 5,
  },
  topicsCountText: {
    fontSize: 13,
    fontWeight: '500',
    color: '#766E65',
  },
  scrollContent: {
    paddingHorizontal: 20,
    paddingTop: 20,
    paddingBottom: 40,
    gap: 14,
  },
  topicCard: {
    backgroundColor: '#FFFFFF',
    borderRadius: 20,
    paddingVertical: 16,
    paddingHorizontal: 16,
    flexDirection: 'row',
    alignItems: 'center',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 3 },
    shadowOpacity: 0.04,
    shadowRadius: 8,
    elevation: 2,
  },
  iconCircle: {
    width: 52,
    height: 52,
    borderRadius: 26,
    alignItems: 'center',
    justifyContent: 'center',
  },
  emojiText: {
    fontSize: 24,
  },
  topicTextContainer: {
    flex: 1,
    marginHorizontal: 14,
  },
  topicTitle: {
    fontSize: 16,
    fontWeight: '700',
    color: '#1E1B38',
    marginBottom: 3,
  },
  topicSubtitle: {
    fontSize: 13,
    fontWeight: '600',
  },
  rightGroup: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 8,
  },
  checkBadge: {
    width: 24,
    height: 24,
    borderRadius: 12,
    backgroundColor: '#D1FAE5',
    alignItems: 'center',
    justifyContent: 'center',
  },
});
