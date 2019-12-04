package org.learn.english.nlp;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.simple.Sentence;
import org.apache.commons.lang3.StringUtils;
import org.learn.english.models.Word;
import org.learn.english.util.FileUtil;

import com.google.gson.Gson;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.SentenceUtils;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.util.CoreMap;

public class StanfordNLPUtil {

    Gson gson=new Gson();

    public void tokenizeText(){
        try {
            // set up pipeline properties
            Properties props = new Properties();
            props.setProperty("annotators", "tokenize,ssplit");
            // set up pipeline
            StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
            Set<String> tokens=new HashSet<>();
            System.out.println("---");
            System.out.println("Accessing Tokens In A CoreDocument");
            System.out.println("(text, char offset begin, char offset end)");
            Files.list(Paths.get(FileUtil.DIR + "twoAndHalf")).forEach(path->{
                String fileName = path.toFile().getAbsolutePath();
                System.out.println("Reading file"+fileName);
                Optional<String> data = FileUtil.readFileAsString(fileName);
                if(data.isPresent()){
                    // the following has examples for the new Core Wrapper API and the older Annotation API
                    // example using Core Wrappers (new API designed to make it easier to work with NLP data)
                    CoreDocument exampleDocument = new CoreDocument(data.get());
                    // annotate document
                    pipeline.annotate(exampleDocument);
                    // access tokens from a CoreDocument
                    // a token is represented by a CoreLabel
                    List<CoreSentence> sentences = exampleDocument.sentences();
                    // this for loop will print out all of the tokens and the character offset info
                    sentences.stream().forEach(sentence->{
                        for (CoreLabel token : sentence.tokens()) {
                            // System.out.println(token.word() + "\t" + token.beginPosition() + "\t" + token.endPosition());
                            //   System.out.println(token.word() + "\t" + token.beginPosition() + "\t" + token.endPosition());
                            if(token.word().length()>2){
                                tokens.add(StringUtils.lowerCase(token.word()));
                            }

                        }
                    });
                    // example using older Annotation API
         /*            System.out.println("---");
                    System.out.println("Accessing Tokens In An Annotation");
                    System.out.println("(text, char offset begin, char offset end)");
                    Annotation exampleAnnotation = new Annotation("Here is the text to tokenize.");
                    pipeline.annotate(exampleAnnotation);
                    CoreMap firstSentence = exampleAnnotation.get(CoreAnnotations.SentencesAnnotation.class).get(0);
                    // this for loop will print out all of the tokens and the character offset info
           for (CoreLabel token : firstSentence.get(CoreAnnotations.TokensAnnotation.class)) {
                System.out.println(token.word() + "\t" + token.beginPosition() + "\t" + token.endPosition());
            }*/
                }
            });
            System.out.println("Tokens size ::"+tokens.size());
            tokens.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }








    public void getLemmas(){
          Properties props = new Properties();
          props.setProperty("annotators", "tokenize,ssplit,pos,lemma");
          StanfordCoreNLP pipeline=new StanfordCoreNLP(props);
          List<String> lemmas = new LinkedList<String>();
          Annotation document = new Annotation("Barack Obama was born in Hawaii.");
          pipeline.annotate(document);
          // Iterate over all of the sentences found
          List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
          for(CoreMap sentence: sentences) {
              // Iterate over all tokens in a sentence
              for (CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                  // Retrieve and add the lemma for each word into the list of lemmas
                  lemmas.add(token.get(CoreAnnotations.LemmaAnnotation.class));
              }
          }
        lemmas.stream().forEach(s->System.out.println(s));
      }


    public List<org.learn.english.models.Word> getDictList(){
        Optional<String> webSterData = FileUtil.readFileAsString(FileUtil.DIR+"Latest\\VenuDict.json");
        if(webSterData.isPresent()){
            org.learn.english.models.Word[] words = gson.fromJson(webSterData.get(), Word[].class);
            return	Arrays.asList(words).stream().collect(Collectors.toList());
        }
        return Collections.EMPTY_LIST;
    }






    public void posTagging(String inputFile) throws Exception {
        String stopWordsFile="C:\\Work\\Books\\Vocabulary\\vocab\\stopwords.txt";
        String newStopWords="C:\\Work\\Books\\Vocabulary\\vocab\\newstopwords.txt";
        BufferedWriter bufferedWriter=new BufferedWriter(new FileWriter(newStopWords));
        MaxentTagger tagger = new MaxentTagger("C:\\Work\\Jars\\stanford-postagger-2018-10-16\\models\\english-bidirectional-distsim.tagger");
        TokenizerFactory<CoreLabel> ptbTokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(),"untokenizable=noneKeep");
        BufferedReader r = new BufferedReader(new FileReader(inputFile));
        DocumentPreprocessor documentPreprocessor = new DocumentPreprocessor(r);
        documentPreprocessor.setTokenizerFactory(ptbTokenizerFactory);
        Scanner scanner=new Scanner(System.in);
        Set<String> stopWordList=this.getStopWords(stopWordsFile);
        for (List<HasWord> sentence : documentPreprocessor) {
            List<TaggedWord> tSentence = tagger.tagSentence(sentence);
            tSentence.stream().forEach(ts ->{
                String lowerWord=ts.word().toLowerCase();
                if(!stopWordList.contains(lowerWord)){
                    System.out.print("Word :: "+lowerWord + "  Skip ? y/n :: " );
                    //System.out.println("Word :: " +ts.word() + "    Tag ::"+ts.tag());
                    String skip=scanner.next();
                    if(skip.equalsIgnoreCase("y")){
                        try {
                            bufferedWriter.write(lowerWord+ "\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

           bufferedWriter.flush();
        }
         bufferedWriter.close();
     }

    public void listUnknown(String inputFile) throws Exception {
        String stopWordsFile="C:\\Work\\Books\\Vocabulary\\vocab\\stopwords.txt";
        String newStopWords="C:\\Work\\Books\\Vocabulary\\vocab\\newstopwords.txt";
        BufferedWriter bufferedWriter=new BufferedWriter(new FileWriter(newStopWords));
        MaxentTagger tagger = new MaxentTagger("C:\\Work\\Jars\\stanford-postagger-2018-10-16\\models\\english-bidirectional-distsim.tagger");
        TokenizerFactory<CoreLabel> ptbTokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(),"untokenizable=noneKeep");
        BufferedReader r = new BufferedReader(new FileReader(inputFile));
        DocumentPreprocessor documentPreprocessor = new DocumentPreprocessor(r);
        documentPreprocessor.setTokenizerFactory(ptbTokenizerFactory);
        Scanner scanner=new Scanner(System.in);
        Set<String> oldStopWords=this.getStopWords(stopWordsFile);
        Set<String> allWords=new HashSet<>();

        for (List<HasWord> sentence : documentPreprocessor) {
            List<TaggedWord> tSentence = tagger.tagSentence(sentence);
            Set<String> wordsNow= tSentence.stream().map(ts -> ts.word().toLowerCase()).filter(word -> !oldStopWords.contains(word)).collect(Collectors.toSet());
            allWords.addAll(wordsNow);


        }

        allWords.forEach(wordToSkip->{
            System.out.print("Word :: "+wordToSkip + "  Skip ? y/n :: " );
            //System.out.println("Word :: " +ts.word() + "    Tag ::"+ts.tag());
            String skip=scanner.next();
            if(skip.equalsIgnoreCase("y")){
                try {
                    bufferedWriter.write(wordToSkip+ "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if(skip.equalsIgnoreCase("end")){
                try {
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    System.exit(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        bufferedWriter.flush();
        bufferedWriter.close();
    }





    public Set<String> getStopWords(String stopWordsFile){
         //read file into stream, try-with-resources
         try (Stream<String> stream = Files.lines(Paths.get(stopWordsFile))) {
             return stream.collect(Collectors.toSet());
         } catch (IOException e) {
             e.printStackTrace();
         }
        return Collections.emptySet();

     }
    public void sentenceDetector(String inputFile) throws FileNotFoundException {

        Reader reader = new FileReader(inputFile);
        DocumentPreprocessor dp = new DocumentPreprocessor(reader);
        List<String> sentenceList = new ArrayList<String>();

        for (List<HasWord> sentence : dp) {
            // SentenceUtils not Sentence
            String sentenceString = SentenceUtils.listToString(sentence);
            sentenceList.add(sentenceString);
        }

        for (String sentence : sentenceList) {
            System.out.println(sentence);
        }
    }


    public  void pipelineDemo(String inputFile) {
        // set up pipeline properties
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit");
        // set up pipeline
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        // the following has examples for the new Core Wrapper API and the older Annotation API
        // example using Core Wrappers (new API designed to make it easier to work with NLP data)
        System.out.println("---");
        System.out.println("Accessing Tokens In A CoreDocument");
        System.out.println("(text, char offset begin, char offset end)");
        CoreDocument exampleDocument = new CoreDocument("Here is the text to tokenize.");
        //annotate document
        pipeline.annotate(exampleDocument);
        // access tokens from a CoreDocument
        // a token is represented by a CoreLabel
        List<CoreLabel> firstSentenceTokens = exampleDocument.sentences().get(0).tokens();
        // this for loop will print out all of the tokens and the character offset info
        for (CoreLabel token : firstSentenceTokens) {
            System.out.println(token.word() + "\t" + token.beginPosition() + "\t" + token.endPosition());
        }
        // example using older Annotation API
        System.out.println("---");
        System.out.println("Accessing Tokens In An Annotation");
        System.out.println("(text, char offset begin, char offset end)");
        Annotation exampleAnnotation = new Annotation("Here is the text to tokenize.");
        pipeline.annotate(exampleAnnotation);
        CoreMap firstSentence = exampleAnnotation.get(CoreAnnotations.SentencesAnnotation.class).get(0);
        // this for loop will print out all of the tokens and the character offset info
        for (CoreLabel token : firstSentence.get(CoreAnnotations.TokensAnnotation.class)) {
            System.out.println(token.word() + "\t" + token.beginPosition() + "\t" + token.endPosition());
        }
    }
}
