export function extrairTextoPassagem(dados: unknown): string {
  if (dados == null) {
    return '';
  }

  if (typeof dados === 'string') {
    return dados.trim();
  }

  if (typeof dados !== 'object') {
    return '';
  }

  const node = dados as Record<string, unknown>;

  if (typeof node.content === 'string' && node.content.trim()) {
    return node.content.trim();
  }

  if (typeof node.text === 'string' && node.text.trim()) {
    return node.text.trim();
  }

  const passage = node.passage;
  if (passage && typeof passage === 'object') {
    const textoPassagem = extrairTextoPassagem(passage);
    if (textoPassagem) {
      return textoPassagem;
    }
  }

  if (Array.isArray(node.verses)) {
    const linhas = node.verses
      .map((versiculo) => formatarVersiculo(versiculo))
      .filter((linha) => linha.length > 0);
    if (linhas.length > 0) {
      return linhas.join('\n');
    }
  }

  if (Array.isArray(node.data)) {
    const linhas = node.data
      .map((versiculo) => formatarVersiculo(versiculo))
      .filter((linha) => linha.length > 0);
    if (linhas.length > 0) {
      return linhas.join('\n');
    }
  }

  if (Array.isArray(dados)) {
    const linhas = dados
      .map((versiculo) => formatarVersiculo(versiculo))
      .filter((linha) => linha.length > 0);
    return linhas.join('\n');
  }

  return '';
}

export function extrairReferenciaUsfm(dados: unknown): string | undefined {
  if (!dados || typeof dados !== 'object') {
    return undefined;
  }

  const node = dados as Record<string, unknown>;
  if (typeof node.usfm === 'string') {
    return node.usfm;
  }
  if (typeof node.reference === 'string') {
    return node.reference;
  }
  if (typeof node.human === 'string') {
    return node.human;
  }
  return undefined;
}

function formatarVersiculo(versiculo: unknown): string {
  if (!versiculo || typeof versiculo !== 'object') {
    return '';
  }

  const node = versiculo as Record<string, unknown>;
  const numero = node.verse ?? node.number ?? node.verse_number;
  const texto =
    (typeof node.content === 'string' && node.content) ||
    (typeof node.text === 'string' && node.text) ||
    '';

  if (!texto.trim()) {
    return '';
  }

  if (numero != null && `${numero}`.trim()) {
    return `${numero}. ${texto.trim()}`;
  }

  return texto.trim();
}

export function montarContextoSabio(referenciaUsfm: string | undefined, texto: string | undefined): string | undefined {
  if (!referenciaUsfm && !texto) {
    return undefined;
  }
  if (!texto) {
    return referenciaUsfm;
  }
  const trecho = texto.length > 800 ? `${texto.slice(0, 800)}…` : texto;
  return referenciaUsfm ? `${referenciaUsfm}: ${trecho}` : trecho;
}
