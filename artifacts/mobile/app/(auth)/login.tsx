import React, { useState } from 'react';
import {
  Image,
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
import colors from '@/constants/colors';

export default function LoginScreen() {
  const router = useRouter();
  const insets = useSafeAreaInsets();
  const { login } = useAuth();

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);

  const handleSignIn = async () => {
    setErrorMessage(null);
    setIsSubmitting(true);
    try {
      await login(email, password);
      router.replace('/(tabs)');
    } catch (error) {
      setErrorMessage(error instanceof Error ? error.message : 'Falha ao entrar');
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <View style={[styles.container, { paddingTop: insets.top + 20, paddingBottom: insets.bottom + 16 }]}>
      <ScrollView contentContainerStyle={styles.scrollContent} showsVerticalScrollIndicator={false}>
        {/* Top Logo & Title */}
        <View style={styles.header}>
          <Image
            source={require('@/assets/images/owl_logo.jpg')}
            style={styles.logo}
            resizeMode="contain"
          />
          <Text style={styles.brandTitle}>HOWL</Text>
        </View>

        {/* Form Container */}
        <View style={styles.form}>
          {/* Email Field */}
          <View style={styles.fieldGroup}>
            <Text style={styles.label}>EMAIL</Text>
            <View style={styles.inputWrapper}>
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
          </View>

          {/* Password Field */}
          <View style={styles.fieldGroup}>
            <Text style={styles.label}>SENHA</Text>
            <View style={styles.inputWrapper}>
              <TextInput
                style={[styles.input, { paddingRight: 48 }]}
                value={password}
                onChangeText={setPassword}
                secureTextEntry={!showPassword}
                placeholder="Sua senha"
                placeholderTextColor="#A4998E"
              />
              <Pressable
                style={styles.eyeIcon}
                onPress={() => setShowPassword(!showPassword)}
                hitSlop={12}
              >
                <Feather
                  name={showPassword ? 'eye-off' : 'eye'}
                  size={20}
                  color="#1E1B38"
                />
              </Pressable>
            </View>
          </View>

          {/* Forgot Password Link */}
          <Pressable style={styles.forgotPasswordWrapper}>
            <Text style={styles.forgotPasswordText}>Esqueceu a senha?</Text>
          </Pressable>

          {/* Action Buttons */}
          <View style={styles.buttonContainer}>
            {errorMessage ? <Text style={styles.errorText}>{errorMessage}</Text> : null}
            <Pressable
              style={({ pressed }) => [
                styles.primaryButton,
                { opacity: pressed || isSubmitting ? 0.9 : 1 },
              ]}
              onPress={handleSignIn}
              disabled={isSubmitting}
            >
              <Text style={styles.primaryButtonText}>
                {isSubmitting ? 'Entrando...' : 'Sign In'}
              </Text>
            </Pressable>

            <Pressable
              style={({ pressed }) => [
                styles.secondaryButton,
                { opacity: pressed ? 0.8 : 1 },
              ]}
              onPress={() => router.push('/(auth)/register')}
            >
              <Text style={styles.secondaryButtonText}>Criar uma conta</Text>
            </Pressable>
          </View>
        </View>

        {/* Footer Legal Terms */}
        <View style={styles.footer}>
          <Text style={styles.footerText}>
            Ao continuar, você concorda com nossos{' '}
            <Text style={styles.linkText}>Termos</Text> e{' '}
            <Text style={styles.linkText}>Política de Privacidade</Text>
          </Text>
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
    flexGrow: 1,
    paddingHorizontal: 24,
    justifyContent: 'space-between',
    paddingBottom: 24,
  },
  header: {
    alignItems: 'center',
    marginTop: 20,
    marginBottom: 32,
  },
  logo: {
    width: 100,
    height: 100,
    marginBottom: 8,
  },
  brandTitle: {
    fontSize: 42,
    fontWeight: '900',
    letterSpacing: 2,
    color: '#1E1B38',
    fontFamily: 'Inter_700Bold',
  },
  form: {
    width: '100%',
  },
  fieldGroup: {
    marginBottom: 20,
  },
  label: {
    fontSize: 12,
    fontWeight: '800',
    letterSpacing: 1.2,
    color: '#8C7C6D',
    marginBottom: 8,
  },
  inputWrapper: {
    position: 'relative',
    justifyContent: 'center',
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
  eyeIcon: {
    position: 'absolute',
    right: 18,
    padding: 4,
  },
  forgotPasswordWrapper: {
    alignSelf: 'flex-end',
    marginBottom: 28,
  },
  forgotPasswordText: {
    fontSize: 14,
    fontWeight: '700',
    color: '#ED5B0A',
  },
  buttonContainer: {
    gap: 14,
  },
  errorText: {
    color: '#B42318',
    fontSize: 14,
    fontWeight: '600',
    textAlign: 'center',
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
  },
  primaryButtonText: {
    color: '#FFFFFF',
    fontSize: 16,
    fontWeight: '700',
  },
  secondaryButton: {
    backgroundColor: 'transparent',
    borderRadius: 28,
    height: 56,
    alignItems: 'center',
    justifyContent: 'center',
    borderWidth: 1.5,
    borderColor: '#ED5B0A',
  },
  secondaryButtonText: {
    color: '#ED5B0A',
    fontSize: 16,
    fontWeight: '700',
  },
  footer: {
    marginTop: 40,
    alignItems: 'center',
    paddingHorizontal: 16,
  },
  footerText: {
    fontSize: 13,
    color: '#8C7C6D',
    textAlign: 'center',
    lineHeight: 20,
    fontWeight: '500',
  },
  linkText: {
    color: '#ED5B0A',
    fontWeight: '700',
  },
});
