import { useQuery } from '@tanstack/react-query';

import * as bibleService from '@/features/bible/bibleService';

export function useBiblias(idioma = 'por') {
  return useQuery({
    queryKey: ['biblias', idioma],
    queryFn: () => bibleService.listarBiblias({ idioma, pageSize: 25 }),
  });
}

export function usePassagemBiblica(bibleId?: number, referenciaUsfm?: string) {
  return useQuery({
    queryKey: ['biblia', 'passagem', bibleId, referenciaUsfm],
    queryFn: () => bibleService.buscarPassagem(bibleId!, referenciaUsfm!),
    enabled: bibleId != null && !!referenciaUsfm,
  });
}

export function useVersiculosCapitulo(bibleId?: number, livroUsfm?: string, capitulo?: number) {
  return useQuery({
    queryKey: ['biblia', 'versiculos', bibleId, livroUsfm, capitulo],
    queryFn: () => bibleService.listarVersiculosCapitulo(bibleId!, livroUsfm!, capitulo!),
    enabled: bibleId != null && !!livroUsfm && capitulo != null,
  });
}
