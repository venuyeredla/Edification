package org.edification.nlp;

import com.google.gson.Gson;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.edification.models.Sentence;
import org.edification.service.FileService;
import org.edification.service.TermsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Setter
public class NlpAnalysisService {

    @Autowired
    TermsService stopWordsService;
    
    @Autowired
    Gson gson;

    StanfordCoreNLP lemmaPipeline;
    StanfordCoreNLP sentencePipeLine;

    @PostConstruct
      public void init(){
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma");
        lemmaPipeline=new StanfordCoreNLP(props);
        Properties sentenceProps = new Properties();    // set up pipeline properties
        sentenceProps.setProperty("annotators", "tokenize,ssplit");
        sentencePipeLine= new StanfordCoreNLP(sentenceProps);
    }

    /**
     * Returns the file content as sentences for reading purpose.
     * @param filePath
     * @return
     */
    public List<String> sentenceDetection(String filePath){
                List<String> sentenceList=new ArrayList<>();
                Optional<String> data = FileService.readFileAsString(Paths.get(filePath));
                if(data.isPresent()){
                    CoreDocument exampleDocument = new CoreDocument(data.get());
                    sentencePipeLine.annotate(exampleDocument); // annotate document
                    List<CoreSentence> sentences = exampleDocument.sentences();
                    sentences.stream().forEach(sentence->{
                        sentenceList.add(sentence.text());
                    });
                }
        return sentenceList;
    }

    /**
     * Returns lemma form of words with frequencies.
     * @param directorPath
     * @return
     */
     public  Map<String,Long> getUnknownWords(String directorPath){
        Set<String> stopWords = stopWordsService.getStopWords();
        List<String> directoryTokens=new ArrayList<>();
        try (Stream<Path> stream = Files.walk( Paths.get(directorPath), Integer.MAX_VALUE)) {
            List<String> directoryFiles = stream.map(String::valueOf).filter(name-> name.endsWith(".txt")).sorted().collect(Collectors.toList());
            directoryFiles.forEach(fileName->{
                List<String> fileTokens = this.tokenizeFileText(stopWords, fileName);
                directoryTokens.addAll(fileTokens);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, Long> tokenizedTextWithFreq = directoryTokens.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        Map<String, Long> sortedByValue = new LinkedHashMap<>();
        tokenizedTextWithFreq.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .forEachOrdered(x -> sortedByValue.put(x.getKey(), x.getValue()));
        return sortedByValue;
    }


    public List<String> tokenizeFileText(Set<String> stopWords, String fileName){
        Optional<String> data = FileService.readFileAsString(Paths.get(fileName));
        if(data.isPresent()){
            List<String> tokens=new ArrayList<>();
            CoreDocument exampleDocument = new CoreDocument(data.get());
            lemmaPipeline.annotate(exampleDocument); // annotate document
            List<CoreSentence> sentences = exampleDocument.sentences();
            sentences.stream().forEach(sentence->{
                for (CoreLabel sentenceToken : sentence.tokens()) {
                    if(sentenceToken.word().length()>2){
                        String word = StringUtils.lowerCase(sentenceToken.word());
                        AnalyzedWord nlpWord = this.getWordLemma(word);
                        if(!nlpWord.getPos().equalsIgnoreCase("CD")){
                            this.filterWord(nlpWord.getLemma(),stopWords).ifPresent(t->{
                                tokens.add(t);
                            });
                        }
                    }
                }
            });
            return tokens;
        }
        return Collections.emptyList();
    }


    public Optional<String> filterWord(String word, Set<String> existingStopWords){
        Optional<String> wordOptional=Optional.empty();
        if(word.length() > 2 && !existingStopWords.contains(word) && !word.contains("'") && !word.matches(".*\\d.*")){
            wordOptional= Optional.ofNullable(word);
        }
        return  wordOptional;
    }

   /**
    * Walks through directory .txt files and returns sentences.
    * @param directorPath
    * @return
    */
    public  List<Sentence> getDirSentences(String directorPath){
         List<Sentence> sentenceList=new ArrayList<>();
        try (Stream<Path> stream = Files.walk(Paths.get(directorPath), Integer.MAX_VALUE)) {
            List<String> filesList = stream.map(String::valueOf).filter(name-> name.endsWith(".txt")).sorted().collect(Collectors.toList());
            filesList.forEach(fileName->{
                List<String> fileSentences = this.sentenceDetection(fileName);
                    Path filePath = Paths.get(fileName);
                    String secriesName = filePath.getParent().getFileName().toString();
                    String episodeName = filePath.getFileName().toString();
                    for(int i=0;i<fileSentences.size();i++){
                        Sentence sent=new Sentence();
                        sent.setSentence(fileSentences.get(i));
                        if(i>0) sent.setBefore(fileSentences.get(i-1));
                        if (i<fileSentences.size()-1) sent.setAfter(fileSentences.get(i+1));
                        sent.setSeries(secriesName);
                        sent.setEpisode(episodeName);
                        sentenceList.add(sent);
                    }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sentenceList;
    }

    public AnalyzedWord getWordLemma(String word){
        List<AnalyzedWord> lemmas = new ArrayList<>();
        Annotation document = new Annotation(word);
        lemmaPipeline.annotate(document);
        // Iterate over all of the sentences found
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        for(CoreMap sentence: sentences) {
            // Iterate over all tokens in a sentence
            for (CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                AnalyzedWord nlpWord = new AnalyzedWord();
                nlpWord.setWord(word);
                nlpWord.setLemma(token.get(CoreAnnotations.LemmaAnnotation.class));
                nlpWord.setPos(token.get(CoreAnnotations.PartOfSpeechAnnotation.class));
                lemmas.add(nlpWord);
            }
        }
        return  lemmas.stream().findFirst().get();
    }

       /**
     * Go throgh with only .txt files.
     * @param directorPath
     * @return
     */
    public  List<AnalyzedWord> tokenizeText(String directorPath){
        List<AnalyzedWord> nlpWords=new ArrayList<>();
        Set<String> stopWords = stopWordsService.getStopWords();
        try (Stream<Path> stream = Files.walk(Paths.get(directorPath), Integer.MAX_VALUE)) {
            List<String> filesList = stream.map(String::valueOf).filter(name-> name.endsWith(".txt")).sorted().collect(Collectors.toList());
            filesList.forEach(fileName->{
                Optional<String> data = FileService.readFileAsString(Paths.get(fileName));
                if(data.isPresent()){
                    CoreDocument exampleDocument = new CoreDocument(data.get());
                    lemmaPipeline.annotate(exampleDocument);  // annotate document
                    // access tokens from a CoreDocument
                    List<CoreSentence> sentences = exampleDocument.sentences();
                    // this for loop will print out all of the tokens and the character offset info
                    sentences.stream().forEach(sentence->{
                        for (CoreLabel token : sentence.tokens()) {     // a token is represented by a CoreLabel
                            if(token.word().length()>2){
                                String word = StringUtils.lowerCase(token.word());
                                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                                if(!stopWords.contains(word) && !word.contains("'") && !word.matches(".*\\d.*") && !pos.equalsIgnoreCase("CD")){
                                    AnalyzedWord nlpWord=new AnalyzedWord();
                                    nlpWord.setWord(word);
                                    nlpWord.setLemma(token.get(CoreAnnotations.LemmaAnnotation.class));
                                    nlpWord.setPos(pos);
                                    nlpWords.add(nlpWord);
                                }
                            }
                        }
                    });
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Tokens size ::"+nlpWords.size());
        return nlpWords;
    }
}
