import { useEffect, useState } from 'react';
import { useLocalSearchParams } from 'expo-router';

import { useBibleReading } from '@/context/BibleReadingContext';
import { getPreferredBibleId, setPreferredBibleId } from '@/features/bible/biblePreferences';
import { extrairReferenciaUsfm, extrairTextoPassagem } from '@/features/bible/bibleParsers';
import { useBiblias, usePassagemBiblica } from '@/features/bible/useBiblias';

const REFERENCIA_PADRAO_LIVRE = 'JHN.3.16';
const REFERENCIA_PADRAO_TRILHA = 'MAT.1.1';

function resolverReferencia(intencao?: string, referenciaParam?: string): string {
  if (referenciaParam?.trim()) {
    return referenciaParam.trim();
  }
  if (intencao === 'TRILHA') {
    return REFERENCIA_PADRAO_TRILHA;
  }
  return REFERENCIA_PADRAO_LIVRE;
}

export function useBibleReader() {
  const params = useLocalSearchParams<{
    intencao?: string | string[];
    referenciaUsfm?: string | string[];
  }>();
  const { definirLeitura } = useBibleReading();

  const intencao = Array.isArray(params.intencao) ? params.intencao[0] : params.intencao;
  const referenciaParam = Array.isArray(params.referenciaUsfm)
    ? params.referenciaUsfm[0]
    : params.referenciaUsfm;
  const referenciaUsfm = resolverReferencia(intencao, referenciaParam);

  const bibliasQuery = useBiblias('por');
  const [bibleId, setBibleId] = useState<number | undefined>();

  useEffect(() => {
    let active = true;

    async function resolverBibleId() {
      const lista = bibliasQuery.data?.dados ?? [];
      if (lista.length === 0) {
        return;
      }

      const preferido = await getPreferredBibleId();
      const existePreferido = preferido != null && lista.some((item) => item.id === preferido);
      const selecionado = existePreferido ? preferido! : lista[0]?.id;

      if (!selecionado || !active) {
        return;
      }

      if (!existePreferido) {
        await setPreferredBibleId(selecionado);
      }

      setBibleId(selecionado);
    }

    if (bibliasQuery.data) {
      void resolverBibleId();
    }

    return () => {
      active = false;
    };
  }, [bibliasQuery.data]);

  const passagemQuery = usePassagemBiblica(bibleId, referenciaUsfm);

  const tituloBiblia = bibliasQuery.data?.dados?.find((item) => item.id === bibleId)?.titulo;
  const texto = extrairTextoPassagem(passagemQuery.data?.dados);
  const referenciaDetalhe = extrairReferenciaUsfm(passagemQuery.data?.dados) ?? referenciaUsfm;
  const isLoading = bibliasQuery.isLoading || passagemQuery.isLoading;

  useEffect(() => {
    definirLeitura({
      bibleId,
      referenciaUsfm: referenciaDetalhe,
      tituloBiblia,
      texto: texto || undefined,
      isLoading,
    });
  }, [bibleId, definirLeitura, isLoading, referenciaDetalhe, texto, tituloBiblia]);

  return {
    bibleId,
    referenciaUsfm: referenciaDetalhe,
    tituloBiblia,
    texto,
    isLoading,
    isError: bibliasQuery.isError || passagemQuery.isError,
  };
}
