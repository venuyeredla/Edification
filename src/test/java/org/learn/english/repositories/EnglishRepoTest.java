package org.learn.english.repositories;

import org.dizitart.no2.Document;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.NitriteCollection;
import org.dizitart.no2.objects.ObjectRepository;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.learn.english.config.EnglishTestConfig;
import org.learn.english.models.Word;
import org.learn.english.solr.DictionaryIndexer;
import org.learn.english.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes= {EnglishTestConfig.class})
public class EnglishRepoTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnglishRepository.class);

    @Autowired
    EnglishRepository englishRepository;
    @Autowired
    StopWordsRepository stopWordsRepository;
    @Autowired
    OriginReposiotry originReposiotry;

    @Autowired
    DictionaryIndexer dictionaryIndexer;

    @Test
    @Ignore
    public void testRead(){
        List<Word> words = englishRepository.readDictionary();
        words.stream().map(word -> word.getWord()).forEach(System.out::println);
        LOGGER.info("No of words in dictionary = {}",words.size());
    }

    @Test
    public void testUpdateOriginsAndIndex(){
        Map<String, String> origins = originReposiotry.getOrigins();
        List<Word> tobeIndexed = englishRepository.updateOrigins(origins);
        dictionaryIndexer.indexDictionary(tobeIndexed);
    }

    @Test
    @Ignore
    public void copyDB(){
        List<Word> words = englishRepository.readDictionary();
        Set<String> stopWords = stopWordsRepository.getStopWords();
        Nitrite newNitriteDB = Nitrite.builder()
                .compressed()
                .filePath("data/english.db")
                .openOrCreate("english", "english");
        ObjectRepository<Word> newDictRepo = newNitriteDB.getRepository(Word.class);
        NitriteCollection newStopWordsCollection = newNitriteDB.getCollection("stopwords");

        Word[] wordsArray=new Word[words.size()];
        words.toArray(wordsArray);
        newDictRepo.insert(wordsArray);
        stopWords.stream().forEach(word->{
            newStopWordsCollection.insert(Document.createDocument("key",word));
        });
        newNitriteDB.commit();
        newNitriteDB.close();
    }
}
