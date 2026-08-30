import { api } from '@/services/apiClient';

export interface ConsultaSabioResponse {
  id: string;
  pergunta?: string;
  resposta?: string;
  contextoReferencia?: string;
  usuarioId?: string;
  sessaoEstudoId?: string;
}

export interface PerguntarSabioInput {
  pergunta: string;
  sessaoEstudoId?: string;
  contextoReferencia?: string;
}

export async function perguntar(input: PerguntarSabioInput): Promise<ConsultaSabioResponse> {
  return api.post<ConsultaSabioResponse>('/sabio/consultas', input);
}

export async function listarConsultas(): Promise<ConsultaSabioResponse[]> {
  return api.get<ConsultaSabioResponse[]>('/me/consultas-sabio');
}
