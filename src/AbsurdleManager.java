/*
 * The AbsurdleManager class differentiates the game of Absurdle from
 * Wordle. The game is prolonged with each guess by changing the
 * set of words being considered. This is determined by the pattern with
 * the largest set of words.
 */

import java.util.*;

public class AbsurdleManager {
    private Set<String> wordSet;
    private int length;
    /*
     * Constructs a new AbsurdleManager object with a set of words that
     * match the target word length and does not contain duplicate words.
     * Throws an IllegalArgumentException if the length parameter is less
     * than one (int length < 1). Assumes that the dictionary parameter
     * contains, non-empty, lowercase letter Strings. 
     * Parameters:
     *  Collection<String> dictionary - list of words to be considered
     *  int length - the required length of the target word
     */
    public AbsurdleManager(Collection<String> dictionary, int length) {
        if (length < 1) {
            throw new IllegalArgumentException();
        }
        
        this.wordSet = new TreeSet<>();
        this.length = length;
        for (String word : dictionary) {
            if (word.length() == length) {
                wordSet.add(word);
            }
        } 
    }
    
    // Returns the set of words currently being considered.
    public Set<String> words() {
        return wordSet;
    }

    /*
     * Params:
     *  String word -- the secret word trying to be guessed. Assumes word is made up of only
     *                 lower case letters and is the same length as guess.
     * String guess -- the guess for the word. Assumes guess is made up of only
     *                 lower case letters and is the same length as word.
     * Exceptions:
     *   none
     * Returns:
     *   returns a string, made up of gray, yellow, or green squares, representing a
     *   standard wordle clue for the provided guess made against the provided secret word.
     */
    public static String patternFor(String word, String guess) {
        Map<Character, Integer> counts = new TreeMap<Character, Integer>();
        for (int i = 0; i < word.length(); i++) {
            char letter = word.charAt(i);
            
            if (!counts.containsKey(letter)) {
                counts.put(letter, 1);
            } else {
                counts.put(letter, counts.get(letter) + 1);
            }
        }
        
        String[] pattern = new String[word.length()];
        for (int i = 0; i < word.length(); i++) {
            char wordLetter = word.charAt(i);
            char guessLetter = guess.charAt(i);
            
            if (guessLetter == wordLetter) {
                pattern[i] = "🟩";
                counts.put(wordLetter, counts.get(wordLetter) - 1);
            }
        }
    
        for (int i = 0; i < word.length(); i++) {
            char letter = guess.charAt(i);
        
            if (pattern[i] == null && counts.containsKey(letter) && counts.get(letter) > 0) {
                pattern[i] = "🟨";
                counts.put(letter, counts.get(letter) - 1);
            }
        }
    
        for (int i = 0; i < word.length(); i++) {
            if (pattern[i] == null) {
                pattern[i] = "⬜";
            }
        }
        
        String newPattern = "";
        for (int i = 0; i < pattern.length; i++) {
            newPattern += pattern[i];
        }
        return newPattern;
    }
    
    /*
     * Returns a String for the pattern with the largest set of words.
     * Throws an IllegalStateException if the current set of words being 
     * considered is empty. Throws an IllegalArgumentException if the guess
     * parameter is not the correct length. Assumes that the guess parameter 
     * contains all lowercase letters. Updates the current set of words 
     * being considered.
     * Parameters:
     *  String guess - the guess for the target word
     */
    public String record(String guess) {
        if (wordSet.isEmpty()) {
            throw new IllegalStateException();
        }
        
        if (guess.length() != length) {
            throw new IllegalArgumentException();
        }
    
        Map<String, Set<String>> patternMap = new TreeMap<>();
        for (String word : wordSet) {
            String pattern = patternFor(word, guess);
            if (!patternMap.containsKey(pattern)) {
                patternMap.put(pattern, new TreeSet<>());
            }
            patternMap.get(pattern).add(word);  
        }
        
        int max = 0;
        String largestSet = "";
        for (String pattern : patternMap.keySet()) {
            int setSize = patternMap.get(pattern).size();
            if (setSize > max) {
                largestSet = pattern;
                max = setSize;
                wordSet = patternMap.get(pattern);
            }
        }
        return largestSet;
    }
}