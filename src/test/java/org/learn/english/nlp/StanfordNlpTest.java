package org.learn.english.nlp;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.learn.english.config.EnglishTestConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Scanner;
import java.util.Set;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes= {EnglishTestConfig.class})
public class StanfordNlpTest {
    @Autowired
    StanfordNLPUtil stanfordNLPUtil;

    @Test
    @Ignore
    public void test(){
        // stanfordNLPUtil.posTagging("C:\\Work\\Books\\Vocabulary\\PrideAndPrejudice.txt");
        //stanfordNLPUtil.listUnknown("C:\\Work\\Books\\Vocabulary\\PrideAndPrejudice.txt");
        stanfordNLPUtil.getLemmas();
    }

    @Test
    public void tokenizeText(){
        stanfordNLPUtil.buildStopWords();
    }


    @Test
    @Ignore
    public void testPipelineDemo(){
        stanfordNLPUtil.pipelineDemo(null);
    }
}
