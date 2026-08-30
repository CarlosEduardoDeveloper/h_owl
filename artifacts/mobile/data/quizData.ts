export interface QuizOption {
  key: 'A' | 'B' | 'C' | 'D';
  text: string;
}

export interface QuizQuestion {
  id: string;
  category: string;
  question: string;
  options: QuizOption[];
  correctOption: 'A' | 'B' | 'C' | 'D';
  explanation: string;
}

export interface QuizSet {
  topicId: string;
  title: string;
  summaryText?: string;
  questions: QuizQuestion[];
}

export const QUIZ_DATABASE: Record<string, QuizSet> = {
  'JHN_1': {
    topicId: 'JHN_1',
    title: 'Estudo Livre: João 1',
    summaryText:
      '📖 RESUMO DO CAPÍTULO JOÃO 1:\n\n• O Verbo Divino (Logos): No princípio era o Verbo, o Verbo estava com Deus e o Verbo era Deus. Nele estava a vida e a luz dos homens.\n• Testemunho de João Batista: João veio como testemunha da Luz, declarando: "Eis o Cordeiro de Deus que tira o pecado do mundo!"\n• O Verbo se fez Carne: O Verbo habitou entre nós, cheio de graça e de verdade (João 1:14).\n• Os Primeiros Discípulos: André, Simão Pedro, Filipe e Natanael passam a seguir Jesus ao reconhecerem nele o Messias.',
    questions: [
      {
        id: 'qjhn1_1',
        category: 'QUIZ DO SEU ESTUDO DIÁRIO — JOÃO 1',
        question:
          'Em João 1:1, qual termo grego original ("Logos") é traduzido como "Verbo", revelando a divindade e eternidade de Jesus Cristo?',
        options: [
          {
            key: 'A',
            text: 'Significa apenas uma palavra humana falada ocasionalmente por Deus.',
          },
          {
            key: 'B',
            text:
              'Refere-se ao "Logos" — a Razão Divina, Sabedoria Eterna e a própria encarnação de Deus no princípio com Ele.',
          },
          {
            key: 'C',
            text: 'Refere-se à lei escrita em tábuas de pedra enviada aos profetas.',
          },
        ],
        correctOption: 'B',
        explanation:
          'Em João 1:1, "No princípio era o Verbo (Logos), e o Verbo estava com Deus, e o Verbo era Deus".',
      },
      {
        id: 'qjhn1_2',
        category: 'QUIZ DO SEU ESTUDO DIÁRIO — JOÃO 1',
        question:
          'Em João 1:29, como João Batista proclama a missão salvadora de Jesus ao vê-lO aproximando-se?',
        options: [
          {
            key: 'A',
            text: '“Eis o Cordeiro de Deus, que tira o pecado do mundo!”',
          },
          {
            key: 'B',
            text: '“Eis o grande líder político de Israel!”',
          },
          {
            key: 'C',
            text: '“Eis um doutor da lei para julgar as nações!”',
          },
        ],
        correctOption: 'A',
        explanation:
          'Em João 1:29, João Batista exclama: "Eis o Cordeiro de Deus, que tira o pecado do mundo!"',
      },
      {
        id: 'qjhn1_3',
        category: 'QUIZ DO SEU ESTUDO DIÁRIO — JOÃO 1',
        question:
          'O que Jesus disse a Natanael em João 1:48 que o levou a declarar: "Rabi, tu és o Filho de Deus, tu és o Rei de Israel"?',
        options: [
          {
            key: 'A',
            text: 'Que sabia a sua profissão e sua cidade natal.',
          },
          {
            key: 'B',
            text:
              'Que o tinha visto quando estavas debaixo da figueira, antes de Filipe o chamar.',
          },
          {
            key: 'C',
            text: 'Que iria dar-lhe grandes riquezas materiais.',
          },
        ],
        correctOption: 'B',
        explanation:
          'Jesus respondeu: "Antes que Filipe te chamasse, eu te vi quando estavas debaixo da figueira" (João 1:48).',
      },
    ],
  },
  '1': {
    topicId: '1',
    title: 'Jesus, nosso socorro nas crises',
    questions: [
      {
        id: 'q1_1',
        category: 'QUIZ DO SEU ESTUDO DIÁRIO',
        question:
          'Segundo Jesus em Mateus 5:14–16, o que significa ser “a luz do mundo” e como devemos demonstrar essa luz em nossas atitudes?',
        options: [
          {
            key: 'A',
            text: 'Manter nossa fé em segredo para evitar julgamentos e conflitos com outras pessoas.',
          },
          {
            key: 'B',
            text:
              'Jesus ensina que nossas boas atitudes devem ser visíveis às pessoas, para que elas reconheçam a Deus e o glorifiquem.',
          },
          {
            key: 'C',
            text: 'Buscar reconhecimento e elogios das pessoas por nossas boas ações.',
          },
        ],
        correctOption: 'B',
        explanation:
          'Vossas boas obras devem resplandecer diante dos homens para que glorifiquem a vosso Pai que está nos céus.',
      },
      {
        id: 'q1_2',
        category: 'QUIZ DO SEU ESTUDO DIÁRIO',
        question:
          'Quando Pedro começou a afundar nas águas ao ir ao encontro de Jesus (Mateus 14:30-31), qual foi o motivo e a atitude de Jesus?',
        options: [
          {
            key: 'A',
            text: 'Pedro afundou porque olhou para o vento forte, e Jesus imediatamente estendeu a mão para socorrê-lo.',
          },
          {
            key: 'B',
            text: 'Pedro afundou por cansaço físico, e Jesus pediu para ele nadar de volta ao barco sozinho.',
          },
          {
            key: 'C',
            text: 'Jesus esperou Pedro submergir totalmente para depois ensiná-lo a nadar com fé.',
          },
        ],
        correctOption: 'A',
        explanation: 'Reparando no vento com medo, Pedro começou a afundar e clamou: "Senhor, salva-me!". Prontamente Jesus estendeu a mão.',
      },
    ],
  },
  '2': {
    topicId: '2',
    title: 'Amizades improváveis',
    questions: [
      {
        id: 'q2_1',
        category: 'QUIZ DO SEU ESTUDO DIÁRIO',
        question:
          'Qual lição principal sobre lealdade e aliança espiritual podemos aprender da amizade entre Davi e Jônatas em 1 Samuel 18?',
        options: [
          {
            key: 'A',
            text: 'Amizades verdadeiras buscam vantagem pessoal e privilégios de poder.',
          },
          {
            key: 'B',
            text:
              'O amor fraterno verdadeiro se baseia na renúncia, proteção mútua e fidelidade firmada perante Deus.',
          },
          {
            key: 'C',
            text: 'Devemos nos aproximar apenas de quem tem a mesma posição social que nós.',
          },
        ],
        correctOption: 'B',
        explanation: 'Jônatas fez uma aliança com Davi porque o amava como à sua própria alma.',
      },
    ],
  },
  '3': {
    topicId: '3',
    title: 'Relacionamento em Santidade',
    questions: [
      {
        id: 'q3_1',
        category: 'QUIZ DO SEU ESTUDO DIÁRIO',
        question:
          'Em 1 Tessalonicenses 4:3-5, qual é a instrução direta referente ao domínio próprio e respeito nos relacionamentos?',
        options: [
          {
            key: 'A',
            text: 'Viver em santificação e honra, mantendo o respeito mútuo e o domínio sobre as próprias vontades.',
          },
          {
            key: 'B',
            text: 'Ignorar princípios éticos se houver consentimento emocional mútuo.',
          },
          {
            key: 'C',
            text: 'Seguir apenas padrões culturais e sociais vigentes no momento.',
          },
        ],
        correctOption: 'A',
        explanation: 'Pois esta é a vontade de Deus: a vossa santificação e que cada um saiba possuir o seu vaso em santidade e honra.',
      },
    ],
  },
  '4': {
    topicId: '4',
    title: 'A Coragem vem da Fé',
    questions: [
      {
        id: 'q4_1',
        category: 'QUIZ DO SEU ESTUDO DIÁRIO',
        question:
          'Em Josué 1:9, onde se fundamenta a ordem divina de "sê forte e corajoso"?',
        options: [
          {
            key: 'A',
            text: 'Na autoconfiança de Josué e na sua superioridade militar.',
          },
          {
            key: 'B',
            text:
              'Na promessa incondicional da presença de Deus: "o Senhor teu Deus é contigo por onde quer que andares".',
          },
          {
            key: 'C',
            text: 'Na ausência total de perigos e obstáculos pela frente.',
          },
        ],
        correctOption: 'B',
        explanation: 'Não mo ordenei eu? Sê forte e corajoso; não pasmes, nem te espantes, porque o Senhor teu Deus é contigo.',
      },
    ],
  },
  DEFAULT: {
    topicId: 'JHN_1',
    title: 'Estudo Livre: João 1',
    summaryText:
      '📖 RESUMO DO CAPÍTULO JOÃO 1:\n\n• O Verbo Divino (Logos): No princípio era o Verbo, o Verbo estava com Deus e o Verbo era Deus. Nele estava a vida e a luz dos homens.\n• Testemunho de João Batista: João veio como testemunha da Luz, declarando: "Eis o Cordeiro de Deus que tira o pecado do mundo!"\n• O Verbo se fez Carne: O Verbo habitou entre nós, cheio de graça e de verdade (João 1:14).\n• Os Primeiros Discípulos: André, Simão Pedro, Filipe e Natanael passam a seguir Jesus ao reconhecerem nele o Messias.',
    questions: [
      {
        id: 'qjhn1_1',
        category: 'QUIZ DO SEU ESTUDO DIÁRIO — JOÃO 1',
        question:
          'Em João 1:1, qual termo grego original ("Logos") é traduzido como "Verbo", revelando a divindade e eternidade de Jesus Cristo?',
        options: [
          {
            key: 'A',
            text: 'Significa apenas uma palavra humana falada ocasionalmente por Deus.',
          },
          {
            key: 'B',
            text:
              'Refere-se ao "Logos" — a Razão Divina, Sabedoria Eterna e a própria encarnação de Deus no princípio com Ele.',
          },
          {
            key: 'C',
            text: 'Refere-se à lei escrita em tábuas de pedra enviada aos profetas.',
          },
        ],
        correctOption: 'B',
        explanation:
          'Em João 1:1, "No princípio era o Verbo (Logos), e o Verbo estava com Deus, e o Verbo era Deus".',
      },
      {
        id: 'qjhn1_2',
        category: 'QUIZ DO SEU ESTUDO DIÁRIO — JOÃO 1',
        question:
          'Em João 1:29, como João Batista proclama a missão salvadora de Jesus ao vê-lO aproximando-se?',
        options: [
          {
            key: 'A',
            text: '“Eis o Cordeiro de Deus, que tira o pecado do mundo!”',
          },
          {
            key: 'B',
            text: '“Eis o grande líder político de Israel!”',
          },
          {
            key: 'C',
            text: '“Eis um doutor da lei para julgar as nações!”',
          },
        ],
        correctOption: 'A',
        explanation:
          'Em João 1:29, João Batista exclama: "Eis o Cordeiro de Deus, que tira o pecado do mundo!"',
      },
      {
        id: 'qjhn1_3',
        category: 'QUIZ DO SEU ESTUDO DIÁRIO — JOÃO 1',
        question:
          'O que Jesus disse a Natanael em João 1:48 que o levou a declarar: "Rabi, tu és o Filho de Deus, tu és o Rei de Israel"?',
        options: [
          {
            key: 'A',
            text: 'Que sabia a sua profissão e sua cidade natal.',
          },
          {
            key: 'B',
            text:
              'Que o tinha visto quando estavas debaixo da figueira, antes de Filipe o chamar.',
          },
          {
            key: 'C',
            text: 'Que iria dar-lhe grandes riquezas materiais.',
          },
        ],
        correctOption: 'B',
        explanation:
          'Jesus respondeu: "Antes que Filipe te chamasse, eu te vi quando estavas debaixo da figueira" (João 1:48).',
      },
    ],
  },
};

export function getQuizByTopicId(topicId?: string): QuizSet {
  if (!topicId || !QUIZ_DATABASE[topicId]) {
    return QUIZ_DATABASE.JHN_1;
  }
  return QUIZ_DATABASE[topicId];
}
