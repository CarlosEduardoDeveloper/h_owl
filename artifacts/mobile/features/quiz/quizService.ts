import { api } from '@/services/apiClient';

export interface QuizResumo {
  id: string;
  titulo?: string;
  descricao?: string;
  ordem?: number;
}

export interface AlternativaJogar {
  id: string;
  texto?: string;
  ordem?: number;
}

export interface QuestaoJogar {
  id: string;
  enunciado?: string;
  ordem?: number;
  alternativas: AlternativaJogar[];
}

export interface QuizJogar {
  id: string;
  titulo?: string;
  descricao?: string;
  questoes: QuestaoJogar[];
}

export interface IniciarTentativaQuizResponse {
  tentativaId: string;
  quizId: string;
  quiz: QuizJogar;
}

export interface RespostaQuizItem {
  questaoId: string;
  alternativaId: string;
}

export interface FinalizarTentativaQuizResponse {
  tentativaId: string;
  acertos: number;
  totalQuestoes: number;
  pontuacaoPercentual: number;
  biscoitoConcedido: boolean;
  saldoBiscoitos: number;
  streakAtual?: number;
  saudeFloresta?: string;
}

export async function listarQuizzes(): Promise<QuizResumo[]> {
  return api.get<QuizResumo[]>('/quizzes');
}

export async function iniciarTentativa(quizId: string): Promise<IniciarTentativaQuizResponse> {
  return api.post<IniciarTentativaQuizResponse>(`/quizzes/${quizId}/tentativas`);
}

export async function finalizarTentativa(
  tentativaId: string,
  respostas: RespostaQuizItem[],
): Promise<FinalizarTentativaQuizResponse> {
  return api.post<FinalizarTentativaQuizResponse>(`/tentativas-quiz/${tentativaId}/finalizar`, {
    respostas,
  });
}
