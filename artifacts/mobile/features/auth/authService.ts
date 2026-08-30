import { api } from '@/services/apiClient';

import type { LoginResponse, SessaoResponse } from '@/features/auth/types';

export async function login(usuario: string, senha: string): Promise<LoginResponse> {
  return api.post<LoginResponse>(
    '/auth/login',
    { usuario: usuario.trim().toLowerCase(), senha },
    { auth: false },
  );
}

export async function registrar(usuario: string, senha: string): Promise<LoginResponse> {
  return api.post<LoginResponse>(
    '/auth/registrar',
    { usuario: usuario.trim().toLowerCase(), senha },
    { auth: false },
  );
}

export async function buscarSessao(): Promise<SessaoResponse> {
  return api.get<SessaoResponse>('/auth/sessao');
}
