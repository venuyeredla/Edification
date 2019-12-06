package org.learn.english.repositories;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.learn.english.config.EnglishTestConfig;
import org.learn.english.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes= {EnglishTestConfig.class})
public class StopWordsRepoTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(EnglishRepository.class);

    @Autowired
    StopWordsRepository stopWordsRepository;

    @Test
    @Ignore
    public void testSaveStopwords(){
        Set<String> stopWords = FileUtil.loadStopWords();
        stopWordsRepository.saveAllStopWords(stopWords);
    }

    @Test
    public void testGetStopwords(){
        Set<String> stopWords = stopWordsRepository.getStopWords();
        LOGGER.info("No of stop words = {}",stopWords.size());
        stopWords.forEach(System.out::println);
    }

}
