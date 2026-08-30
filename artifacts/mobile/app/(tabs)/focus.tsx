import React, { useState, useEffect, useRef } from 'react';
import {
  Alert,
  Image,
  Modal,
  PanResponder,
  Pressable,
  ScrollView,
  StyleSheet,
  Text,
  View,
} from 'react-native';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import { useRouter, useLocalSearchParams } from 'expo-router';
import { Feather } from '@expo/vector-icons';
import Svg, { Circle, G } from 'react-native-svg';
import { useOwlSanctuary, OwlKey } from '@/context/OwlContext';
import { YouVersionBibleReader } from '@/components/YouVersionBibleReader';

const OWL_MAP: Record<OwlKey, any> = {
  coruja1: require('@/assets/images/coruja1.png'),
  coruja2: require('@/assets/images/coruja2.png'),
  coruja3: require('@/assets/images/coruja3.png'),
};

const OWL_KEYS: OwlKey[] = ['coruja1', 'coruja2', 'coruja3'];

export default function FocusScreen() {
  const router = useRouter();
  const insets = useSafeAreaInsets();
  const params = useLocalSearchParams<{ topicId?: string; topicTitle?: string; autoStart?: string }>();
  const { addHatchedOwl, addCoins, coins } = useOwlSanctuary();

  const [targetMinutes, setTargetMinutes] = useState(25);
  const [secondsLeft, setSecondsLeft] = useState(25 * 60);
  const [hasStartedFocus, setHasStartedFocus] = useState(false);
  const [isTimerRunning, setIsTimerRunning] = useState(false);
  const [showFocusSheet, setShowFocusSheet] = useState(false);
  const [showCancelModal, setShowCancelModal] = useState(false);
  const [focusMode, setFocusMode] = useState<'livre' | 'direcionado' | null>(null);
  const [showOwlHatchedModal, setShowOwlHatchedModal] = useState(false);
  const [hatchedOwlImage, setHatchedOwlImage] = useState<any>(null);

  // Auto start timer when coming from a directed study topic
  useEffect(() => {
    if (params.autoStart === 'true' || params.topicId) {
      setFocusMode('direcionado');
      setHasStartedFocus(true);
      setIsTimerRunning(true);
    }
  }, [params.autoStart, params.topicId]);

  // SVG Ring dimensions
  const ringSize = 280;
  const strokeWidth = 16;
  const center = ringSize / 2; // 140
  const radius = (ringSize - strokeWidth) / 2; // 132
  const circumference = 2 * Math.PI * radius;

  // Format MM:SS
  const formatTime = (secs: number) => {
    const m = Math.floor(secs / 60);
    const s = secs % 60;
    return `${m.toString().padStart(2, '0')}:${s.toString().padStart(2, '0')}`;
  };

  // Timer countdown effect
  useEffect(() => {
    let interval: ReturnType<typeof setInterval> | null = null;
    if (isTimerRunning) {
      interval = setInterval(() => {
        setSecondsLeft((prev) => {
          if (prev <= 1) {
            setIsTimerRunning(false);
            setHasStartedFocus(false);

            // Navigate to Quiz for completed study topic (Estudo Livre goes to João 1)
            router.push({
              pathname: '/quiz',
              params: {
                topicId: params.topicId || (focusMode === 'direcionado' ? '1' : 'JHN_1'),
                topicTitle: params.topicTitle || (focusMode === 'direcionado' ? 'Estudo Direcionado' : 'Estudo Livre: João 1'),
              },
            });

            setFocusMode(null);

            return targetMinutes * 60;
          }
          return prev - 1;
        });
      }, 1000);
    }
    return () => {
      if (interval) clearInterval(interval);
    };
  }, [isTimerRunning, targetMinutes, params.topicId, params.topicTitle, focusMode, router]);

  // Touch handler to convert angle around center to 1-60 minutes
  const updateMinutesFromTouch = (locationX: number, locationY: number) => {
    if (hasStartedFocus) return;

    const dx = locationX - center;
    const dy = locationY - center;

    // Angle in degrees from top (12 o'clock), clockwise
    let angleRad = Math.atan2(dy, dx);
    let angleDeg = (angleRad * 180) / Math.PI + 90;
    if (angleDeg < 0) angleDeg += 360;

    let mins = Math.round((angleDeg / 360) * 60);
    if (mins < 1) mins = 1;
    if (mins > 60) mins = 60;

    setTargetMinutes(mins);
    setSecondsLeft(mins * 60);
  };

  const panResponder = useRef(
    PanResponder.create({
      onStartShouldSetPanResponder: () => !hasStartedFocus,
      onMoveShouldSetPanResponder: () => !hasStartedFocus,
      onPanResponderGrant: (evt) => {
        const { locationX, locationY } = evt.nativeEvent;
        updateMinutesFromTouch(locationX, locationY);
      },
      onPanResponderMove: (evt) => {
        const { locationX, locationY } = evt.nativeEvent;
        updateMinutesFromTouch(locationX, locationY);
      },
    })
  ).current;

  // Calculate ring progress ratio
  const totalSeconds = targetMinutes * 60;
  const currentRatio = targetMinutes / 60;

  // Calculate stroke dashoffset for filled arc (during setup)
  const strokeDashoffset = circumference * (1 - currentRatio);

  // Knob coordinate on circle circumference
  const knobAngleRad = (currentRatio * 360 - 90) * (Math.PI / 180);
  const knobX = center + radius * Math.cos(knobAngleRad);
  const knobY = center + radius * Math.sin(knobAngleRad);

  const handleStartFocusPress = () => {
    setShowFocusSheet(true);
  };

  const handleSelectFocusMode = (mode: 'livre' | 'direcionado') => {
    setShowFocusSheet(false);
    if (mode === 'direcionado') {
      router.push('/study');
    } else {
      setFocusMode(mode);
      setHasStartedFocus(true);
      setIsTimerRunning(true);
    }
  };

  const handlePauseResume = () => {
    setIsTimerRunning((prev) => !prev);
  };

  const handleCancelPress = () => {
    setShowCancelModal(true);
  };

  const executeCancelFocus = () => {
    setIsTimerRunning(false);
    setHasStartedFocus(false);
    setFocusMode(null);
    setSecondsLeft(targetMinutes * 60);
    setShowCancelModal(false);
  };

  return (
    <View style={[styles.container, { paddingTop: insets.top + 12 }]}>
      {/* Top Header Bar */}
      <View style={styles.topBar}>
        <Pressable style={({ pressed }) => [{ opacity: pressed ? 0.7 : 1 }]}>
          <Feather name="menu" size={26} color="#FFFFFF" />
        </Pressable>

        <View style={styles.coinBadge}>
          <Text style={styles.coinIcon}>🪙</Text>
          <Text style={styles.coinText}>{coins}</Text>
        </View>
      </View>

      {/* Main Content Container */}
      <View style={styles.contentContainer}>
        {/* Title */}
        <Text style={styles.headerTitle}>
          {hasStartedFocus
            ? `${params.topicTitle ? params.topicTitle : focusMode === 'direcionado' ? 'Estudo Direcionado' : 'Estudo Livre'}\n${isTimerRunning ? 'Foco em andamento ⏳' : 'Sessão Pausada ⏸️'}`
            : 'Comece seu estudo\ne choque sua coruja!'}
        </Text>

        {/* Central Visual Container */}
        <View
          style={styles.ringWrapper}
          {...(hasStartedFocus ? {} : panResponder.panHandlers)}
        >
          {/* Inner Cream Background Circle */}
          <View style={styles.innerCircle}>
            <Image
              source={require('@/assets/images/egg_nest.png')}
              style={styles.eggImage}
              resizeMode="contain"
            />
          </View>

          {/* Render Slider Ring only before focus starts */}
          {!hasStartedFocus && (
            <Svg width={ringSize} height={ringSize} style={styles.svgOverlay}>
              <G transform={`rotate(-90 ${center} ${center})`}>
                {/* Background Track Circle */}
                <Circle
                  cx={center}
                  cy={center}
                  r={radius}
                  stroke="#D8A985"
                  strokeWidth={strokeWidth}
                  fill="none"
                />

                {/* Active Filled Arc Circle */}
                <Circle
                  cx={center}
                  cy={center}
                  r={radius}
                  stroke="#FF6E00"
                  strokeWidth={strokeWidth}
                  strokeDasharray={circumference}
                  strokeDashoffset={strokeDashoffset}
                  strokeLinecap="round"
                  fill="none"
                />
              </G>

              {/* Slider Knob Handle Circle */}
              <Circle
                cx={knobX}
                cy={knobY}
                r={12}
                fill="#FF6E00"
              />
            </Svg>
          )}
        </View>

        {/* Timer Text Section */}
        <View style={styles.timerContainer}>
          <Text style={styles.timerLabel}>Timer</Text>
          <Text style={styles.timerValue}>{formatTime(secondsLeft)}</Text>
        </View>

        {/* Control Buttons */}
        {!hasStartedFocus ? (
          /* Start Focus Button */
          <Pressable
            style={({ pressed }) => [
              styles.focusButton,
              {
                opacity: pressed ? 0.9 : 1,
                transform: [{ scale: pressed ? 0.97 : 1 }],
              },
            ]}
            onPress={handleStartFocusPress}
          >
            <Text style={styles.focusButtonText}>Focar</Text>
          </Pressable>
        ) : (
          /* Active Focus Buttons: Pausar & Cancelar */
          <View style={styles.activeControlsRow}>
            <Pressable
              style={({ pressed }) => [
                styles.pauseButton,
                {
                  opacity: pressed ? 0.85 : 1,
                  transform: [{ scale: pressed ? 0.97 : 1 }],
                },
              ]}
              onPress={handlePauseResume}
            >
              <Feather
                name={isTimerRunning ? 'pause' : 'play'}
                size={20}
                color="#FFFFFF"
              />
              <Text style={styles.controlButtonText}>
                {isTimerRunning ? 'Pausar' : 'Retomar'}
              </Text>
            </Pressable>

            <Pressable
              style={({ pressed }) => [
                styles.cancelButton,
                {
                  opacity: pressed ? 0.85 : 1,
                  transform: [{ scale: pressed ? 0.97 : 1 }],
                },
              ]}
              onPress={handleCancelPress}
            >
              <Feather name="x-circle" size={20} color="#FFFFFF" />
              <Text style={styles.cancelButtonText}>Cancelar</Text>
            </Pressable>
          </View>
        )}

        {/* YouVersion Bible Reader during Estudo Livre */}
        {hasStartedFocus && focusMode === 'livre' && (
          <YouVersionBibleReader />
        )}
      </View>

      {/* Bottom Sheet Focus Mode Choice Modal */}
      <Modal
        visible={showFocusSheet}
        transparent
        animationType="slide"
        onRequestClose={() => setShowFocusSheet(false)}
      >
        <Pressable
          style={styles.modalOverlay}
          onPress={() => setShowFocusSheet(false)}
        >
          <View style={styles.bottomSheetContainer}>
            {/* Drag Handle */}
            <View style={styles.dragHandle} />

            <Text style={styles.sheetTitle}>Selecione o modo de foco</Text>

            {/* Option 1: Estudo Livre */}
            <Pressable
              style={({ pressed }) => [
                styles.sheetOptionPill,
                { opacity: pressed ? 0.85 : 1 },
              ]}
              onPress={() => handleSelectFocusMode('livre')}
            >
              <Text style={styles.sheetOptionPillText}>Estudo Livre</Text>
            </Pressable>

            {/* Option 2: Estudo Direcionado */}
            <Pressable
              style={({ pressed }) => [
                styles.sheetOptionTextRow,
                { opacity: pressed ? 0.7 : 1 },
              ]}
              onPress={() => handleSelectFocusMode('direcionado')}
            >
              <Text style={styles.sheetOptionText}>Estudo Direcionado</Text>
            </Pressable>
          </View>
        </Pressable>
      </Modal>

      {/* Cancel Confirmation Modal */}
      <Modal
        visible={showCancelModal}
        transparent
        animationType="fade"
        onRequestClose={() => setShowCancelModal(false)}
      >
        <Pressable
          style={styles.modalOverlayCenter}
          onPress={() => setShowCancelModal(false)}
        >
          <Pressable style={styles.confirmCard} onPress={(e) => e.stopPropagation()}>
            <Feather name="alert-triangle" size={36} color="#ED5B0A" style={{ marginBottom: 12 }} />
            <Text style={styles.confirmTitle}>Desistir desse estudo?</Text>
            <Text style={styles.confirmMessage}>
              Seu ovo ficará choco e você perderá ele!
            </Text>

            <View style={styles.confirmButtonsRow}>
              <Pressable
                style={({ pressed }) => [
                  styles.keepButton,
                  { opacity: pressed ? 0.85 : 1 },
                ]}
                onPress={() => setShowCancelModal(false)}
              >
                <Text style={styles.keepButtonText}>Continuar</Text>
              </Pressable>

              <Pressable
                style={({ pressed }) => [
                  styles.confirmCancelBtn,
                  { opacity: pressed ? 0.85 : 1 },
                ]}
                onPress={executeCancelFocus}
              >
                <Text style={styles.confirmCancelBtnText}>Sim, Cancelar</Text>
              </Pressable>
            </View>
          </Pressable>
        </Pressable>
      </Modal>

      {/* Owl Hatched Pop-up Modal */}
      <Modal
        visible={showOwlHatchedModal}
        transparent
        animationType="fade"
        onRequestClose={() => setShowOwlHatchedModal(false)}
      >
        <Pressable
          style={styles.modalOverlayCenter}
          onPress={() => setShowOwlHatchedModal(false)}
        >
          <Pressable style={styles.owlHatchedCard} onPress={(e) => e.stopPropagation()}>
            {/* Sparkle Header Badge */}
            <View style={styles.celebrationBadge}>
              <Text style={styles.celebrationBadgeText}>✨ FOCO CONCLUÍDO! ✨</Text>
            </View>

            {/* Owl Image */}
            <View style={styles.owlImageWrapper}>
              {hatchedOwlImage && (
                <Image
                  source={hatchedOwlImage}
                  style={styles.hatchedOwlImage}
                  resizeMode="contain"
                />
              )}
            </View>

            {/* Title */}
            <Text style={styles.owlHatchedTitle}>Parabéns, sua coruja nasceu!</Text>
            <Text style={styles.owlHatchedSubtitle}>
              Você concluiu sua sessão de estudo com sucesso!
            </Text>

            {/* +5 Coins Reward Badge */}
            <View style={styles.rewardBadge}>
              <Text style={styles.rewardCoinEmoji}>🪙</Text>
              <Text style={styles.rewardText}>+5 moedas</Text>
            </View>

            {/* Collect Button */}
            <Pressable
              style={({ pressed }) => [
                styles.collectButton,
                { opacity: pressed ? 0.9 : 1, transform: [{ scale: pressed ? 0.98 : 1 }] },
              ]}
              onPress={() => setShowOwlHatchedModal(false)}
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
    backgroundColor: '#FA9948',
  },
  topBar: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    paddingHorizontal: 20,
    height: 48,
  },
  coinBadge: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: 'rgba(0, 0, 0, 0.12)',
    paddingHorizontal: 12,
    paddingVertical: 5,
    borderRadius: 18,
    gap: 6,
  },
  coinIcon: {
    fontSize: 16,
  },
  coinText: {
    color: '#FFFFFF',
    fontSize: 15,
    fontWeight: '700',
  },
  contentContainer: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    paddingBottom: 60,
  },
  headerTitle: {
    fontSize: 22,
    fontWeight: '600',
    color: '#FFFFFF',
    textAlign: 'center',
    lineHeight: 28,
    marginBottom: 32,
  },
  ringWrapper: {
    width: 280,
    height: 280,
    alignItems: 'center',
    justifyContent: 'center',
    position: 'relative',
    marginBottom: 28,
  },
  innerCircle: {
    width: 224,
    height: 224,
    borderRadius: 112,
    backgroundColor: '#FFF5E4',
    alignItems: 'center',
    justifyContent: 'center',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.08,
    shadowRadius: 8,
    elevation: 3,
  },
  eggImage: {
    width: 170,
    height: 170,
  },
  svgOverlay: {
    position: 'absolute',
    top: 0,
    left: 0,
  },
  timerContainer: {
    alignItems: 'center',
    marginBottom: 28,
  },
  timerLabel: {
    fontSize: 17,
    fontWeight: '400',
    color: 'rgba(255, 255, 255, 0.8)',
    marginBottom: 2,
  },
  timerValue: {
    fontSize: 64,
    fontWeight: '300',
    color: '#FFFFFF',
    letterSpacing: 2,
  },
  focusButton: {
    backgroundColor: '#FF6E00',
    paddingHorizontal: 54,
    paddingVertical: 14,
    borderRadius: 16,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.18,
    shadowRadius: 6,
    elevation: 4,
  },
  focusButtonText: {
    color: '#FFFFFF',
    fontSize: 19,
    fontWeight: '700',
  },
  activeControlsRow: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 16,
    zIndex: 10,
  },
  pauseButton: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: '#FF6E00',
    paddingHorizontal: 28,
    paddingVertical: 14,
    borderRadius: 16,
    gap: 8,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.18,
    shadowRadius: 6,
    elevation: 4,
  },
  controlButtonText: {
    color: '#FFFFFF',
    fontSize: 17,
    fontWeight: '700',
  },
  cancelButton: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: 'rgba(0, 0, 0, 0.22)',
    paddingHorizontal: 24,
    paddingVertical: 14,
    borderRadius: 16,
    gap: 8,
    borderWidth: 1,
    borderColor: 'rgba(255, 255, 255, 0.3)',
  },
  cancelButtonText: {
    color: '#FFFFFF',
    fontSize: 17,
    fontWeight: '600',
  },

  /* Bottom Sheet Modal Styles */
  modalOverlay: {
    flex: 1,
    backgroundColor: 'rgba(0, 0, 0, 0.4)',
    justifyContent: 'flex-end',
  },
  bottomSheetContainer: {
    backgroundColor: '#FFFFFF',
    borderTopLeftRadius: 28,
    borderTopRightRadius: 28,
    paddingTop: 12,
    paddingBottom: 44,
    paddingHorizontal: 24,
    alignItems: 'center',
  },
  dragHandle: {
    width: 44,
    height: 5,
    borderRadius: 2.5,
    backgroundColor: '#D1D1D6',
    marginBottom: 16,
  },
  sheetTitle: {
    fontSize: 16,
    fontWeight: '700',
    color: '#1E1B38',
    marginBottom: 20,
  },
  sheetOptionPill: {
    width: '100%',
    height: 54,
    backgroundColor: '#F3F3F3',
    borderRadius: 16,
    alignItems: 'center',
    justifyContent: 'center',
    marginBottom: 14,
  },
  sheetOptionPillText: {
    fontSize: 17,
    fontWeight: '700',
    color: '#4A4A4A',
  },
  sheetOptionTextRow: {
    width: '100%',
    height: 48,
    alignItems: 'center',
    justifyContent: 'center',
  },
  sheetOptionText: {
    fontSize: 17,
    fontWeight: '600',
    color: '#8C7C6D',
  },

  /* Confirm Modal Styles */
  modalOverlayCenter: {
    flex: 1,
    backgroundColor: 'rgba(0, 0, 0, 0.5)',
    alignItems: 'center',
    justifyContent: 'center',
    paddingHorizontal: 24,
  },
  confirmCard: {
    width: '100%',
    maxWidth: 320,
    backgroundColor: '#FFFFFF',
    borderRadius: 24,
    padding: 24,
    alignItems: 'center',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 8 },
    shadowOpacity: 0.15,
    shadowRadius: 16,
    elevation: 8,
  },
  confirmTitle: {
    fontSize: 20,
    fontWeight: '700',
    color: '#1E1B38',
    marginBottom: 8,
  },
  confirmMessage: {
    fontSize: 15,
    color: '#666666',
    textAlign: 'center',
    marginBottom: 24,
    lineHeight: 20,
  },
  confirmButtonsRow: {
    flexDirection: 'row',
    gap: 12,
    width: '100%',
  },
  keepButton: {
    flex: 1,
    height: 46,
    borderRadius: 12,
    backgroundColor: '#F3F3F3',
    alignItems: 'center',
    justifyContent: 'center',
  },
  keepButtonText: {
    fontSize: 15,
    fontWeight: '600',
    color: '#666666',
  },
  confirmCancelBtn: {
    flex: 1,
    height: 46,
    borderRadius: 12,
    backgroundColor: '#ED5B0A',
    alignItems: 'center',
    justifyContent: 'center',
  },
  confirmCancelBtnText: {
    fontSize: 15,
    fontWeight: '700',
    color: '#FFFFFF',
  },

  /* Owl Hatched Pop-up Modal Styles */
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
