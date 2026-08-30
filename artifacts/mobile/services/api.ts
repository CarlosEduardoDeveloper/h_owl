import { setBaseUrl } from '@workspace/api-client-react';

const configuredBaseUrl = process.env.EXPO_PUBLIC_API_BASE_URL?.trim();

export const apiBaseUrl =
  configuredBaseUrl && configuredBaseUrl.length > 0
    ? configuredBaseUrl.replace(/\/+$/, '')
    : 'http://localhost:8080';

export function configureApiClient(): void {
  setBaseUrl(apiBaseUrl);
}