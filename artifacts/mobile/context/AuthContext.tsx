import React, { createContext, useContext, useState } from 'react';

export interface UserProfile {
  name: string;
  email: string;
  avatarInitials: string;
}

interface AuthContextType {
  user: UserProfile | null;
  isAuthenticated: boolean;
  login: (email: string, password: string) => Promise<void>;
  register: (name: string, email: string, password: string, birthDate?: string) => Promise<void>;
  logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({ children }: { children: React.ReactNode }) {
  // Default to Jane Houston for smooth previewing matching the attached screenshots
  const [user, setUser] = useState<UserProfile | null>({
    name: 'Jane Houston',
    email: 'jane.houston@email.com',
    avatarInitials: 'JH',
  });

  const login = async (email: string, password: string) => {
    const namePart = email.split('@')[0] || 'User';
    const initials = namePart.slice(0, 2).toUpperCase();
    setUser({
      name: email === 'jane.houston@email.com' ? 'Jane Houston' : namePart,
      email: email,
      avatarInitials: email === 'jane.houston@email.com' ? 'JH' : initials,
    });
  };

  const register = async (name: string, email: string, password: string, birthDate?: string) => {
    const initials = name
      .split(' ')
      .filter(Boolean)
      .map((n) => n[0])
      .slice(0, 2)
      .join('')
      .toUpperCase() || 'JH';

    setUser({
      name: name || 'Jane Houston',
      email: email || 'jane.houston@email.com',
      avatarInitials: initials,
    });
  };

  const logout = () => {
    setUser(null);
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        isAuthenticated: !!user,
        login,
        register,
        logout,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
}
