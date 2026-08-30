import React, { useState } from 'react';
import {
  Pressable,
  ScrollView,
  StyleSheet,
  Text,
  TextInput,
  View,
} from 'react-native';
import { useRouter } from 'expo-router';
import { Feather } from '@expo/vector-icons';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import { useAuth } from '@/context/AuthContext';

export default function RegisterScreen() {
  const router = useRouter();
  const insets = useSafeAreaInsets();
  const { register } = useAuth();

  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [birthDate, setBirthDate] = useState('');
  const [agreeTerms, setAgreeTerms] = useState(false);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);

  const handleRegister = async () => {
    setErrorMessage(null);
    if (!agreeTerms) {
      setErrorMessage('Aceite os termos para continuar');
      return;
    }
    setIsSubmitting(true);
    try {
      await register(name, email, password);
      router.replace('/(tabs)');
    } catch (error) {
      setErrorMessage(error instanceof Error ? error.message : 'Falha ao registrar');
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <View style={[styles.container, { paddingTop: insets.top + 16, paddingBottom: insets.bottom + 16 }]}>
      <ScrollView contentContainerStyle={styles.scrollContent} showsVerticalScrollIndicator={false}>
        {/* Header Navigation */}
        <View style={styles.topNav}>
          <Pressable
            style={({ pressed }) => [styles.backButton, { opacity: pressed ? 0.7 : 1 }]}
            onPress={() => router.back()}
          >
            <Feather name="chevron-left" size={24} color="#1E1B38" />
          </Pressable>
        </View>

        {/* Title Section */}
        <View style={styles.titleSection}>
          <Text style={styles.title}>Crie seu perfil</Text>
          <Text style={styles.subtitle}>Junte-se hoje mesmo a milhares de alunos</Text>
        </View>

        {/* 3-Step Progress Bar */}
        <View style={styles.stepContainer}>
          <View style={[styles.stepBar, styles.stepActive]} />
          <View style={[styles.stepBar, styles.stepInactive]} />
          <View style={[styles.stepBar, styles.stepInactive]} />
        </View>

        {/* Form Fields */}
        <View style={styles.form}>
          {/* NOME COMPLETO */}
          <View style={styles.fieldGroup}>
            <Text style={styles.label}>NOME COMPLETO</Text>
            <TextInput
              style={styles.input}
              value={name}
              onChangeText={setName}
              placeholder="Seu nome completo"
              placeholderTextColor="#A4998E"
            />
          </View>

          {/* EMAIL */}
          <View style={styles.fieldGroup}>
            <Text style={styles.label}>EMAIL</Text>
            <TextInput
              style={styles.input}
              value={email}
              onChangeText={setEmail}
              placeholder="seu.email@exemplo.com"
              placeholderTextColor="#A4998E"
              keyboardType="email-address"
              autoCapitalize="none"
            />
          </View>

          {/* CRIE UMA SENHA */}
          <View style={styles.fieldGroup}>
            <Text style={styles.label}>CRIE UMA SENHA</Text>
            <TextInput
              style={styles.input}
              value={password}
              onChangeText={setPassword}
              secureTextEntry
              placeholder="Min. 8 caracteres"
              placeholderTextColor="#A4998E"
            />
          </View>

          {/* DATA DE NASCIMENTO */}
          <View style={styles.fieldGroup}>
            <Text style={styles.label}>DATA DE NASCIMENTO</Text>
            <TextInput
              style={styles.input}
              value={birthDate}
              onChangeText={setBirthDate}
              placeholder="DD/MM/AAAA"
              placeholderTextColor="#A4998E"
              keyboardType="numeric"
            />
          </View>

          {/* Checkbox Terms */}
          <Pressable
            style={styles.checkboxRow}
            onPress={() => setAgreeTerms(!agreeTerms)}
          >
            <View style={[styles.checkbox, agreeTerms && styles.checkboxChecked]}>
              {agreeTerms && <Feather name="check" size={14} color="#FFFFFF" />}
            </View>
            <Text style={styles.checkboxLabel}>
              Concordo em receber conteúdo educacional e atualizações sobre os cursos da plataforma.
            </Text>
          </Pressable>

          {/* Primary Action Button */}
          {errorMessage ? <Text style={styles.errorText}>{errorMessage}</Text> : null}
          <Pressable
            style={({ pressed }) => [
              styles.primaryButton,
              { opacity: pressed || isSubmitting ? 0.9 : 1 },
            ]}
            onPress={handleRegister}
            disabled={isSubmitting}
          >
            <Text style={styles.primaryButtonText}>
              {isSubmitting ? 'Criando conta...' : 'Criar minha conta'}
            </Text>
          </Pressable>

          {/* Footer Login Link */}
          <View style={styles.loginFooter}>
            <Text style={styles.loginFooterText}>
              Já tem uma conta?{' '}
              <Text
                style={styles.loginFooterLink}
                onPress={() => router.push('/(auth)/login')}
              >
                Log in
              </Text>
            </Text>
          </View>
        </View>
      </ScrollView>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#FFF6E5',
  },
  scrollContent: {
    paddingHorizontal: 24,
    paddingBottom: 32,
  },
  topNav: {
    marginTop: 8,
    marginBottom: 20,
  },
  backButton: {
    width: 44,
    height: 44,
    borderRadius: 22,
    backgroundColor: '#FFFFFF',
    alignItems: 'center',
    justifyContent: 'center',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.05,
    shadowRadius: 4,
    elevation: 2,
  },
  titleSection: {
    marginBottom: 20,
  },
  title: {
    fontSize: 32,
    fontWeight: '800',
    color: '#1E1B38',
    marginBottom: 6,
  },
  subtitle: {
    fontSize: 15,
    color: '#8C7C6D',
    fontWeight: '500',
  },
  stepContainer: {
    flexDirection: 'row',
    gap: 8,
    marginBottom: 28,
  },
  stepBar: {
    flex: 1,
    height: 6,
    borderRadius: 3,
  },
  stepActive: {
    backgroundColor: '#ED5B0A',
  },
  stepInactive: {
    backgroundColor: '#E8DFC9',
  },
  form: {
    width: '100%',
  },
  fieldGroup: {
    marginBottom: 18,
  },
  label: {
    fontSize: 12,
    fontWeight: '800',
    letterSpacing: 1.2,
    color: '#8C7C6D',
    marginBottom: 8,
  },
  input: {
    backgroundColor: '#FFFFFF',
    borderRadius: 20,
    height: 56,
    paddingHorizontal: 20,
    fontSize: 15,
    color: '#1E1B38',
    fontWeight: '500',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.04,
    shadowRadius: 6,
    elevation: 2,
  },
  checkboxRow: {
    flexDirection: 'row',
    alignItems: 'flex-start',
    marginTop: 10,
    marginBottom: 28,
    gap: 12,
  },
  checkbox: {
    width: 22,
    height: 22,
    borderRadius: 11,
    borderWidth: 2,
    borderColor: '#ED5B0A',
    alignItems: 'center',
    justifyContent: 'center',
    marginTop: 2,
  },
  checkboxChecked: {
    backgroundColor: '#ED5B0A',
  },
  checkboxLabel: {
    flex: 1,
    fontSize: 13,
    color: '#8C7C6D',
    lineHeight: 19,
    fontWeight: '500',
  },
  errorText: {
    color: '#B42318',
    fontSize: 14,
    fontWeight: '600',
    textAlign: 'center',
    marginBottom: 12,
  },
  primaryButton: {
    backgroundColor: '#ED5B0A',
    borderRadius: 28,
    height: 56,
    alignItems: 'center',
    justifyContent: 'center',
    shadowColor: '#ED5B0A',
    shadowOffset: { width: 0, height: 6 },
    shadowOpacity: 0.25,
    shadowRadius: 10,
    elevation: 4,
    marginBottom: 24,
  },
  primaryButtonText: {
    color: '#FFFFFF',
    fontSize: 16,
    fontWeight: '700',
  },
  loginFooter: {
    alignItems: 'center',
  },
  loginFooterText: {
    fontSize: 14,
    color: '#8C7C6D',
    fontWeight: '500',
  },
  loginFooterLink: {
    color: '#ED5B0A',
    fontWeight: '700',
  },
});
