package org.edification.service;


import org.edification.nlp.NlpAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReaderService {
    @Autowired
    NlpAnalysisService nlpAnalysis;
    public List<String> getSentences() {
     return   nlpAnalysis.sentenceDetection("C:\\work\\opensource\\engdata\\reader\\reader.txt");
    }
    public List<String> getDirFiles(){
        return null;
    }
}
