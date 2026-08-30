import { api } from '@/services/apiClient';

export interface MeTrilhaProgresso {
  trilhaId?: string;
  titulo?: string;
  progressoPercentual?: number;
}

export interface MeResumo {
  usuarioId: string;
  usuario: string;
  ofensiva?: number | null;
  xpDiario?: number | null;
  ranking?: number | null;
  viveiro?: {
    id: string;
    nome?: string;
    nivel?: number;
    xpTotal?: number;
  } | null;
  ovoAtivo?: {
    id: string;
    status?: string;
  } | null;
  sessaoAtual?: {
    id: string;
    status?: string;
    intencao?: string;
  } | null;
  trilhasEmProgresso: MeTrilhaProgresso[];
}

export async function getResumo(): Promise<MeResumo> {
  return api.get<MeResumo>('/me/resumo');
}
