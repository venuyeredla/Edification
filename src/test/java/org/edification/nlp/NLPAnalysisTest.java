package org.edification.nlp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.edification.config.BootStrap;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.gson.Gson;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes= {BootStrap.class})
public class NLPAnalysisTest {
    static String FILE_ROOT="C:\\work\\opensource\\engdata\\";

    @Autowired
    NlpAnalysisService nlpAnalysis;
    @Autowired
    Gson gson;

    @Test
    public  void testSentenceDetection(){
        List<String> sentences = nlpAnalysis.sentenceDetection(FILE_ROOT+"reader\\s07e05.txt");
        sentences.stream().filter(Objects::nonNull).forEach(System.out::println);
    }

    @Test
    @Ignore
    public void tokenizeText(){
        List<AnalyzedWord> nlpWords =  nlpAnalysis.tokenizeText("C:\\work\\wenglish\\scripts\\");
        nlpWords.forEach(nlpWord -> {
            System.out.println(nlpWord.getWord()+"  -- "+nlpWord.getLemma()+" -- "+nlpWord.getPos());
        });
        System.out.println("Nlp Words size :: "+nlpWords.size());
    }

    @Test
    public void testGetUnknownWords(){
        Map<String, Long> tokenizedTextWithFreq = nlpAnalysis.getUnknownWords(FILE_ROOT+"reader\\");
        List<String> lemmaList=new ArrayList<>();
        tokenizedTextWithFreq.forEach((k,v)->{
            System.out.println(k+" --  "+  v );
        });
        System.out.println("Total unique Tokens :: "+tokenizedTextWithFreq.size());
    }


    @Test
    @Ignore
    public void tokenizeSentences() {
        nlpAnalysis.getDirSentences("C:\\work\\wenglish\\scripts\\");
    }

    @Test
    @Ignore
    public  void getLemma(){
        AnalyzedWord lemmaWord = nlpAnalysis.getWordLemma("fifty");
        System.out.println(lemmaWord.getLemma());
        System.out.println(lemmaWord.getPos());
    }

    @Test
    @Ignore
    public  void filterRegex(){
        List<String> testData = Arrays.asList("9,000", "venu");
        testData.stream().filter(s -> !s.matches(".*\\d.*")).forEach(System.out::println);
       }
}
