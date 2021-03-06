package com.company;


import java.util.HashMap;

/**
 * Responsible for sanitizing words, and producing data structure with words in it.
 */
public class WordHolder {
    private HashMap<String, Integer> wordTracker;

    public WordHolder(){
        wordTracker = new HashMap<>();
    }

    public void processWord(String word){
        String sanitizedWord = findWord(word.toLowerCase());
        if (sanitizedWord != null){
            if (wordTracker.containsKey(sanitizedWord)){
                wordTracker.replace(sanitizedWord, wordTracker.get(sanitizedWord)+1);
            } else {
                wordTracker.put(sanitizedWord, 1);
            }
        }
    }

    private String findWord(String word){
        int length = word.length();
        Integer firstAlphanumeric = null;
        Integer lastAlphanumeric = null;
        for (int i=0; i<length; i++){
            char currentChar = word.charAt(i);
            if (firstAlphanumeric == null){
                if (Character.isAlphabetic(currentChar)){
                    firstAlphanumeric = i;
                }
            } else {
                if (Character.isAlphabetic(currentChar)){
                    lastAlphanumeric = i + 1;
                }
            }
        }
        if (firstAlphanumeric !=null && lastAlphanumeric != null){
            return word.substring(firstAlphanumeric, lastAlphanumeric);
        }
        return null; //no start/end alphanumeric characters found, discard non-word
    }

    public HashMap<String, Integer> getWordsAndCounts(){
        return wordTracker;
    }
}
