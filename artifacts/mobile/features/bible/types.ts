export interface BibliaIdioma {
  iso6391?: string;
  nome?: string;
}

export interface BibliaResumo {
  id?: number;
  abreviacao?: string;
  titulo?: string;
  idioma?: BibliaIdioma;
}

export interface BibliasPaginadas {
  dados: BibliaResumo[];
  proximoPageToken?: string | null;
}

export interface BibliaDetalhe {
  dados: unknown;
}

export interface BibleLeituraAtual {
  bibleId?: number;
  referenciaUsfm?: string;
  tituloBiblia?: string;
  texto?: string;
  isLoading: boolean;
}
