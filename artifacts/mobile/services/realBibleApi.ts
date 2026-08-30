export interface RealBibleVerse {
  book_id: string;
  book_name: string;
  chapter: number;
  verse: number;
  text: string;
}

export interface RealBibleChapterResponse {
  reference: string;
  verses: RealBibleVerse[];
  text: string;
  translation_id: string;
  translation_name: string;
}

/**
 * Fetches real, authentic Portuguese Bible text for any book and chapter directly from public Bible API
 */
export async function fetchRealBibleChapter(
  bookName: string,
  chapter: number,
): Promise<RealBibleVerse[]> {
  try {
    const formattedQuery = `${bookName} ${chapter}`;
    const url = `https://bible-api.com/${encodeURIComponent(formattedQuery)}?translation=almeida`;

    const res = await fetch(url);
    if (!res.ok) {
      throw new Error(`HTTP Error ${res.status}`);
    }

    const data: RealBibleChapterResponse = await res.json();
    if (data && Array.isArray(data.verses) && data.verses.length > 0) {
      return data.verses.map((v) => ({
        book_id: v.book_id,
        book_name: v.book_name,
        chapter: v.chapter,
        verse: v.verse,
        text: v.text.trim(),
      }));
    }

    return [];
  } catch (error) {
    console.warn(`[realBibleApi] Could not fetch ${bookName} ${chapter} from API:`, error);
    return [];
  }
}
