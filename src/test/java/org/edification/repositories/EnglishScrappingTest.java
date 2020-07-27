package org.edification.repositories;

import org.junit.Test;
import org.edification.crawler.EnglishScrapping;

public class EnglishScrappingTest {

    @Test
    public void crawlScript(){
        EnglishScrapping englishScrapping=new EnglishScrapping();
        englishScrapping.getScript("suits",9,16);
    }
}
