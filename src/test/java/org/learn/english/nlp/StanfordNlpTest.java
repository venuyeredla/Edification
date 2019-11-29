package org.learn.english.nlp;

import org.junit.Test;

public class StanfordNlpTest {

    @Test
    public void test(){
        StanfordNLPUtil nlpUtil =  new StanfordNLPUtil() ;
        // stanfordNLPUtil.posTagging("C:\\Work\\Books\\Vocabulary\\PrideAndPrejudice.txt");
       // stanfordNLPUtil.listUnknown("C:\\Work\\Books\\Vocabulary\\PrideAndPrejudice.txt");
        nlpUtil.getLemmas();

    }

    @Test
    public void testPipelineDemo(){
        StanfordNLPUtil nlpUtil =  new StanfordNLPUtil() ;
        nlpUtil.pipelineDemo(null);

    }
}
