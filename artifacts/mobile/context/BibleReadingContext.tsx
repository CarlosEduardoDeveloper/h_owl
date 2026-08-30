import React, { createContext, useContext, useMemo, useState } from 'react';

import type { BibleLeituraAtual } from '@/features/bible/types';

interface BibleReadingContextValue extends BibleLeituraAtual {
  definirLeitura: (leitura: Partial<BibleLeituraAtual>) => void;
  limparLeitura: () => void;
}

const estadoInicial: BibleLeituraAtual = {
  isLoading: false,
};

const BibleReadingContext = createContext<BibleReadingContextValue | undefined>(undefined);

export function BibleReadingProvider({ children }: { children: React.ReactNode }) {
  const [leitura, setLeitura] = useState<BibleLeituraAtual>(estadoInicial);

  const value = useMemo(
    () => ({
      ...leitura,
      definirLeitura: (parcial: Partial<BibleLeituraAtual>) => {
        setLeitura((atual) => ({ ...atual, ...parcial }));
      },
      limparLeitura: () => {
        setLeitura(estadoInicial);
      },
    }),
    [leitura],
  );

  return <BibleReadingContext.Provider value={value}>{children}</BibleReadingContext.Provider>;
}

export function useBibleReading() {
  const context = useContext(BibleReadingContext);
  if (!context) {
    throw new Error('useBibleReading must be used within BibleReadingProvider');
  }
  return context;
}
