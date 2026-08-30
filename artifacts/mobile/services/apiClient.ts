import { getApiBaseUrl } from '@/config/env';

type HttpMethod = 'GET' | 'POST' | 'PUT' | 'DELETE';

export interface ApiCredentials {
  usuario: string;
  senha: string;
}

export class ApiClientError extends Error {
  readonly status: number;
  readonly data: unknown;

  constructor(message: string, status: number, data?: unknown) {
    super(message);
    this.name = 'ApiClientError';
    this.status = status;
    this.data = data ?? null;
  }
}

let credentials: ApiCredentials | null = null;

export function setApiCredentials(next: ApiCredentials | null): void {
  credentials = next;
}

export function getApiCredentials(): ApiCredentials | null {
  return credentials;
}

function resolveErrorMessage(data: unknown, status: number): string {
  if (data && typeof data === 'object' && 'message' in data) {
    const message = (data as { message?: unknown }).message;
    if (typeof message === 'string' && message.trim()) {
      return message;
    }
  }
  return `Erro HTTP ${status}`;
}

async function parseBody(response: Response): Promise<unknown> {
  if (response.status === 204) {
    return null;
  }

  const text = await response.text();
  if (!text.trim()) {
    return null;
  }

  try {
    return JSON.parse(text);
  } catch {
    return text;
  }
}

export async function apiRequest<T>(
  method: HttpMethod,
  path: string,
  body?: unknown,
  options?: { auth?: boolean },
): Promise<T> {
  const requiresAuth = options?.auth ?? true;
  const headers: Record<string, string> = {
    Accept: 'application/json',
  };

  if (requiresAuth) {
    if (!credentials) {
      throw new ApiClientError('Credenciais não configuradas', 401);
    }
    headers['X-Usuario'] = credentials.usuario;
    headers['X-Senha'] = credentials.senha;
  }

  if (body !== undefined) {
    headers['Content-Type'] = 'application/json';
  }

  const normalizedPath = path.startsWith('/') ? path : `/${path}`;
  const url = `${getApiBaseUrl()}${normalizedPath}`;

  let response: Response;
  try {
    response = await fetch(url, {
      method,
      headers,
      body: body !== undefined ? JSON.stringify(body) : undefined,
    });
  } catch {
    throw new ApiClientError(
      'Sem conexão com o servidor. No Expo Go use o IP do PC (ex.: http://192.168.x.x:8080), não localhost.',
      0,
    );
  }

  const data = await parseBody(response);

  if (!response.ok) {
    throw new ApiClientError(resolveErrorMessage(data, response.status), response.status, data);
  }

  return data as T;
}

export const api = {
  get: <T>(path: string, options?: { auth?: boolean }) =>
    apiRequest<T>('GET', path, undefined, options),
  post: <T>(path: string, body?: unknown, options?: { auth?: boolean }) =>
    apiRequest<T>('POST', path, body, options),
  put: <T>(path: string, body?: unknown, options?: { auth?: boolean }) =>
    apiRequest<T>('PUT', path, body, options),
  delete: (path: string, options?: { auth?: boolean }) =>
    apiRequest<void>('DELETE', path, undefined, options),
};
