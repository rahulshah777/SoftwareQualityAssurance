package org.zorba;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class WordFrequencyAnalyzerTest {

    @Test
    public void testCountWordFrequencies() {
        String testText = "the quick brown fox jumps over the lazy dog. The quick brown fox is quick.";
        
        Map<String, Integer> frequencies = Main.countWordFrequencies(testText);
        
        // Verify word counts
        assertEquals(3, frequencies.get("the"));
        assertEquals(3, frequencies.get("quick"));
        assertEquals(2, frequencies.get("brown"));
        assertEquals(2, frequencies.get("fox"));
        assertEquals(1, frequencies.get("jumps"));
        assertEquals(1, frequencies.get("over"));
        assertEquals(1, frequencies.get("lazy"));
        assertEquals(1, frequencies.get("dog"));
        assertEquals(1, frequencies.get("is"));
        
        // Verify total unique words
        assertEquals(9, frequencies.size());
    }

    @Test
    public void testEmptyText() {
        Map<String, Integer> frequencies = Main.countWordFrequencies("");
        assertTrue(frequencies.isEmpty());
    }

    @Test
    public void testSpecialCharacters() {
        String testText = "word!@#$%^&*()_+={}[]|\\:;\"'<>,.?/`~word word-word word123 123";
        Map<String, Integer> frequencies = Main.countWordFrequencies(testText);
        
        // The current implementation splits on any non-alphanumeric character
        // So "word!" becomes "word" and "word-word" becomes ["word", "word"]
        assertEquals(4, frequencies.get("word")); // word! + word + word-word (split into two words)
        assertEquals(1, frequencies.get("word123"));
        assertEquals(1, frequencies.get("123"));
        assertNull(frequencies.get("word!"));
    }
}
