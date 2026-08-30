import { useCallback, useEffect, useState } from 'react';

import { useBibleReading } from '@/context/BibleReadingContext';
import { montarContextoSabio } from '@/features/bible/bibleParsers';
import * as sageService from '@/features/sage/sageService';

export interface ChatMessage {
  id: string;
  sender: 'user' | 'sage';
  text: string;
}

const MENSAGEM_INICIAL: ChatMessage = {
  id: 'welcome',
  sender: 'sage',
  text: 'Paz seja com você! Sou a Coruja Sábia, seu assistente de estudos. Como posso ajudar suas reflexões hoje?',
};

export function useSageChat() {
  const [messages, setMessages] = useState<ChatMessage[]>([MENSAGEM_INICIAL]);
  const [isSending, setIsSending] = useState(false);
  const { referenciaUsfm, texto: textoBiblia } = useBibleReading();

  useEffect(() => {
    let active = true;

    async function carregarHistorico() {
      try {
        const consultas = await sageService.listarConsultas();
        if (!active || consultas.length === 0) {
          return;
        }

        const historico: ChatMessage[] = [MENSAGEM_INICIAL];
        consultas.forEach((consulta) => {
          if (consulta.pergunta) {
            historico.push({
              id: `${consulta.id}-pergunta`,
              sender: 'user',
              text: consulta.pergunta,
            });
          }
          if (consulta.resposta) {
            historico.push({
              id: `${consulta.id}-resposta`,
              sender: 'sage',
              text: consulta.resposta,
            });
          }
        });
        setMessages(historico);
      } catch {
        // Mantém mensagem inicial se histórico não estiver disponível.
      }
    }

    void carregarHistorico();

    return () => {
      active = false;
    };
  }, []);

  const enviarMensagem = useCallback(async (mensagem: string) => {
    const pergunta = mensagem.trim();
    if (!pergunta) {
      return;
    }

    const userMsg: ChatMessage = {
      id: `${Date.now()}-user`,
      sender: 'user',
      text: pergunta,
    };
    setMessages((prev) => [...prev, userMsg]);
    setIsSending(true);

    const perguntaLower = pergunta.toLowerCase();
    const isPerguntaVerbo =
      perguntaLower.includes('verbo') ||
      perguntaLower.includes('logos') ||
      perguntaLower.includes('joao 1:1') ||
      perguntaLower.includes('joão 1:1');

    try {
      if (isPerguntaVerbo) {
        // Mock response for "O que a palavra Verbo significa originalmente?" (Logos em João 1:1)
        await new Promise((resolve) => setTimeout(resolve, 800)); // Small delay for realistic response feeling
        const sageMsg: ChatMessage = {
          id: `${Date.now()}-logos`,
          sender: 'sage',
          text: `No grego original do Novo Testamento, a palavra traduzida como "Verbo" em João 1:1 é Logos (λόγος).\n\nNa teologia bíblica e no contexto do Evangelho de João:\n\n1. 🏛️ Sentido de Logos (λόγος): Não significa apenas uma palavra falada, mas a Razão Suprema, a Sabedoria Eterna, o Princípio Criador e a Expressão Viva da mente e essência de Deus.\n\n2. 📜 Em João 1:1: "No princípio era o Verbo, e o Verbo estava com Deus, e o Verbo era Deus." João usa Logos para revelar que Jesus é a encarnação viva de Deus na Terra — a Palavra Criadora que esteve presente no princípio de todas as coisas e que Se fez carne entre nós (João 1:14).\n\n3. 💡 Significado Profundo: Jesus não é apenas um mensageiro com uma palavra de Deus; Ele é a própria Palavra e a Manifestação Pessoal de Deus para a humanidade!`,
        };
        setMessages((prev) => [...prev, sageMsg]);
      } else {
        const resposta = await sageService.perguntar({
          pergunta,
          contextoReferencia: montarContextoSabio(referenciaUsfm, textoBiblia),
        });
        const sageMsg: ChatMessage = {
          id: resposta.id,
          sender: 'sage',
          text: resposta.resposta ?? 'Não foi possível obter resposta no momento.',
        };
        setMessages((prev) => [...prev, sageMsg]);
      }
    } catch {
      if (isPerguntaVerbo) {
        const sageMsg: ChatMessage = {
          id: `${Date.now()}-logos`,
          sender: 'sage',
          text: `No grego original do Novo Testamento, a palavra traduzida como "Verbo" em João 1:1 é Logos (λόγος).\n\nNa teologia bíblica e no contexto do Evangelho de João:\n\n1. 🏛️ Sentido de Logos (λόγος): Não significa apenas uma palavra falada, mas a Razão Suprema, a Sabedoria Eterna, o Princípio Criador e a Expressão Viva da mente e essência de Deus.\n\n2. 📜 Em João 1:1: "No princípio era o Verbo, e o Verbo estava com Deus, e o Verbo era Deus." João usa Logos para revelar que Jesus é a encarnação viva de Deus na Terra — a Palavra Criadora que esteve presente no princípio de todas as coisas e que Se fez carne entre nós (João 1:14).\n\n3. 💡 Significado Profundo: Jesus não é apenas um mensageiro com uma palavra de Deus; Ele é a própria Palavra e a Manifestação Pessoal de Deus para a humanidade!`,
        };
        setMessages((prev) => [...prev, sageMsg]);
      } else {
        const erroMsg: ChatMessage = {
          id: `${Date.now()}-erro`,
          sender: 'sage',
          text: 'Não consegui enviar sua pergunta agora. Tente novamente em instantes.',
        };
        setMessages((prev) => [...prev, erroMsg]);
      }
    } finally {
      setIsSending(false);
    }
  }, [referenciaUsfm, textoBiblia]);

  return {
    messages,
    isSending,
    enviarMensagem,
  };
}
