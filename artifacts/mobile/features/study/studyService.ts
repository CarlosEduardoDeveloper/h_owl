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

export interface GamificacaoSessaoResponse {
  ovoId?: string;
  corujaUsuarioId?: string;
  corujaNome?: string;
  poleiroIndice?: number;
  biscoitoConcedido?: boolean;
  saldoBiscoitos?: number;
  streakAtual?: number;
  saudeFloresta?: 'NORMAL' | 'AMARELA' | 'CINZA' | 'SUJA';
}

export interface SessaoEstudoConclusaoResponse extends SessaoEstudoResponse {
  gamificacao?: GamificacaoSessaoResponse;
}

export interface CriarSessaoEstudoInput {
  usuarioId: string;
  intencao: 'LEITURA_LIVRE' | 'TRILHA' | 'REVISAO' | 'QUIZ';
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
): Promise<SessaoEstudoConclusaoResponse> {
  return api.post<SessaoEstudoConclusaoResponse>(`/sessoes-estudo/${id}/concluir`, {
    duracaoRealMinutos,
  });
}

export async function interromperSessao(id: string): Promise<SessaoEstudoResponse> {
  return api.post<SessaoEstudoResponse>(`/sessoes-estudo/${id}/interromper`);
}
