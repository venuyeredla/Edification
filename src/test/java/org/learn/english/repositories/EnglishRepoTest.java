package org.learn.english.repositories;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.learn.english.config.EnglishTestConfig;
import org.learn.english.models.Word;
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

    @Test
    @Ignore
    public void testRead(){
        List<Word> words = englishRepository.readDictionary();
        words.stream().map(word -> word.getWord()).forEach(System.out::println);
        LOGGER.info("No of words in dictionary = {}",words.size());
    }

    @Test
    @Ignore
    public void testOrigins(){
        Map<String, String> originsMap = englishRepository.getOrigins();
        originsMap.forEach((k,v)->{ System.out.println(k+" --> "+v);});
    }

    @Test
    @Ignore
    public void testSaveStopwords(){
        Set<String> stopWords = FileUtil.loadStopWords();
        englishRepository.saveAllStopWords(stopWords);
    }

    @Test
    public void testGetStopwords(){
        Set<String> stopWords = englishRepository.getStopWords();
        LOGGER.info("No of stop words = {}",stopWords.size());
        stopWords.forEach(System.out::println);
    }
}
