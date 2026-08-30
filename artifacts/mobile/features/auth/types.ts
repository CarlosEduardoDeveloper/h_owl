export interface LoginResponse {
  usuarioId: string;
  usuario: string;
}

export interface SessaoResponse {
  usuarioId: string;
  usuario: string;
  status: string;
}

export interface StoredSession {
  usuarioId: string;
  usuario: string;
  senha: string;
  displayName?: string;
}

export interface UserProfile {
  usuarioId: string;
  usuario: string;
  displayName: string;
  avatarInitials: string;
}
