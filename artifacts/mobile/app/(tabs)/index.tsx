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
import { useRouter } from 'expo-router';
import { Feather } from '@expo/vector-icons';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import { useAuth } from '@/context/AuthContext';
import { useHome } from '@/features/home/useHome';
import { useOwlSanctuary, OwlKey } from '@/context/OwlContext';

const CURSO_PADRAO_1 = { titulo: 'Livro de Mateus', progresso: 68 };
const CURSO_PADRAO_2 = { titulo: 'Glossário Judaico', progresso: 34 };

const OWL_ASSETS: Record<OwlKey, any> = {
  coruja1: require('@/assets/images/coruja1.png'),
  coruja2: require('@/assets/images/coruja2.png'),
  coruja3: require('@/assets/images/coruja3.png'),
};

const PERCH_POSITIONS = [
  { top: '22%', left: '20%' },
  { top: '24%', right: '20%' },
  { top: '48%', left: '18%' },
  { top: '50%', right: '18%' },
  { top: '38%', left: '42%' },
  { top: '65%', left: '30%' },
  { top: '66%', right: '30%' },
];

export default function HomeScreen() {
  const router = useRouter();
  const insets = useSafeAreaInsets();
  const { user, logout } = useAuth();
  const { data: resumo } = useHome();
  const { hatchedOwls } = useOwlSanctuary();

  const [showProfileMenu, setShowProfileMenu] = useState(false);
  const [showFocusSheet, setShowFocusSheet] = useState(false);
  const [activeTooltipId, setActiveTooltipId] = useState<string | null>(null);

  const userName = user?.displayName?.split(' ')[0] || 'Estudante';
  const userInitials = user?.avatarInitials || 'CS';

  const ofensiva = resumo?.ofensiva ?? 7;
  const xpDiario = resumo?.xpDiario ?? 320;
  const ranking = resumo?.ranking ?? 42;

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

  const handleStartFocus = (mode: string) => {
    setShowFocusSheet(false);
    if (mode === 'direcionado') {
      router.push('/study');
    } else {
      router.push({
        pathname: '/(tabs)/focus',
        params: {
          intencao: 'LEITURA_LIVRE',
          referenciaUsfm: 'JHN.3.16',
        },
      });
    }
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

          {/* Render Earned Owls Perched on Branches / Platforms */}
          {hatchedOwls.map((owl, index) => {
            const position = PERCH_POSITIONS[index % PERCH_POSITIONS.length];
            const owlSource = OWL_ASSETS[owl.owlKey] || OWL_ASSETS.coruja1;
            const isTooltipOpen = activeTooltipId === owl.id;

            return (
              <Pressable
                key={owl.id}
                style={({ pressed }) => [
                  styles.perchedOwlWrapper,
                  position as any,
                  { transform: [{ scale: pressed ? 0.92 : 1 }] },
                ]}
                onPress={() => setActiveTooltipId(isTooltipOpen ? null : owl.id)}
              >
                {/* Speech Bubble Tooltip */}
                {isTooltipOpen && (
                  <View style={styles.owlSpeechBubble}>
                    <Text style={styles.owlSpeechText}>{owl.name} 🦉✨</Text>
                  </View>
                )}

                <Image
                  source={owlSource}
                  style={styles.perchedOwlImage}
                  resizeMode="contain"
                />
              </Pressable>
            );
          })}
        </View>

        {/* Continue Aprendendo Section */}
        <View style={styles.learningSection}>
          <View style={styles.sectionHeaderRow}>
            <Text style={styles.sectionTitle}>CONTINUE APRENDENDO</Text>
            <Pressable onPress={() => router.push('/study')}>
              <Text style={styles.seeAllText}>Ver todos ›</Text>
            </Pressable>
          </View>

          {/* Directed Study 1: Jesus, nosso socorro nas crises */}
          <Pressable
            style={({ pressed }) => [
              styles.courseCard,
              { opacity: pressed ? 0.95 : 1, transform: [{ scale: pressed ? 0.98 : 1 }] },
            ]}
            onPress={() =>
              router.push({
                pathname: '/(tabs)/focus',
                params: {
                  topicId: '1',
                  topicTitle: 'Jesus, nosso socorro nas crises',
                  autoStart: 'true',
                },
              })
            }
          >
            <View style={[styles.courseIconCircle, { backgroundColor: '#FDF3D7' }]}>
              <Text style={styles.cardEmoji}>🙏</Text>
            </View>
            <View style={styles.courseContent}>
              <Text style={styles.courseTitle}>Jesus, nosso socorro nas crises</Text>
              <Text style={styles.courseSubtitle}>O socorro de Deus nunca falha</Text>
              <View style={styles.progressRow}>
                <View style={styles.progressBarBg}>
                  <View style={[styles.progressBarFill, { width: '100%', backgroundColor: '#D97706' }]} />
                </View>
                <Text style={styles.progressPercent}>100%</Text>
              </View>
            </View>
            <Feather name="chevron-right" size={20} color="#9CA3AF" />
          </Pressable>

          {/* Directed Study 2: Amizades improváveis */}
          <Pressable
            style={({ pressed }) => [
              styles.courseCard,
              { opacity: pressed ? 0.95 : 1, transform: [{ scale: pressed ? 0.98 : 1 }] },
            ]}
            onPress={() =>
              router.push({
                pathname: '/(tabs)/focus',
                params: {
                  topicId: '2',
                  topicTitle: 'Amizades improváveis',
                  autoStart: 'true',
                },
              })
            }
          >
            <View style={[styles.courseIconCircle, { backgroundColor: '#EDE9FE' }]}>
              <Text style={styles.cardEmoji}>👥</Text>
            </View>
            <View style={styles.courseContent}>
              <Text style={styles.courseTitle}>Amizades improváveis</Text>
              <Text style={styles.courseSubtitle}>Olhar além das aparências</Text>
              <View style={styles.progressRow}>
                <View style={styles.progressBarBg}>
                  <View style={[styles.progressBarFill, { width: '45%', backgroundColor: '#8B5CF6' }]} />
                </View>
                <Text style={styles.progressPercent}>45%</Text>
              </View>
            </View>
            <Feather name="chevron-right" size={20} color="#9CA3AF" />
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
    position: 'relative',
    height: 290,
    width: '100%',
    borderRadius: 24,
    overflow: 'hidden',
  },
  sanctuaryImage: {
    width: '100%',
    height: 290,
    borderRadius: 24,
  },
  perchedOwlWrapper: {
    position: 'absolute',
    width: 52,
    height: 52,
    alignItems: 'center',
    justifyContent: 'center',
    zIndex: 10,
  },
  perchedOwlImage: {
    width: 48,
    height: 48,
  },
  owlSpeechBubble: {
    position: 'absolute',
    top: -26,
    backgroundColor: '#FFFFFF',
    paddingHorizontal: 10,
    paddingVertical: 4,
    borderRadius: 12,
    borderWidth: 1,
    borderColor: '#FDE8D0',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.15,
    shadowRadius: 4,
    elevation: 4,
    zIndex: 20,
  },
  owlSpeechText: {
    fontSize: 11,
    fontWeight: '800',
    color: '#1E1B38',
  },
  learningSection: {
    marginTop: 24,
  },
  sectionHeaderRow: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    marginBottom: 16,
  },
  sectionTitle: {
    fontSize: 13,
    fontWeight: '800',
    letterSpacing: 1.2,
    color: '#1E1B38',
  },
  seeAllText: {
    fontSize: 13,
    fontWeight: '700',
    color: '#ED5B0A',
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
    width: 48,
    height: 48,
    borderRadius: 24,
    alignItems: 'center',
    justifyContent: 'center',
  },
  cardEmoji: {
    fontSize: 22,
  },
  courseContent: {
    flex: 1,
  },
  courseTitle: {
    fontSize: 15,
    fontWeight: '700',
    color: '#1E1B38',
    marginBottom: 2,
  },
  courseSubtitle: {
    fontSize: 12,
    fontWeight: '500',
    color: '#8C7C6D',
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
