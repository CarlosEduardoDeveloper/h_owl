import AsyncStorage from '@react-native-async-storage/async-storage';

const ACTIVE_SESSION_KEY = '@corujas/study/active-session-id';

export async function saveActiveSessionId(sessionId: string): Promise<void> {
  await AsyncStorage.setItem(ACTIVE_SESSION_KEY, sessionId);
}

export async function loadActiveSessionId(): Promise<string | null> {
  return AsyncStorage.getItem(ACTIVE_SESSION_KEY);
}

export async function clearActiveSessionId(): Promise<void> {
  await AsyncStorage.removeItem(ACTIVE_SESSION_KEY);
}
