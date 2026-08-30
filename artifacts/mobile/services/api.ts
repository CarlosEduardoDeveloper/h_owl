/**
 * @deprecated Use `@/services/apiClient` e `@/config/env`.
 * Mantido apenas para compatibilidade com imports antigos.
 */
export { getApiHostUrl as apiBaseUrl } from '@/config/env';

export function configureApiClient(): void {
  // Credenciais e base URL são resolvidas em apiClient/env sob demanda.
}
