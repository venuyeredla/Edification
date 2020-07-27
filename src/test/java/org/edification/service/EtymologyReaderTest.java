package org.edification.service;

import org.edification.config.BootStrapTest;
import org.edification.crawler.EtymologyReader;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes= {BootStrapTest.class})
public class EtymologyReaderTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(EtymologyReaderTest.class);


    @Autowired
    TermsService stopWordsService;

    @Autowired
    EtymologyReader etymologyReader;







    @Test
    @Ignore
    public void testGetStopwords(){
        Set<String> stopWords = stopWordsService.getStopWords();
        LOGGER.info("No of stop words = {}",stopWords.size());
        stopWords.forEach(System.out::println);
    }
}
