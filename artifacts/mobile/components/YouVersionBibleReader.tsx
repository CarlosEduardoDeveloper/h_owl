import React, { useEffect, useRef, useState } from 'react';
import {
  ActivityIndicator,
  FlatList,
  Modal,
  Pressable,
  ScrollView,
  StyleSheet,
  Text,
  TextInput,
  View,
} from 'react-native';
import { Feather } from '@expo/vector-icons';
import {
  BIBLE_BOOKS,
  BibleBook,
  getVersesForChapter,
  VerseContent,
} from '@/data/bibleData';
import { fetchRealBibleChapter } from '@/services/realBibleApi';

interface YouVersionBibleReaderProps {
  initialBookId?: string;
  initialChapter?: number;
  initialVerse?: number;
}

type ModalStep = 'LIVRO' | 'CAPITULO' | 'VERSICULO';

export function YouVersionBibleReader({
  initialBookId = 'JHN',
  initialChapter = 3,
  initialVerse,
}: YouVersionBibleReaderProps) {
  const [currentBook, setCurrentBook] = useState<BibleBook>(
    () => BIBLE_BOOKS.find((b) => b.id === initialBookId) || BIBLE_BOOKS[39], // João
  );
  const [currentChapter, setCurrentChapter] = useState<number>(initialChapter);
  const [selectedVerseNumber, setSelectedVerseNumber] = useState<number | null>(
    initialVerse ?? null,
  );
  const [selectedVersion, setSelectedVersion] = useState<string>('NVI');

  // Real Bible API state
  const [verses, setVerses] = useState<VerseContent[]>([]);
  const [isLoadingApi, setIsLoadingApi] = useState<boolean>(true);

  // Modal State
  const [showSelectorModal, setShowSelectorModal] = useState(false);
  const [modalStep, setModalStep] = useState<ModalStep>('LIVRO');
  const [selectedTestament, setSelectedTestament] = useState<'AT' | 'NT'>('NT');
  const [searchQuery, setSearchQuery] = useState('');

  // Temporary selection state in modal
  const [tempBook, setTempBook] = useState<BibleBook>(currentBook);
  const [tempChapter, setTempChapter] = useState<number>(currentChapter);
  const [tempVerses, setTempVerses] = useState<VerseContent[]>([]);

  const scrollViewRef = useRef<ScrollView>(null);

  // Fetch real Bible chapter verses whenever currentBook or currentChapter changes
  useEffect(() => {
    let active = true;

    async function loadChapterText() {
      setIsLoadingApi(true);
      const apiResults = await fetchRealBibleChapter(currentBook.name, currentChapter);

      if (!active) return;

      if (apiResults.length > 0) {
        setVerses(apiResults.map((v) => ({ number: v.verse, text: v.text })));
      } else {
        // Fallback to local dataset
        const fallback = getVersesForChapter(currentBook.id, currentChapter);
        setVerses(fallback);
      }
      setIsLoadingApi(false);
    }

    void loadChapterText();

    return () => {
      active = false;
    };
  }, [currentBook.name, currentBook.id, currentChapter]);

  // Load verses for modal temp selection
  useEffect(() => {
    let active = true;

    async function loadTempVerses() {
      const results = await fetchRealBibleChapter(tempBook.name, tempChapter);
      if (!active) return;

      if (results.length > 0) {
        setTempVerses(results.map((v) => ({ number: v.verse, text: v.text })));
      } else {
        setTempVerses(getVersesForChapter(tempBook.id, tempChapter));
      }
    }

    void loadTempVerses();

    return () => {
      active = false;
    };
  }, [tempBook.name, tempBook.id, tempChapter]);

  // Filter books for modal
  const filteredBooks = BIBLE_BOOKS.filter(
    (b) =>
      b.testament === selectedTestament &&
      b.name.toLowerCase().includes(searchQuery.trim().toLowerCase()),
  );

  // Chapter navigation
  const handleNextChapter = () => {
    if (currentChapter < currentBook.chaptersCount) {
      setCurrentChapter((prev) => prev + 1);
      setSelectedVerseNumber(null);
    } else {
      const currentIndex = BIBLE_BOOKS.findIndex((b) => b.id === currentBook.id);
      if (currentIndex < BIBLE_BOOKS.length - 1) {
        const nextBook = BIBLE_BOOKS[currentIndex + 1];
        setCurrentBook(nextBook);
        setCurrentChapter(1);
        setSelectedVerseNumber(null);
      }
    }
  };

  const handlePrevChapter = () => {
    if (currentChapter > 1) {
      setCurrentChapter((prev) => prev - 1);
      setSelectedVerseNumber(null);
    } else {
      const currentIndex = BIBLE_BOOKS.findIndex((b) => b.id === currentBook.id);
      if (currentIndex > 0) {
        const prevBook = BIBLE_BOOKS[currentIndex - 1];
        setCurrentBook(prevBook);
        setCurrentChapter(prevBook.chaptersCount);
        setSelectedVerseNumber(null);
      }
    }
  };

  // Open modal
  const handleOpenSelector = () => {
    setTempBook(currentBook);
    setTempChapter(currentChapter);
    setSelectedTestament(currentBook.testament);
    setSearchQuery('');
    setModalStep('LIVRO');
    setShowSelectorModal(true);
  };

  // Select Book step
  const handleSelectBook = (book: BibleBook) => {
    setTempBook(book);
    setTempChapter(1);
    setModalStep('CAPITULO');
  };

  // Select Chapter step
  const handleSelectChapter = (chap: number) => {
    setTempChapter(chap);
    setModalStep('VERSICULO');
  };

  // Select Verse step -> Finish Selection
  const handleSelectVerse = (verseNum: number) => {
    setCurrentBook(tempBook);
    setCurrentChapter(tempChapter);
    setSelectedVerseNumber(verseNum);
    setShowSelectorModal(false);
  };

  // Confirm whole chapter selection
  const handleConfirmWholeChapter = () => {
    setCurrentBook(tempBook);
    setCurrentChapter(tempChapter);
    setSelectedVerseNumber(null);
    setShowSelectorModal(false);
  };

  return (
    <View style={styles.container}>
      {/* Bible Navigation Header Bar */}
      <View style={styles.headerBar}>
        {/* Previous Chapter */}
        <Pressable
          style={({ pressed }) => [
            styles.navButton,
            { opacity: pressed ? 0.7 : 1 },
          ]}
          onPress={handlePrevChapter}
        >
          <Feather name="chevron-left" size={20} color="#0F7B6C" />
        </Pressable>

        {/* Book & Chapter & Verse Trigger Button */}
        <Pressable
          style={({ pressed }) => [
            styles.titleSelectorTrigger,
            { opacity: pressed ? 0.8 : 1 },
          ]}
          onPress={handleOpenSelector}
        >
          <Feather name="book-open" size={16} color="#ED5B0A" />
          <Text style={styles.titleText}>
            {currentBook.name} {currentChapter}
            {selectedVerseNumber ? `:${selectedVerseNumber}` : ''}
          </Text>
          <Feather name="chevron-down" size={16} color="#ED5B0A" />
        </Pressable>

        {/* Next Chapter */}
        <Pressable
          style={({ pressed }) => [
            styles.navButton,
            { opacity: pressed ? 0.7 : 1 },
          ]}
          onPress={handleNextChapter}
        >
          <Feather name="chevron-right" size={20} color="#0F7B6C" />
        </Pressable>
      </View>

      {/* Main Scripture Display Surface */}
      <View style={styles.scriptureCard}>
        {/* Badge Header */}
        <View style={styles.cardBadgeHeader}>
          <View style={styles.badgeGroup}>
            <View style={styles.yvBadge}>
              <Text style={styles.yvBadgeText}>BÍBLIA SAGRADA (ALMEIDA)</Text>
            </View>
            <View style={styles.usfmBadge}>
              <Text style={styles.usfmBadgeText}>
                {currentBook.id}.{currentChapter}
                {selectedVerseNumber ? `.${selectedVerseNumber}` : ''}
              </Text>
            </View>
          </View>

          {/* Version Selector (AA, NVI, NVT) */}
          <View style={styles.versionSelectorRow}>
            {['AA', 'NVI', 'NVT'].map((v) => (
              <Pressable
                key={v}
                style={[
                  styles.versionPill,
                  selectedVersion === v && styles.versionPillActive,
                ]}
                onPress={() => setSelectedVersion(v)}
              >
                <Text
                  style={[
                    styles.versionPillText,
                    selectedVersion === v && styles.versionPillTextActive,
                  ]}
                >
                  {v}
                </Text>
              </Pressable>
            ))}
          </View>
        </View>

        {/* Loading Indicator or Verses Scroll List */}
        {isLoadingApi ? (
          <View style={styles.loadingBox}>
            <ActivityIndicator size="small" color="#ED5B0A" />
            <Text style={styles.loadingText}>
              Buscando {currentBook.name} {currentChapter} na Bíblia...
            </Text>
          </View>
        ) : (
          <ScrollView
            ref={scrollViewRef}
            style={styles.versesScrollView}
            nestedScrollEnabled
            showsVerticalScrollIndicator={true}
          >
            <View style={styles.versesWrapper}>
              {verses.map((v) => {
                const isSelected = selectedVerseNumber === v.number;
                return (
                  <Pressable
                    key={v.number}
                    style={[
                      styles.verseParagraph,
                      isSelected && styles.verseParagraphSelected,
                    ]}
                    onPress={() =>
                      setSelectedVerseNumber(isSelected ? null : v.number)
                    }
                  >
                    <Text
                      style={[
                        styles.verseNumberBadge,
                        isSelected && styles.verseNumberBadgeSelected,
                      ]}
                    >
                      {v.number}
                    </Text>
                    <Text
                      style={[
                        styles.verseText,
                        isSelected && styles.verseTextSelected,
                      ]}
                    >
                      {v.text}
                    </Text>
                  </Pressable>
                );
              })}
            </View>
          </ScrollView>
        )}

        {/* Card Footer Info */}
        <View style={styles.cardFooter}>
          <Feather name="check-circle" size={14} color="#0F7B6C" />
          <Text style={styles.footerText}>
            {currentBook.name} {currentChapter} — Texto Bíblico Oficial ({verses.length} versículos)
          </Text>
        </View>
      </View>

      {/* Book / Chapter / Verse Selection Modal */}
      <Modal
        visible={showSelectorModal}
        animationType="slide"
        transparent
        onRequestClose={() => setShowSelectorModal(false)}
      >
        <Pressable
          style={styles.modalOverlay}
          onPress={() => setShowSelectorModal(false)}
        >
          <Pressable
            style={styles.modalContentCard}
            onPress={(e) => e.stopPropagation()}
          >
            {/* Modal Header */}
            <View style={styles.modalHeader}>
              <Text style={styles.modalTitle}>Navegar pela Bíblia Sagrada</Text>
              <Pressable onPress={() => setShowSelectorModal(false)}>
                <Feather name="x" size={22} color="#1E1B38" />
              </Pressable>
            </View>

            {/* Breadcrumb Steps Bar */}
            <View style={styles.breadcrumbBar}>
              <Pressable
                style={[
                  styles.breadcrumbPill,
                  modalStep === 'LIVRO' && styles.breadcrumbPillActive,
                ]}
                onPress={() => setModalStep('LIVRO')}
              >
                <Text
                  style={[
                    styles.breadcrumbText,
                    modalStep === 'LIVRO' && styles.breadcrumbTextActive,
                  ]}
                >
                  1. Livro: {tempBook.name}
                </Text>
              </Pressable>

              <Feather name="chevron-right" size={14} color="#8C7C6D" />

              <Pressable
                style={[
                  styles.breadcrumbPill,
                  modalStep === 'CAPITULO' && styles.breadcrumbPillActive,
                ]}
                onPress={() => setModalStep('CAPITULO')}
              >
                <Text
                  style={[
                    styles.breadcrumbText,
                    modalStep === 'CAPITULO' && styles.breadcrumbTextActive,
                  ]}
                >
                  2. Cap. {tempChapter}
                </Text>
              </Pressable>

              <Feather name="chevron-right" size={14} color="#8C7C6D" />

              <Pressable
                style={[
                  styles.breadcrumbPill,
                  modalStep === 'VERSICULO' && styles.breadcrumbPillActive,
                ]}
                onPress={() => setModalStep('VERSICULO')}
              >
                <Text
                  style={[
                    styles.breadcrumbText,
                    modalStep === 'VERSICULO' && styles.breadcrumbTextActive,
                  ]}
                >
                  3. Versículo
                </Text>
              </Pressable>
            </View>

            {/* STEP 1: LIVROS */}
            {modalStep === 'LIVRO' && (
              <View style={styles.stepContainer}>
                {/* Testament Tabs */}
                <View style={styles.testamentTabRow}>
                  <Pressable
                    style={[
                      styles.testamentTab,
                      selectedTestament === 'AT' && styles.testamentTabActive,
                    ]}
                    onPress={() => setSelectedTestament('AT')}
                  >
                    <Text
                      style={[
                        styles.testamentTabText,
                        selectedTestament === 'AT' && styles.testamentTabTextActive,
                      ]}
                    >
                      Antigo Testamento (39)
                    </Text>
                  </Pressable>

                  <Pressable
                    style={[
                      styles.testamentTab,
                      selectedTestament === 'NT' && styles.testamentTabActive,
                    ]}
                    onPress={() => setSelectedTestament('NT')}
                  >
                    <Text
                      style={[
                        styles.testamentTabText,
                        selectedTestament === 'NT' && styles.testamentTabTextActive,
                      ]}
                    >
                      Novo Testamento (27)
                    </Text>
                  </Pressable>
                </View>

                {/* Search Bar */}
                <View style={styles.searchBoxContainer}>
                  <Feather name="search" size={16} color="#8C7C6D" />
                  <TextInput
                    style={styles.searchInput}
                    placeholder="Buscar livro..."
                    placeholderTextColor="#A09488"
                    value={searchQuery}
                    onChangeText={setSearchQuery}
                  />
                  {searchQuery.length > 0 && (
                    <Pressable onPress={() => setSearchQuery('')}>
                      <Feather name="x-circle" size={16} color="#8C7C6D" />
                    </Pressable>
                  )}
                </View>

                {/* Books Grid */}
                <FlatList
                  data={filteredBooks}
                  keyExtractor={(item) => item.id}
                  numColumns={2}
                  columnWrapperStyle={styles.booksGridWrapper}
                  showsVerticalScrollIndicator={false}
                  renderItem={({ item }) => {
                    const isSelected = tempBook.id === item.id;
                    return (
                      <Pressable
                        style={[
                          styles.bookGridSquare,
                          isSelected && styles.bookGridSquareSelected,
                        ]}
                        onPress={() => handleSelectBook(item)}
                      >
                        <Text
                          style={[
                            styles.bookGridText,
                            isSelected && styles.bookGridTextSelected,
                          ]}
                          numberOfLines={1}
                        >
                          {item.name}
                        </Text>
                        <Text style={styles.bookGridSubtext}>
                          {item.chaptersCount} cap.
                        </Text>
                      </Pressable>
                    );
                  }}
                />
              </View>
            )}

            {/* STEP 2: CAPÍTULOS */}
            {modalStep === 'CAPITULO' && (
              <View style={styles.stepContainer}>
                <Text style={styles.stepSubtitle}>
                  Selecione o capítulo de <Text style={{ fontWeight: '800', color: '#ED5B0A' }}>{tempBook.name}</Text>:
                </Text>

                <ScrollView showsVerticalScrollIndicator={false} style={{ maxHeight: 300 }}>
                  <View style={styles.numbersGrid}>
                    {Array.from(
                      { length: tempBook.chaptersCount },
                      (_, i) => i + 1,
                    ).map((chapNum) => {
                      const isSelected = chapNum === tempChapter;
                      return (
                        <Pressable
                          key={chapNum}
                          style={[
                            styles.numberSquare,
                            isSelected && styles.numberSquareSelected,
                          ]}
                          onPress={() => handleSelectChapter(chapNum)}
                        >
                          <Text
                            style={[
                              styles.numberSquareText,
                              isSelected && styles.numberSquareTextSelected,
                            ]}
                          >
                            {chapNum}
                          </Text>
                        </Pressable>
                      );
                    })}
                  </View>
                </ScrollView>
              </View>
            )}

            {/* STEP 3: VERSÍCULOS */}
            {modalStep === 'VERSICULO' && (
              <View style={styles.stepContainer}>
                <View style={styles.stepHeaderRow}>
                  <Text style={styles.stepSubtitle}>
                    Versículo em <Text style={{ fontWeight: '800', color: '#0F7B6C' }}>{tempBook.name} {tempChapter}</Text>:
                  </Text>
                  <Pressable
                    style={styles.confirmChapterButton}
                    onPress={handleConfirmWholeChapter}
                  >
                    <Text style={styles.confirmChapterButtonText}>Ler Capítulo Todo</Text>
                  </Pressable>
                </View>

                <ScrollView showsVerticalScrollIndicator={false} style={{ maxHeight: 300 }}>
                  <View style={styles.numbersGrid}>
                    {tempVerses.map((v) => (
                      <Pressable
                        key={v.number}
                        style={styles.numberSquare}
                        onPress={() => handleSelectVerse(v.number)}
                      >
                        <Text style={styles.numberSquareText}>{v.number}</Text>
                      </Pressable>
                    ))}
                  </View>
                </ScrollView>
              </View>
            )}
          </Pressable>
        </Pressable>
      </Modal>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    width: '100%',
    marginVertical: 12,
  },
  headerBar: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    backgroundColor: '#FFFFFF',
    borderRadius: 20,
    paddingHorizontal: 12,
    paddingVertical: 10,
    marginBottom: 12,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.04,
    shadowRadius: 6,
    elevation: 2,
  },
  navButton: {
    width: 36,
    height: 36,
    borderRadius: 18,
    backgroundColor: '#F3EDE2',
    alignItems: 'center',
    justifyContent: 'center',
  },
  titleSelectorTrigger: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 8,
    backgroundColor: '#FFF6E5',
    paddingHorizontal: 16,
    paddingVertical: 8,
    borderRadius: 16,
    borderWidth: 1.5,
    borderColor: '#ED5B0A',
  },
  titleText: {
    fontSize: 16,
    fontWeight: '800',
    color: '#ED5B0A',
  },
  scriptureCard: {
    backgroundColor: '#FFFFFF',
    borderRadius: 22,
    padding: 18,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.05,
    shadowRadius: 10,
    elevation: 3,
  },
  cardBadgeHeader: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    marginBottom: 14,
    flexWrap: 'wrap',
    gap: 8,
  },
  badgeGroup: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 6,
  },
  yvBadge: {
    backgroundColor: '#FDE8E0',
    paddingHorizontal: 8,
    paddingVertical: 4,
    borderRadius: 10,
  },
  yvBadgeText: {
    fontSize: 10,
    fontWeight: '900',
    color: '#ED5B0A',
    letterSpacing: 0.5,
  },
  usfmBadge: {
    backgroundColor: '#F3EDE2',
    paddingHorizontal: 8,
    paddingVertical: 4,
    borderRadius: 8,
  },
  usfmBadgeText: {
    fontSize: 11,
    fontWeight: '700',
    color: '#766E65',
  },
  versionSelectorRow: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 4,
    backgroundColor: '#F7F3EB',
    borderRadius: 12,
    padding: 3,
  },
  versionPill: {
    paddingHorizontal: 8,
    paddingVertical: 3,
    borderRadius: 9,
  },
  versionPillActive: {
    backgroundColor: '#0F7B6C',
  },
  versionPillText: {
    fontSize: 11,
    fontWeight: '700',
    color: '#766E65',
  },
  versionPillTextActive: {
    color: '#FFFFFF',
  },
  loadingBox: {
    paddingVertical: 40,
    alignItems: 'center',
    justifyContent: 'center',
    gap: 12,
  },
  loadingText: {
    fontSize: 13,
    fontWeight: '600',
    color: '#8C7C6D',
  },
  versesScrollView: {
    maxHeight: 280,
    marginVertical: 4,
  },
  versesWrapper: {
    gap: 8,
    paddingRight: 4,
  },
  verseParagraph: {
    flexDirection: 'row',
    alignItems: 'flex-start',
    gap: 8,
    paddingVertical: 8,
    paddingHorizontal: 10,
    borderRadius: 12,
  },
  verseParagraphSelected: {
    backgroundColor: '#FDF3D7',
    borderWidth: 1,
    borderColor: '#D97706',
  },
  verseNumberBadge: {
    fontSize: 13,
    fontWeight: '900',
    color: '#ED5B0A',
    marginTop: 2,
    minWidth: 20,
  },
  verseNumberBadgeSelected: {
    color: '#D97706',
  },
  verseText: {
    flex: 1,
    fontSize: 15,
    lineHeight: 24,
    color: '#2C2A4A',
    fontWeight: '500',
  },
  verseTextSelected: {
    color: '#1E1B38',
    fontWeight: '700',
  },
  cardFooter: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 6,
    paddingTop: 12,
    marginTop: 8,
    borderTopWidth: 1,
    borderTopColor: '#F3EDE2',
  },
  footerText: {
    fontSize: 11,
    fontWeight: '600',
    color: '#766E65',
  },

  // Modal styles
  modalOverlay: {
    flex: 1,
    backgroundColor: 'rgba(30, 27, 56, 0.55)',
    justifyContent: 'flex-end',
  },
  modalContentCard: {
    backgroundColor: '#FFF6E5',
    borderTopLeftRadius: 28,
    borderTopRightRadius: 28,
    padding: 20,
    maxHeight: '88%',
  },
  modalHeader: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    marginBottom: 12,
  },
  modalTitle: {
    fontSize: 18,
    fontWeight: '800',
    color: '#1E1B38',
  },
  breadcrumbBar: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 6,
    marginBottom: 14,
    flexWrap: 'wrap',
  },
  breadcrumbPill: {
    backgroundColor: '#E8DFC9',
    paddingHorizontal: 10,
    paddingVertical: 5,
    borderRadius: 10,
  },
  breadcrumbPillActive: {
    backgroundColor: '#ED5B0A',
  },
  breadcrumbText: {
    fontSize: 12,
    fontWeight: '700',
    color: '#766E65',
  },
  breadcrumbTextActive: {
    color: '#FFFFFF',
  },
  stepContainer: {
    minHeight: 320,
  },
  stepSubtitle: {
    fontSize: 14,
    fontWeight: '600',
    color: '#1E1B38',
    marginBottom: 12,
  },
  stepHeaderRow: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    marginBottom: 12,
  },
  confirmChapterButton: {
    backgroundColor: '#0F7B6C',
    paddingHorizontal: 12,
    paddingVertical: 6,
    borderRadius: 10,
  },
  confirmChapterButtonText: {
    fontSize: 11,
    fontWeight: '800',
    color: '#FFFFFF',
  },
  testamentTabRow: {
    flexDirection: 'row',
    backgroundColor: '#E8DFC9',
    borderRadius: 14,
    padding: 3,
    marginBottom: 12,
  },
  testamentTab: {
    flex: 1,
    paddingVertical: 8,
    alignItems: 'center',
    borderRadius: 12,
  },
  testamentTabActive: {
    backgroundColor: '#0F7B6C',
  },
  testamentTabText: {
    fontSize: 12,
    fontWeight: '700',
    color: '#766E65',
  },
  testamentTabTextActive: {
    color: '#FFFFFF',
  },
  searchBoxContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: '#FFFFFF',
    borderRadius: 14,
    paddingHorizontal: 12,
    paddingVertical: 8,
    gap: 8,
    marginBottom: 12,
    borderWidth: 1,
    borderColor: '#E8DFC9',
  },
  searchInput: {
    flex: 1,
    fontSize: 14,
    color: '#1E1B38',
    padding: 0,
  },
  booksGridWrapper: {
    gap: 10,
    marginBottom: 10,
  },
  bookGridSquare: {
    flex: 1,
    backgroundColor: '#FFFFFF',
    padding: 12,
    borderRadius: 14,
    borderWidth: 1,
    borderColor: '#E8DFC9',
  },
  bookGridSquareSelected: {
    borderColor: '#ED5B0A',
    backgroundColor: '#FDE8E0',
  },
  bookGridText: {
    fontSize: 14,
    fontWeight: '700',
    color: '#1E1B38',
    marginBottom: 2,
  },
  bookGridTextSelected: {
    color: '#ED5B0A',
  },
  bookGridSubtext: {
    fontSize: 11,
    color: '#8C7C6D',
    fontWeight: '500',
  },
  numbersGrid: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    gap: 10,
    paddingBottom: 20,
  },
  numberSquare: {
    width: 44,
    height: 44,
    borderRadius: 12,
    backgroundColor: '#FFFFFF',
    borderWidth: 1,
    borderColor: '#E8DFC9',
    alignItems: 'center',
    justifyContent: 'center',
  },
  numberSquareSelected: {
    backgroundColor: '#ED5B0A',
    borderColor: '#ED5B0A',
  },
  numberSquareText: {
    fontSize: 15,
    fontWeight: '800',
    color: '#1E1B38',
  },
  numberSquareTextSelected: {
    color: '#FFFFFF',
  },
});
