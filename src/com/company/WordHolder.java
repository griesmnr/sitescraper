package com.company;


import java.util.HashMap;

/**
 * Created by nicoleg on 12/6/15.
 */
public class WordHolder {
    private HashMap<String, Integer> wordTracker;

    public WordHolder(){
        wordTracker = new HashMap<>();
    }

    public void processWord(String word){
        String lowerCaseWord = word.toLowerCase();
        if (wordTracker.containsKey(lowerCaseWord)){
            wordTracker.replace(lowerCaseWord, wordTracker.get(lowerCaseWord)+1);
        } else {
            wordTracker.put(lowerCaseWord, 1);
        }
    }

    public HashMap<String, Integer> getWordsAndCounts(){
        return wordTracker;
    }
}
