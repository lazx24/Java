package com.example.fulltext;

final class WordSegment {
    private String word;
    private int index;

    public WordSegment(String word, int index) {
	this.word = word;
	this.index = index;
    }

    public String getWord() {
	return this.word;
    }

    public void setWord(String word) {
	this.word = word;
    }

    public int getIndex() {
	return this.index;
    }

    public void setIndex(int index) {
	this.index = index;
    }
}
