package org.edification.crawler;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EtymologyReader {
    private static final Logger LOGGER = LoggerFactory.getLogger(EtymologyReader.class);

    public static final String AHD = "https://ahdictionary.com/word/search.html?q=";
    public static String GDICT ="https://api.dictionaryapi.dev/api/v1/entries/en/";
    public static String LEXICO="https://www.lexico.com/en/definition/";
    public static String OXFORD="https://www.oxfordlearnersdictionaries.com/definition/english/";
    public static String WEBSTER = "https://www.merriam-webster.com/dictionary/";

    RestTemplate restTemplate=new RestTemplate();

    public Map<String,String> readOrigins(List<String> wordsList){
        Map<String,String> origins=new HashMap<>();
        wordsList.stream().forEach(w->{
            String   origin = this.americanHeritageOrigin(w);
            if(StringUtils.isBlank(origin)){
                origin = this.getOxfordOrigin(w);
            }
            if(StringUtils.isBlank(origin)){
                origin =this.getWebSterOrigin(w);
            }
            if(StringUtils.isBlank(origin)){
                origin = this.getLexicoOrigin(w);
            }
            origins.put(w,origin);
        });
        return  origins;
    }

    public String americanHeritageOrigin(String word) {
        try {
            Document document = Jsoup.connect(AHD +word).get();
            String origin = document.getElementsByClass("etyseg").text();
            LOGGER.info("AmericanHeritage :: {} -> {} ",word,origin);
            return origin;
        } catch (IOException e) {
            LOGGER.error("AmericanHeritage :: {}",word);
        }
        return null;
    }


    public String getLexicoOrigin(String word) {
        try {
            Document document = Jsoup.connect(LEXICO+word).get();
            String origin = document.getElementsByClass("senseInnerWrapper").text();
            LOGGER.info("Lexico :: {} -> {} ",word,origin);
            return origin;
        } catch (IOException e) {
            LOGGER.error("Lexico :: {}",word);
        }
        return  null;
    }

    public String getOxfordOrigin(String word) {
        try {
            Document document = Jsoup.connect(OXFORD+word).get();
            String origin = document.getElementsByClass("p").text();
            LOGGER.info("OxfordDictionary :: {} -> {} ",word,origin);
            return origin;
        } catch (IOException e) {
            LOGGER.error("OxfordDictionary :: {}",word);
        }
        return null;
    }

    public String getWebSterOrigin(String word) {
        try {
            Document document = Jsoup.connect(WEBSTER + word).get();
            String origin = document.getElementsByClass("et").text();
            LOGGER.info("WebsterDictionary :: {} -> {} ",word,origin);
            return origin;
        } catch (IOException e) {
            LOGGER.error("WebsterDictionary :: {}",word);
        }
        return null;
    }
}
