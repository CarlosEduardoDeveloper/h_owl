import { api } from '@/services/apiClient';

import type { BibliaDetalhe, BibliasPaginadas } from '@/features/bible/types';

export interface ListarBibliasParams {
  idioma?: string;
  pageSize?: number;
  pageToken?: string;
}

export async function listarBiblias(params: ListarBibliasParams = {}): Promise<BibliasPaginadas> {
  const searchParams = new URLSearchParams();
  if (params.idioma) {
    searchParams.set('idioma', params.idioma);
  }
  if (params.pageSize != null) {
    searchParams.set('pageSize', String(params.pageSize));
  }
  if (params.pageToken) {
    searchParams.set('pageToken', params.pageToken);
  }

  const query = searchParams.toString();
  const path = query ? `/biblias?${query}` : '/biblias';
  return api.get<BibliasPaginadas>(path);
}

export async function buscarBiblia(bibleId: number): Promise<BibliaDetalhe> {
  return api.get<BibliaDetalhe>(`/biblias/${bibleId}`);
}

export async function listarLivros(bibleId: number): Promise<BibliaDetalhe> {
  return api.get<BibliaDetalhe>(`/biblias/${bibleId}/livros`);
}

export async function buscarPassagem(bibleId: number, referenciaUsfm: string): Promise<BibliaDetalhe> {
  return api.get<BibliaDetalhe>(`/biblias/${bibleId}/passagens/${encodeURIComponent(referenciaUsfm)}`);
}

export async function listarVersiculosCapitulo(
  bibleId: number,
  livroUsfm: string,
  capitulo: number,
): Promise<BibliaDetalhe> {
  return api.get<BibliaDetalhe>(
    `/biblias/${bibleId}/livros/${encodeURIComponent(livroUsfm)}/capitulos/${capitulo}/versiculos`,
  );
}
