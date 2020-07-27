package org.edification.nlp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnalyzedWord {
    private String word;
    private String pos;
    private String lemma;
}
