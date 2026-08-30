import AsyncStorage from '@react-native-async-storage/async-storage';

const BIBLE_ID_KEY = '@corujas/bible/preferred-id';

export async function getPreferredBibleId(): Promise<number | null> {
  const raw = await AsyncStorage.getItem(BIBLE_ID_KEY);
  if (!raw) {
    return null;
  }
  const parsed = Number(raw);
  return Number.isFinite(parsed) ? parsed : null;
}

export async function setPreferredBibleId(bibleId: number): Promise<void> {
  await AsyncStorage.setItem(BIBLE_ID_KEY, String(bibleId));
}
