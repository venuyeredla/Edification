package org.learn.english.repositories;

import org.dizitart.no2.Document;
import org.dizitart.no2.NitriteCollection;
import org.dizitart.no2.filters.Filters;
import org.dizitart.no2.objects.Cursor;
import org.dizitart.no2.objects.ObjectRepository;
import org.dizitart.no2.objects.filters.ObjectFilters;
import org.learn.english.models.Word;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EnglishRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnglishRepository.class);

    @Autowired
    ObjectRepository<Word> dictRepository;

    @Autowired
    @Qualifier("stopWordsCollection")
    NitriteCollection stopWordsCollection;

    @Autowired
    @Qualifier("originWordsCollection")
    NitriteCollection originWordsCollection;

    public void deleteAll(){
        dictRepository.remove(ObjectFilters.ALL);
    }

    public void saveAll(List<Word> words){
         Word[] wordsArray=new Word[words.size()];
         words.toArray(wordsArray);
         dictRepository.insert(wordsArray);
    }
    public void saveAll(Word[] words){
        this.deleteAll();
        dictRepository.insert(words);
    }


    public List<Word> readDictionary(){
        Cursor<Word> dictCursor = dictRepository.find();
         List<Word> words=new ArrayList<>();
        for(Word word:dictCursor){
            words.add(word);
            System.out.println(word.getWord());
        }
        LOGGER.info("No of words in dictionary :: {}",words.size());
        return words;
    }

     public Map<String,String> getOrigins(){
         org.dizitart.no2.Cursor documents = originWordsCollection.find();
         Map<String,String> originMap=new HashMap<>();
         for (Document d:documents){
            String word = (String)d.get("word");
             String origin = (String)d.get("origin");
             originMap.put(word,origin);
        }
         System.out.println("Total size :: "+originMap.size());
         return originMap;

    }

    public void writeDictionary(List<Word> englishDic){
       // repository.insert(englishDic);
    }

    public void cleanSaveAllStopWords(Set<String> stopWords){
        stopWordsCollection.remove(Filters.ALL);
        stopWords.stream().forEach(word->{
            stopWordsCollection.insert(Document.createDocument("key",word));
        });
    }

    public void saveAllStopWords(Set<String> stopWords){
        stopWords.stream().forEach(word->{
            stopWordsCollection.insert(Document.createDocument("key",word));
        });
    }

    public Set<String> getStopWords(){
        org.dizitart.no2.Cursor stopWordsCursor = stopWordsCollection.find();
        Set<String> stopWords=new HashSet<>();
        for (Document d:stopWordsCursor){
            String key = (String)d.get("key");
            stopWords.add(key);
        }
        return stopWords;
    }

}
