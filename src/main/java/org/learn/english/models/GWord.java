package org.learn.english.models;

import java.util.Map;

public class GWord {
    private String word;
    private String phonetic;
    private String origin;
    private Map<String,Object> meaning;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getPhonetic() {
        return phonetic;
    }

    public void setPhonetic(String phonetic) {
        this.phonetic = phonetic;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Map<String, Object> getMeaning() {
        return meaning;
    }

    public void setMeaning(Map<String, Object> meaning) {
        this.meaning = meaning;
    }
}