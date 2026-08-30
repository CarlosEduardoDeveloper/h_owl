import { api } from '@/services/apiClient';

export interface SessaoEstudoResponse {
  id: string;
  intencao?: string;
  modoFoco?: string;
  duracaoPlanejadaMinutos?: number;
  duracaoRealMinutos?: number;
  inicioEm?: string;
  fimEm?: string;
  status?: string;
  usuarioId?: string;
}

export interface CriarSessaoEstudoInput {
  usuarioId: string;
  intencao: 'LEITURA_LIVRE' | 'TRILHA' | 'REVISAO';
  modoFoco?: 'ESTRITO' | 'FLEXIVEL';
  duracaoPlanejadaMinutos?: number;
  status?: string;
}

export async function criarSessao(input: CriarSessaoEstudoInput): Promise<SessaoEstudoResponse> {
  return api.post<SessaoEstudoResponse>('/sessoes-estudo', input);
}

export async function iniciarSessao(id: string): Promise<SessaoEstudoResponse> {
  return api.post<SessaoEstudoResponse>(`/sessoes-estudo/${id}/iniciar`);
}

export async function concluirSessao(
  id: string,
  duracaoRealMinutos?: number,
): Promise<SessaoEstudoResponse> {
  return api.post<SessaoEstudoResponse>(`/sessoes-estudo/${id}/concluir`, {
    duracaoRealMinutos,
  });
}

export async function interromperSessao(id: string): Promise<SessaoEstudoResponse> {
  return api.post<SessaoEstudoResponse>(`/sessoes-estudo/${id}/interromper`);
}
