package org.learn.english.readers;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class WebsterDictionary extends DictionaryReaderBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebsterDictionary.class);

    private String URL = "https://www.merriam-webster.com/dictionary/";

    public String getOrigin(String word) {
        try {
            Document document = Jsoup.connect(URL + word).get();
            String origin = document.getElementsByClass("et").text();
            LOGGER.info("WebsterDictionary :: {} -> {} ",word,origin);
            return origin;
        } catch (IOException e) {
            LOGGER.error("WebsterDictionary :: {}",word);
        }
        return null;
    }
}
