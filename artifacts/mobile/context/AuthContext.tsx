import React, { createContext, useCallback, useContext, useEffect, useMemo, useState } from 'react';

import * as authService from '@/features/auth/authService';
import * as authStorage from '@/features/auth/authStorage';
import type { StoredSession, UserProfile } from '@/features/auth/types';
import { ApiClientError, setApiCredentials } from '@/services/apiClient';

interface AuthContextType {
  user: UserProfile | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  login: (usuario: string, senha: string) => Promise<void>;
  register: (displayName: string, usuario: string, senha: string) => Promise<void>;
  logout: () => Promise<void>;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

function buildDisplayName(displayName: string | undefined, usuario: string): string {
  if (displayName?.trim()) {
    return displayName.trim();
  }
  const localPart = usuario.split('@')[0] ?? usuario;
  return localPart.charAt(0).toUpperCase() + localPart.slice(1);
}

function buildInitials(displayName: string): string {
  const parts = displayName.split(' ').filter(Boolean);
  if (parts.length === 0) {
    return 'CS';
  }
  return parts
    .slice(0, 2)
    .map((part) => part[0]?.toUpperCase() ?? '')
    .join('');
}

function toUserProfile(session: StoredSession): UserProfile {
  const displayName = buildDisplayName(session.displayName, session.usuario);
  return {
    usuarioId: session.usuarioId,
    usuario: session.usuario,
    displayName,
    avatarInitials: buildInitials(displayName),
  };
}

function applySession(session: StoredSession): UserProfile {
  setApiCredentials({ usuario: session.usuario, senha: session.senha });
  return toUserProfile(session);
}

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [user, setUser] = useState<UserProfile | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    let active = true;

    async function restoreSession() {
      try {
        const stored = await authStorage.loadStoredSession();
        if (!stored) {
          return;
        }

        applySession(stored);
        await authService.buscarSessao();

        if (active) {
          setUser(toUserProfile(stored));
        }
      } catch {
        await authStorage.clearStoredSession();
        setApiCredentials(null);
        if (active) {
          setUser(null);
        }
      } finally {
        if (active) {
          setIsLoading(false);
        }
      }
    }

    void restoreSession();

    return () => {
      active = false;
    };
  }, []);

  const persistSession = useCallback(async (session: StoredSession) => {
    await authStorage.saveStoredSession(session);
    setUser(applySession(session));
  }, []);

  const login = useCallback(
    async (usuario: string, senha: string) => {
      try {
        const response = await authService.login(usuario, senha);
        await persistSession({
          usuarioId: response.usuarioId,
          usuario: response.usuario,
          senha,
        });
      } catch (error) {
        if (error instanceof ApiClientError && error.status === 401) {
          throw new Error('Usuário ou senha inválidos');
        }
        throw error instanceof Error ? error : new Error('Falha ao entrar');
      }
    },
    [persistSession],
  );

  const register = useCallback(
    async (displayName: string, usuario: string, senha: string) => {
      try {
        const response = await authService.registrar(usuario, senha);
        await persistSession({
          usuarioId: response.usuarioId,
          usuario: response.usuario,
          senha,
          displayName,
        });
      } catch (error) {
        if (error instanceof ApiClientError && error.status === 409) {
          throw new Error('Usuário já cadastrado');
        }
        throw error instanceof Error ? error : new Error('Falha ao registrar');
      }
    },
    [persistSession],
  );

  const logout = useCallback(async () => {
    await authStorage.clearStoredSession();
    setApiCredentials(null);
    setUser(null);
  }, []);

  const value = useMemo(
    () => ({
      user,
      isAuthenticated: user !== null,
      isLoading,
      login,
      register,
      logout,
    }),
    [user, isLoading, login, register, logout],
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
}
