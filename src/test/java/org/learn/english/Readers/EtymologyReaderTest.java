package org.learn.english.Readers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.learn.english.config.EnglishTestConfig;
import org.learn.english.readers.EtymologyReader;
import org.learn.english.solr.DictionaryIndexer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes= {EnglishTestConfig.class})
public class EtymologyReaderTest {
    @Autowired
    EtymologyReader etymologyReader;
    @Autowired
    DictionaryIndexer dictionaryIndexer;

    @Test
    public void getOrigins(){
        List<String> words = dictionaryIndexer.getWords("noxi");
        Map<String, String> originMap = etymologyReader.readOrigins(words);
        originMap.forEach((k,v)->{
            System.out.println(k+" -> "+v);
        });

    }
}
