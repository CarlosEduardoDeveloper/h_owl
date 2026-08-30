import {
  getGetSystemStatusQueryKey,
  useGetSystemStatus,
} from '@workspace/api-client-react';
import { Feather } from '@expo/vector-icons';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import {
  ActivityIndicator,
  Pressable,
  StyleSheet,
  Text,
  View,
} from 'react-native';
import { useColors } from '@/hooks/useColors';

export default function StatusScreen() {
  const colors = useColors();
  const insets = useSafeAreaInsets();
  const { data, isLoading, isError, refetch } = useGetSystemStatus({
    query: {
      retry: 1,
      queryKey: getGetSystemStatusQueryKey(),
    },
  });
  const isOnline = data?.status === 'UP' && !isError;

  return (
    <View
      style={[
        styles.container,
        {
          backgroundColor: colors.background,
          paddingTop: insets.top + 32,
          paddingBottom: insets.bottom + 24,
        },
      ]}
    >
      <View style={[styles.mark, { backgroundColor: colors.primary }]}>
        <Feather name="radio" size={24} color={colors.primaryForeground} />
      </View>
      <Text style={[styles.eyebrow, { color: colors.mutedForeground }]}>
        FOUNDATION MOBILE
      </Text>
      <Text style={[styles.title, { color: colors.foreground }]}>
        Aplicativo iniciado
      </Text>
      <Text style={[styles.text, { color: colors.mutedForeground }]}>
        Esta tela temporária confirma que o app está pronto para conversar com
        o backend.
      </Text>

      <View
        style={[
          styles.statusCard,
          { backgroundColor: colors.card, borderColor: colors.border },
        ]}
      >
        <View style={[styles.statusDot, { backgroundColor: isOnline ? colors.accentForeground : colors.destructive }]} />
        <View style={styles.statusCopy}>
          <Text style={[styles.statusLabel, { color: colors.foreground }]}>
            API
          </Text>
          <Text style={[styles.statusValue, { color: isOnline ? colors.accentForeground : colors.destructive }]}>
            {isLoading ? 'Verificando…' : isOnline ? 'Online' : 'Offline'}
          </Text>
        </View>
        {isLoading ? (
          <ActivityIndicator color={colors.primary} />
        ) : (
          <Pressable
            testID="retry-status"
            accessibilityRole="button"
            accessibilityLabel="Verificar API novamente"
            onPress={() => void refetch()}
            style={({ pressed }) => [
              styles.retryButton,
              { borderColor: colors.border, opacity: pressed ? 0.65 : 1 },
            ]}
          >
            <Feather name="refresh-cw" size={18} color={colors.foreground} />
          </Pressable>
        )}
      </View>

      <Text style={[styles.footer, { color: colors.mutedForeground }]}>
        Spring Boot · PostgreSQL · Flyway
      </Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    paddingHorizontal: 24,
    justifyContent: 'center',
  },
  mark: {
    width: 52,
    height: 52,
    borderRadius: 18,
    alignItems: 'center',
    justifyContent: 'center',
    marginBottom: 24,
  },
  eyebrow: {
    fontSize: 12,
    fontWeight: '700',
    letterSpacing: 1.5,
    marginBottom: 10,
  },
  title: {
    fontSize: 30,
    lineHeight: 36,
    fontWeight: '700',
    marginBottom: 12,
  },
  text: {
    fontSize: 16,
    lineHeight: 24,
    maxWidth: 340,
  },
  statusCard: {
    flexDirection: 'row',
    alignItems: 'center',
    borderWidth: 1,
    borderRadius: 20,
    padding: 18,
    marginTop: 32,
  },
  statusDot: {
    width: 10,
    height: 10,
    borderRadius: 5,
    marginRight: 13,
  },
  statusCopy: {
    flex: 1,
    gap: 3,
  },
  statusLabel: {
    fontSize: 13,
    fontWeight: '600',
  },
  statusValue: {
    fontSize: 18,
    fontWeight: '700',
  },
  retryButton: {
    width: 42,
    height: 42,
    borderRadius: 14,
    borderWidth: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  footer: {
    fontSize: 12,
    marginTop: 28,
  },
});
