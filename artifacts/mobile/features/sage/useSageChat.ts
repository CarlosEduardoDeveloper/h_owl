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

    try {
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
    } catch {
      const erroMsg: ChatMessage = {
        id: `${Date.now()}-erro`,
        sender: 'sage',
        text: 'Não consegui enviar sua pergunta agora. Tente novamente em instantes.',
      };
      setMessages((prev) => [...prev, erroMsg]);
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
