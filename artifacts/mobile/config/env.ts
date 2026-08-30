/**
 * Único ponto de configuração da URL do backend no mobile.
 * EXPO_PUBLIC_API_BASE_URL → base sem sufixo /api/v1
 */
export function getApiHostUrl(): string {
  const url = process.env.EXPO_PUBLIC_API_BASE_URL?.trim();
  if (!url) {
    throw new Error('EXPO_PUBLIC_API_BASE_URL não configurada');
  }
  return url.replace(/\/+$/, '');
}

export function getApiBaseUrl(): string {
  return `${getApiHostUrl()}/api/v1`;
}
