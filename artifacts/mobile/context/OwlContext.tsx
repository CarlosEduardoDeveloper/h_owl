import React, { createContext, useContext, useState, useEffect } from 'react';
import AsyncStorage from '@react-native-async-storage/async-storage';

export type OwlKey = 'coruja1' | 'coruja2' | 'coruja3';

export interface HatchedOwl {
  id: string;
  owlKey: OwlKey;
  name: string;
  hatchedAt: number;
}

interface OwlContextType {
  hatchedOwls: HatchedOwl[];
  coins: number;
  addHatchedOwl: (owlKey: OwlKey) => HatchedOwl;
  addCoins: (amount: number) => void;
}

const OwlContext = createContext<OwlContextType | undefined>(undefined);

const STORAGE_KEY_OWLS = '@howl_hatched_owls';
const STORAGE_KEY_COINS = '@howl_user_coins';

const INITIAL_OWLS: HatchedOwl[] = [
  {
    id: 'initial_owl_1',
    owlKey: 'coruja1',
    name: 'Corujinha Baby',
    hatchedAt: Date.now() - 86400000,
  },
];

export const OwlProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [hatchedOwls, setHatchedOwls] = useState<HatchedOwl[]>(INITIAL_OWLS);
  const [coins, setCoins] = useState<number>(55);
  const [isLoaded, setIsLoaded] = useState<boolean>(false);

  useEffect(() => {
    async function loadData() {
      try {
        const storedOwls = await AsyncStorage.getItem(STORAGE_KEY_OWLS);
        const storedCoins = await AsyncStorage.getItem(STORAGE_KEY_COINS);

        if (storedOwls) {
          const parsed = JSON.parse(storedOwls);
          if (Array.isArray(parsed) && parsed.length > 0) {
            setHatchedOwls(parsed);
          }
        }
        if (storedCoins) {
          setCoins(parseInt(storedCoins, 10) || 55);
        }
      } catch (e) {
        console.error('Error loading owl sanctuary data', e);
      } finally {
        setIsLoaded(true);
      }
    }
    loadData();
  }, []);

  const addHatchedOwl = (owlKey: OwlKey): HatchedOwl => {
    const owlNames: Record<OwlKey, string> = {
      coruja1: 'Coruja Recém-Nascida',
      coruja2: 'Coruja Sábia',
      coruja3: 'Coruja Real',
    };

    const newOwl: HatchedOwl = {
      id: `owl_${Date.now()}_${Math.random().toString(36).substr(2, 4)}`,
      owlKey,
      name: owlNames[owlKey] || 'Coruja Estudiosa',
      hatchedAt: Date.now(),
    };

    const updated = [...hatchedOwls, newOwl];
    setHatchedOwls(updated);

    AsyncStorage.setItem(STORAGE_KEY_OWLS, JSON.stringify(updated)).catch(() => {});

    return newOwl;
  };

  const addCoins = (amount: number) => {
    setCoins((prev) => {
      const updated = prev + amount;
      AsyncStorage.setItem(STORAGE_KEY_COINS, updated.toString()).catch(() => {});
      return updated;
    });
  };

  return (
    <OwlContext.Provider value={{ hatchedOwls, coins, addHatchedOwl, addCoins }}>
      {children}
    </OwlContext.Provider>
  );
};

export function useOwlSanctuary() {
  const context = useContext(OwlContext);
  if (!context) {
    throw new Error('useOwlSanctuary must be used within an OwlProvider');
  }
  return context;
}
