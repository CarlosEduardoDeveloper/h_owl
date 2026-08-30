import React, { useState } from 'react';
import {
  FlatList,
  KeyboardAvoidingView,
  Platform,
  Pressable,
  StyleSheet,
  Text,
  TextInput,
  View,
} from 'react-native';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import { Feather } from '@expo/vector-icons';

interface Message {
  id: string;
  sender: 'user' | 'sage';
  text: string;
}

export default function ChatScreen() {
  const insets = useSafeAreaInsets();
  const [inputText, setInputText] = useState('');
  const [messages, setMessages] = useState<Message[]>([
    {
      id: '1',
      sender: 'sage',
      text: 'Paz seja com você! Sou a Coruja Sábia, seu assistente de estudos. Como posso ajudar suas reflexões hoje?',
    },
  ]);

  const handleSend = () => {
    if (!inputText.trim()) return;

    const userMsg: Message = {
      id: Date.now().toString(),
      sender: 'user',
      text: inputText.trim(),
    };

    setMessages((prev) => [...prev, userMsg]);
    setInputText('');

    // Simulate Sage AI response
    setTimeout(() => {
      const sageMsg: Message = {
        id: (Date.now() + 1).toString(),
        sender: 'sage',
        text: 'Excelente reflexão! Esse conceito bíblico conecta-se ao contexto histórico e ao significado espiritual original.',
      };
      setMessages((prev) => [...prev, sageMsg]);
    }, 1000);
  };

  return (
    <View style={[styles.container, { paddingTop: insets.top + 16 }]}>
      {/* Header */}
      <View style={styles.header}>
        <View style={styles.avatarCircle}>
          <Feather name="book-open" size={20} color="#FFFFFF" />
        </View>
        <View>
          <Text style={styles.headerTitle}>Sábio HOWL</Text>
          <Text style={styles.headerSubtitle}>Assistente de Estudos</Text>
        </View>
      </View>

      {/* Messages List */}
      <KeyboardAvoidingView
        style={{ flex: 1 }}
        behavior={Platform.OS === 'ios' ? 'padding' : undefined}
        keyboardVerticalOffset={Platform.OS === 'ios' ? 90 : 0}
      >
        <FlatList
          data={messages}
          keyExtractor={(item) => item.id}
          contentContainerStyle={styles.messagesContainer}
          renderItem={({ item }) => (
            <View
              style={[
                styles.messageBubble,
                item.sender === 'user' ? styles.userBubble : styles.sageBubble,
              ]}
            >
              <Text
                style={[
                  styles.messageText,
                  item.sender === 'user' ? styles.userText : styles.sageText,
                ]}
              >
                {item.text}
              </Text>
            </View>
          )}
        />

        {/* Input Bar */}
        <View style={[styles.inputBar, { paddingBottom: Math.max(insets.bottom, 90) }]}>
          <TextInput
            style={styles.input}
            value={inputText}
            onChangeText={setInputText}
            placeholder="Pergunte ao Sábio..."
            placeholderTextColor="#A4998E"
          />
          <Pressable
            style={({ pressed }) => [
              styles.sendButton,
              { opacity: pressed ? 0.8 : 1 },
            ]}
            onPress={handleSend}
          >
            <Feather name="send" size={18} color="#FFFFFF" />
          </Pressable>
        </View>
      </KeyboardAvoidingView>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#FFF6E5',
  },
  header: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: 20,
    paddingBottom: 16,
    borderBottomWidth: 1,
    borderBottomColor: '#E8DFC9',
    gap: 12,
  },
  avatarCircle: {
    width: 42,
    height: 42,
    borderRadius: 21,
    backgroundColor: '#046865',
    alignItems: 'center',
    justifyContent: 'center',
  },
  headerTitle: {
    fontSize: 18,
    fontWeight: '800',
    color: '#1E1B38',
  },
  headerSubtitle: {
    fontSize: 12,
    color: '#8C7C6D',
    fontWeight: '500',
  },
  messagesContainer: {
    paddingHorizontal: 20,
    paddingVertical: 16,
    gap: 12,
  },
  messageBubble: {
    maxWidth: '82%',
    padding: 14,
    borderRadius: 20,
  },
  sageBubble: {
    alignSelf: 'flex-start',
    backgroundColor: '#FFFFFF',
    borderTopLeftRadius: 4,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.04,
    shadowRadius: 6,
    elevation: 2,
  },
  userBubble: {
    alignSelf: 'flex-end',
    backgroundColor: '#ED5B0A',
    borderTopRightRadius: 4,
  },
  messageText: {
    fontSize: 15,
    lineHeight: 21,
  },
  sageText: {
    color: '#1E1B38',
    fontWeight: '500',
  },
  userText: {
    color: '#FFFFFF',
    fontWeight: '600',
  },
  inputBar: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: 20,
    paddingTop: 12,
    gap: 10,
    backgroundColor: '#FFF6E5',
    borderTopWidth: 1,
    borderTopColor: '#E8DFC9',
  },
  input: {
    flex: 1,
    backgroundColor: '#FFFFFF',
    borderRadius: 24,
    height: 48,
    paddingHorizontal: 18,
    fontSize: 15,
    color: '#1E1B38',
  },
  sendButton: {
    width: 48,
    height: 48,
    borderRadius: 24,
    backgroundColor: '#ED5B0A',
    alignItems: 'center',
    justifyContent: 'center',
  },
});
