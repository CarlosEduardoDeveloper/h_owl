import React, { useState } from 'react';
import {
  Image,
  Modal,
  Pressable,
  ScrollView,
  StyleSheet,
  Text,
  View,
} from 'react-native';
import { useRouter, type Href } from 'expo-router';
import { Feather } from '@expo/vector-icons';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import { useAuth } from '@/context/AuthContext';
import { useHome } from '@/features/home/useHome';

const CURSO_PADRAO_1 = { titulo: 'Livro de Mateus', progresso: 68 };
const CURSO_PADRAO_2 = { titulo: 'Glossário Judaico', progresso: 34 };

export default function HomeScreen() {
  const router = useRouter();
  const insets = useSafeAreaInsets();
  const { user, logout } = useAuth();
  const { data: resumo } = useHome();

  const [showProfileMenu, setShowProfileMenu] = useState(false);
  const [showFocusSheet, setShowFocusSheet] = useState(false);

  const userName = user?.displayName?.split(' ')[0] || 'Estudante';
  const userInitials = user?.avatarInitials || 'CS';

  const ofensiva = resumo?.ofensiva ?? 0;
  const xpDiario = resumo?.xpDiario ?? 0;
  const ranking = resumo?.ranking ?? 0;
  const mensagemArvore = resumo?.mensagemArvore;

  const trilha1 = resumo?.trilhasEmProgresso?.[0];
  const trilha2 = resumo?.trilhasEmProgresso?.[1];
  const curso1Titulo = trilha1?.titulo ?? CURSO_PADRAO_1.titulo;
  const curso1Progresso = trilha1?.progressoPercentual ?? CURSO_PADRAO_1.progresso;
  const curso2Titulo = trilha2?.titulo ?? CURSO_PADRAO_2.titulo;
  const curso2Progresso = trilha2?.progressoPercentual ?? CURSO_PADRAO_2.progresso;

  const handleLogout = () => {
    setShowProfileMenu(false);
    logout();
    router.replace('/(auth)/login');
  };

  const handleStartQuiz = () => {
    setShowFocusSheet(false);
    router.push('/quiz' as Href);
  };

  const handleStartFocus = (mode: string) => {
    setShowFocusSheet(false);
    router.push({
      pathname: '/(tabs)/focus',
      params: {
        intencao: mode === 'direcionado' ? 'TRILHA' : 'LEITURA_LIVRE',
        referenciaUsfm: mode === 'direcionado' ? 'MAT.1.1' : 'JHN.3.16',
      },
    });
  };

  return (
    <View style={[styles.container, { paddingTop: insets.top + 16 }]}>
      <ScrollView
        contentContainerStyle={styles.scrollContent}
        showsVerticalScrollIndicator={false}
      >
        {/* Header Bar */}
        <View style={styles.headerRow}>
          <View>
            <Text style={styles.greetingText}>Bom dia,</Text>
            <View style={styles.nameRow}>
              <Text style={styles.userNameText}>{userName}</Text>
              <Text style={styles.waveEmoji}> 👋</Text>
            </View>
          </View>

          {/* Profile Avatar Button */}
          <Pressable
            style={({ pressed }) => [
              styles.avatarButton,
              { opacity: pressed ? 0.8 : 1 },
            ]}
            onPress={() => setShowProfileMenu(!showProfileMenu)}
          >
            <Text style={styles.avatarText}>{userInitials}</Text>
          </Pressable>
        </View>

        {/* 3 Stat Cards Row */}
        {mensagemArvore ? (
          <View style={styles.alertBanner}>
            <Text style={styles.alertBannerText}>🌳 {mensagemArvore}</Text>
          </View>
        ) : null}

        <View style={styles.statsRow}>
          {/* Card 1: Ofensiva */}
          <View style={[styles.statCard, { backgroundColor: '#FDE3D2' }]}>
            <Text style={styles.statValue}>{ofensiva} 🔥</Text>
            <Text style={styles.statLabel}>Ofensiva</Text>
          </View>

          {/* Card 2: XP Diário */}
          <View style={[styles.statCard, { backgroundColor: '#D7F9EB' }]}>
            <Text style={styles.statValue}>{xpDiario}</Text>
            <Text style={styles.statLabel}>XP Diário</Text>
          </View>

          {/* Card 3: Ranquing */}
          <View style={[styles.statCard, { backgroundColor: '#EBE4FF' }]}>
            <Text style={styles.statValue}>#{ranking}</Text>
            <Text style={styles.statLabel}>Ranquing</Text>
          </View>
        </View>

        {/* Center Isometric Corujal Illustration */}
        <View style={styles.sanctuaryContainer}>
          <Image
            source={require('@/assets/images/corujal.png')}
            style={styles.sanctuaryImage}
            resizeMode="contain"
          />
        </View>

        {/* Continue Aprendendo Section */}
        <View style={styles.learningSection}>
          <Text style={styles.sectionTitle}>CONTINUE APRENDENDO</Text>

          {/* Course Card 1: Livro de Mateus */}
          <Pressable
            style={({ pressed }) => [
              styles.courseCard,
              { opacity: pressed ? 0.95 : 1 },
            ]}
            onPress={() => setShowFocusSheet(true)}
          >
            <View style={[styles.courseIconCircle, { backgroundColor: '#FDE8E0' }]}>
              <Feather name="book-open" size={20} color="#ED5B0A" />
            </View>
            <View style={styles.courseContent}>
              <Text style={styles.courseTitle}>{curso1Titulo}</Text>
              <View style={styles.progressRow}>
                <View style={styles.progressBarBg}>
                  <View style={[styles.progressBarFill, { width: `${curso1Progresso}%`, backgroundColor: '#ED5B0A' }]} />
                </View>
                <Text style={styles.progressPercent}>{curso1Progresso}%</Text>
              </View>
            </View>
          </Pressable>

          {/* Course Card 2: Glossário Judaico */}
          <Pressable
            style={({ pressed }) => [
              styles.courseCard,
              { opacity: pressed ? 0.95 : 1 },
            ]}
            onPress={() => setShowFocusSheet(true)}
          >
            <View style={[styles.courseIconCircle, { backgroundColor: '#F0E8FF' }]}>
              <Feather name="book-open" size={20} color="#7C3AED" />
            </View>
            <View style={styles.courseContent}>
              <Text style={styles.courseTitle}>{curso2Titulo}</Text>
              <View style={styles.progressRow}>
                <View style={styles.progressBarBg}>
                  <View style={[styles.progressBarFill, { width: `${curso2Progresso}%`, backgroundColor: '#7C3AED' }]} />
                </View>
                <Text style={styles.progressPercent}>{curso2Progresso}%</Text>
              </View>
            </View>
          </Pressable>
        </View>
      </ScrollView>

      {/* Profile Dropdown Popover Menu (Image 5) */}
      {showProfileMenu && (
        <Pressable
          style={styles.popoverOverlay}
          onPress={() => setShowProfileMenu(false)}
        >
          <View style={[styles.profileMenuCard, { top: insets.top + 72 }]}>
            <Text style={styles.menuUserName}>{user?.displayName || 'Estudante'}</Text>
            <Text style={styles.menuUserEmail}>{user?.usuario || ''}</Text>

            <View style={styles.menuDivider} />

            <Pressable
              style={styles.menuItem}
              onPress={() => setShowProfileMenu(false)}
            >
              <Text style={styles.menuItemText}>Perfil</Text>
            </Pressable>

            <Pressable
              style={styles.menuItem}
              onPress={() => setShowProfileMenu(false)}
            >
              <Text style={styles.menuItemText}>Configurações</Text>
            </Pressable>

            <Pressable
              style={styles.menuItem}
              onPress={() => setShowProfileMenu(false)}
            >
              <Text style={styles.menuItemText}>Conquistas</Text>
            </Pressable>

            <View style={styles.menuDivider} />

            <Pressable style={styles.menuItem} onPress={handleLogout}>
              <Text style={[styles.menuItemText, { color: '#D93838' }]}>Log out</Text>
            </Pressable>
          </View>
        </Pressable>
      )}

      {/* Bottom Sheet Focus Mode Action Modal (Image 2) */}
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

            {/* Option 1: Estudo Livre */}
            <Pressable
              style={({ pressed }) => [
                styles.sheetOptionPill,
                { opacity: pressed ? 0.85 : 1 },
              ]}
              onPress={() => handleStartFocus('livre')}
            >
              <Text style={styles.sheetOptionPillText}>Estudo Livre</Text>
            </Pressable>

            {/* Option 2: Estudo Direcionado */}
            <Pressable
              style={({ pressed }) => [
                styles.sheetOptionTextRow,
                { opacity: pressed ? 0.7 : 1 },
              ]}
              onPress={() => handleStartFocus('direcionado')}
            >
              <Text style={styles.sheetOptionText}>Estudo Direcionado</Text>
            </Pressable>

            {/* Option 3: Quiz */}
            <Pressable
              style={({ pressed }) => [
                styles.sheetOptionTextRow,
                { opacity: pressed ? 0.7 : 1 },
              ]}
              onPress={handleStartQuiz}
            >
              <Text style={styles.sheetOptionText}>Quiz</Text>
            </Pressable>
          </View>
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
  scrollContent: {
    paddingHorizontal: 20,
    paddingBottom: 110,
  },
  headerRow: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    marginBottom: 24,
  },
  greetingText: {
    fontSize: 16,
    color: '#8C7C6D',
    fontWeight: '500',
  },
  nameRow: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  userNameText: {
    fontSize: 28,
    fontWeight: '900',
    color: '#1E1B38',
  },
  waveEmoji: {
    fontSize: 24,
  },
  avatarButton: {
    width: 48,
    height: 48,
    borderRadius: 24,
    backgroundColor: '#046865',
    alignItems: 'center',
    justifyContent: 'center',
    shadowColor: '#046865',
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.2,
    shadowRadius: 6,
    elevation: 3,
  },
  avatarText: {
    color: '#FFFFFF',
    fontSize: 17,
    fontWeight: '700',
  },
  alertBanner: {
    backgroundColor: '#FFF3CD',
    borderRadius: 12,
    paddingVertical: 10,
    paddingHorizontal: 14,
    marginBottom: 12,
    borderWidth: 1,
    borderColor: '#FFE08A',
  },
  alertBannerText: {
    color: '#7A5C00',
    fontWeight: '600',
    fontSize: 14,
    textAlign: 'center',
  },
  statsRow: {
    flexDirection: 'row',
    gap: 12,
    marginBottom: 24,
  },
  statCard: {
    flex: 1,
    borderRadius: 20,
    paddingVertical: 16,
    paddingHorizontal: 10,
    alignItems: 'center',
    justifyContent: 'center',
  },
  statValue: {
    fontSize: 18,
    fontWeight: '800',
    color: '#1E1B38',
    marginBottom: 4,
  },
  statLabel: {
    fontSize: 12,
    color: '#8C7C6D',
    fontWeight: '600',
  },
  sanctuaryContainer: {
    alignItems: 'center',
    justifyContent: 'center',
    marginVertical: 8,
  },
  sanctuaryImage: {
    width: '100%',
    height: 290,
    borderRadius: 24,
  },
  learningSection: {
    marginTop: 24,
  },
  sectionTitle: {
    fontSize: 13,
    fontWeight: '800',
    letterSpacing: 1.2,
    color: '#1E1B38',
    marginBottom: 16,
  },
  courseCard: {
    backgroundColor: '#FFFFFF',
    borderRadius: 22,
    padding: 16,
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 12,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 3 },
    shadowOpacity: 0.04,
    shadowRadius: 8,
    elevation: 2,
    gap: 14,
  },
  courseIconCircle: {
    width: 46,
    height: 46,
    borderRadius: 23,
    alignItems: 'center',
    justifyContent: 'center',
  },
  courseContent: {
    flex: 1,
  },
  courseTitle: {
    fontSize: 16,
    fontWeight: '700',
    color: '#1E1B38',
    marginBottom: 8,
  },
  progressRow: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 10,
  },
  progressBarBg: {
    flex: 1,
    height: 6,
    backgroundColor: '#F3EFE6',
    borderRadius: 3,
    overflow: 'hidden',
  },
  progressBarFill: {
    height: '100%',
    borderRadius: 3,
  },
  progressPercent: {
    fontSize: 12,
    fontWeight: '600',
    color: '#8C7C6D',
  },
  /* Profile Popover Overlay & Card (Image 5) */
  popoverOverlay: {
    position: 'absolute',
    top: 0,
    left: 0,
    right: 0,
    bottom: 0,
    zIndex: 999,
  },
  profileMenuCard: {
    position: 'absolute',
    right: 20,
    width: 220,
    backgroundColor: '#FFFFFF',
    borderRadius: 20,
    padding: 18,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 8 },
    shadowOpacity: 0.15,
    shadowRadius: 16,
    elevation: 8,
    borderWidth: 1,
    borderColor: '#F0E8D9',
  },
  menuUserName: {
    fontSize: 16,
    fontWeight: '700',
    color: '#1E1B38',
  },
  menuUserEmail: {
    fontSize: 12,
    color: '#8C7C6D',
    marginBottom: 12,
  },
  menuDivider: {
    height: 1,
    backgroundColor: '#F0E8D9',
    marginVertical: 6,
  },
  menuItem: {
    paddingVertical: 10,
  },
  menuItemText: {
    fontSize: 15,
    fontWeight: '600',
    color: '#1E1B38',
  },
  /* Bottom Sheet Focus Action Modal (Image 2) */
  modalOverlay: {
    flex: 1,
    backgroundColor: 'rgba(0, 0, 0, 0.3)',
    justifyContent: 'flex-end',
  },
  bottomSheetContainer: {
    backgroundColor: '#FFFFFF',
    borderTopLeftRadius: 28,
    borderTopRightRadius: 28,
    paddingTop: 12,
    paddingBottom: 40,
    paddingHorizontal: 24,
    alignItems: 'center',
  },
  dragHandle: {
    width: 44,
    height: 5,
    borderRadius: 2.5,
    backgroundColor: '#D1D1D6',
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
    paddingVertical: 12,
    alignItems: 'center',
  },
  sheetOptionText: {
    fontSize: 17,
    fontWeight: '600',
    color: '#999999',
  },
});
