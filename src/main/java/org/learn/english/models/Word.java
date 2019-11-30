package org.learn.english.models;

import java.util.List;

public class Word {
    private  String word;
    private List<String> meanings;
    private List<String> ant;
    private List<String> syn;
    private String origin;

    public String getWord() {
        return word;
    }
    public void setWord(String word) {
        this.word = word;
    }
    public List<String> getAnt() {
        return ant;
    }
    public void setAnt(List<String> ant) {
        this.ant = ant;
    }
    public List<String> getSyn() {
        return syn;
    }
    public void setSyn(List<String> syn) {
        this.syn = syn;
    }
    public List<String> getMeanings() {
        return meanings;
    }
    public void setMeanings(List<String> meanings) {
        this.meanings = meanings;
    }
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
    
}