import React from 'react';
import {
  Image,
  Pressable,
  StyleSheet,
  Text,
  View,
} from 'react-native';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import { Feather } from '@expo/vector-icons';

import { useFocusSession } from '@/features/study/useFocusSession';
import { useBibleReader } from '@/features/bible/useBibleReader';

export default function FocusScreen() {
  const insets = useSafeAreaInsets();
  const { secondsLeft, isTimerRunning, toggleSession, formatTime } = useFocusSession();
  useBibleReader();

  return (
    <View style={[styles.container, { paddingTop: insets.top + 20, paddingBottom: insets.bottom + 20 }]}>
      {/* Header */}
      <View style={styles.header}>
        <Text style={styles.eyebrow}>SESSÃO DE FOCO</Text>
        <Text style={styles.title}>Estudo Livre</Text>
      </View>

      {/* Egg & Nest Focus Visual */}
      <View style={styles.visualContainer}>
        <View style={styles.eggGlowContainer}>
          <Image
            source={require('@/assets/images/egg_nest.png')}
            style={styles.eggImage}
            resizeMode="contain"
          />
        </View>

        {/* Timer Display */}
        <Text style={styles.timerText}>{formatTime(secondsLeft)}</Text>
        <Text style={styles.subtitleText}>Ovo incubando com seu tempo de estudo</Text>
      </View>

      {/* Timer Controls */}
      <View style={styles.controls}>
        <Pressable
          style={({ pressed }) => [
            styles.playButton,
            { opacity: pressed ? 0.85 : 1 },
          ]}
          onPress={() => {
            void toggleSession();
          }}
        >
          <Feather
            name={isTimerRunning ? 'pause' : 'play'}
            size={28}
            color="#FFFFFF"
          />
          <Text style={styles.playButtonText}>
            {isTimerRunning ? 'Pausar Sessão' : 'Iniciar Foco'}
          </Text>
        </Pressable>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#FFF6E5',
    paddingHorizontal: 24,
    justifyContent: 'space-between',
  },
  header: {
    alignItems: 'center',
    marginTop: 10,
  },
  eyebrow: {
    fontSize: 12,
    fontWeight: '800',
    letterSpacing: 1.5,
    color: '#8C7C6D',
    marginBottom: 6,
  },
  title: {
    fontSize: 28,
    fontWeight: '800',
    color: '#1E1B38',
  },
  visualContainer: {
    alignItems: 'center',
  },
  eggGlowContainer: {
    width: 180,
    height: 180,
    borderRadius: 90,
    backgroundColor: '#FFFFFF',
    alignItems: 'center',
    justifyContent: 'center',
    shadowColor: '#ED5B0A',
    shadowOffset: { width: 0, height: 8 },
    shadowOpacity: 0.15,
    shadowRadius: 16,
    elevation: 6,
    marginBottom: 28,
  },
  eggImage: {
    width: 150,
    height: 150,
    borderRadius: 75,
  },
  timerText: {
    fontSize: 54,
    fontWeight: '800',
    color: '#1E1B38',
    letterSpacing: 2,
    marginBottom: 8,
  },
  subtitleText: {
    fontSize: 14,
    color: '#8C7C6D',
    fontWeight: '500',
    textAlign: 'center',
  },
  controls: {
    marginBottom: 80,
  },
  playButton: {
    backgroundColor: '#ED5B0A',
    borderRadius: 28,
    height: 58,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    gap: 12,
    shadowColor: '#ED5B0A',
    shadowOffset: { width: 0, height: 6 },
    shadowOpacity: 0.25,
    shadowRadius: 10,
    elevation: 4,
  },
  playButtonText: {
    color: '#FFFFFF',
    fontSize: 17,
    fontWeight: '700',
  },
});
