import React, { useState, useEffect, useRef } from 'react';
import {
  Alert,
  Image,
  Modal,
  PanResponder,
  Pressable,
  StyleSheet,
  Text,
  View,
} from 'react-native';
import { useLocalSearchParams } from 'expo-router';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import { Feather } from '@expo/vector-icons';
import Svg, { Circle, G } from 'react-native-svg';

import { useHome } from '@/features/home/useHome';
import {
  resolveIntencao,
  snapDuracao,
  useFocusStudyApi,
  type DuracaoPermitida,
} from '@/features/study/useFocusStudyApi';

export default function FocusScreen() {
  const insets = useSafeAreaInsets();
  const params = useLocalSearchParams<{ intencao?: string | string[] }>();
  const { data: resumo } = useHome();
  const { iniciarSessao, concluirSessao, interromperSessao } = useFocusStudyApi();

  const [targetMinutes, setTargetMinutes] = useState<DuracaoPermitida>(15);
  const [secondsLeft, setSecondsLeft] = useState(15 * 60);
  const [hasStartedFocus, setHasStartedFocus] = useState(false);
  const [isTimerRunning, setIsTimerRunning] = useState(false);
  const [showFocusSheet, setShowFocusSheet] = useState(false);
  const [showCancelModal, setShowCancelModal] = useState(false);
  const [focusMode, setFocusMode] = useState<'livre' | 'direcionado' | null>(null);

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
            setFocusMode(null);
            void (async () => {
              try {
                const resultado = await concluirSessao(targetMinutes);
                const nome = resultado?.gamificacao?.corujaNome ?? 'Coruja';
                const biscoito = resultado?.gamificacao?.biscoitoConcedido ? ' +1 biscoito 🍪' : '';
                Alert.alert('Parabéns! 🎉', `${nome} chocou com sucesso!${biscoito}`);
              } catch {
                Alert.alert('Erro', 'Tempo concluído, mas falhou ao registrar no servidor.');
              }
            })();
            return targetMinutes * 60;
          }
          return prev - 1;
        });
      }, 1000);
    }
    return () => {
      if (interval) clearInterval(interval);
    };
  }, [isTimerRunning, targetMinutes]);

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

    const snapped = snapDuracao(mins);
    setTargetMinutes(snapped);
    setSecondsLeft(snapped * 60);
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

  // Calculate ring progress ratio (10, 15 ou 30 min)
  const totalSeconds = targetMinutes * 60;
  const currentRatio = targetMinutes / 30;

  // Calculate stroke dashoffset for filled arc (during setup)
  const strokeDashoffset = circumference * (1 - currentRatio);

  // Knob coordinate on circle circumference
  const knobAngleRad = (currentRatio * 360 - 90) * (Math.PI / 180);
  const knobX = center + radius * Math.cos(knobAngleRad);
  const knobY = center + radius * Math.sin(knobAngleRad);

  const handleStartFocusPress = () => {
    setShowFocusSheet(true);
  };

  const handleSelectFocusMode = async (mode: 'livre' | 'direcionado') => {
    setShowFocusSheet(false);
    setFocusMode(mode);

    try {
      const intencao = resolveIntencao(mode, params.intencao);
      await iniciarSessao(intencao, targetMinutes);
      setHasStartedFocus(true);
      setIsTimerRunning(true);
    } catch {
      Alert.alert('Erro', 'Não foi possível iniciar a sessão de estudo.');
    }
  };

  const handlePauseResume = () => {
    setIsTimerRunning((prev) => !prev);
  };

  const handleCancelPress = () => {
    setShowCancelModal(true);
  };

  const executeCancelFocus = async () => {
    setIsTimerRunning(false);
    setHasStartedFocus(false);
    setFocusMode(null);
    setSecondsLeft(targetMinutes * 60);
    setShowCancelModal(false);
    try {
      await interromperSessao();
    } catch {
      Alert.alert('Aviso', 'Sessão cancelada localmente, mas falhou ao sincronizar com o servidor.');
    }
  };

  const saldoBiscoitos = resumo?.saldoBiscoitos ?? resumo?.viveiro?.saldoBiscoitos ?? 0;

  return (
    <View style={[styles.container, { paddingTop: insets.top + 12 }]}>
      {/* Top Header Bar */}
      <View style={styles.topBar}>
        <Pressable style={({ pressed }) => [{ opacity: pressed ? 0.7 : 1 }]}>
          <Feather name="menu" size={26} color="#FFFFFF" />
        </Pressable>

        <View style={styles.coinBadge}>
          <Text style={styles.coinIcon}>🪙</Text>
          <Text style={styles.coinText}>{saldoBiscoitos}</Text>
        </View>
      </View>

      {/* Main Content Container */}
      <View style={styles.contentContainer}>
        {/* Title */}
        <Text style={styles.headerTitle}>
          {hasStartedFocus
            ? `${focusMode === 'direcionado' ? 'Estudo Direcionado' : 'Estudo Livre'}\n${isTimerRunning ? 'Foco em andamento ⏳' : 'Sessão Pausada ⏸️'}`
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
});
