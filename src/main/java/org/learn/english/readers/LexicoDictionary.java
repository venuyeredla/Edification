package org.learn.english.readers;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LexicoDictionary extends DictionaryReaderBase {

	private static final Logger LOGGER = LoggerFactory.getLogger(LexicoDictionary.class);
	private static String URL="https://www.lexico.com/en/definition/";
	public String getOrigin(String word) {
		 try {
			Document document = Jsoup.connect(URL+word).get();
			String origin = document.getElementsByClass("senseInnerWrapper").text();
			 LOGGER.info("Lexico :: {} -> {} ",word,origin);
			return origin;
		} catch (IOException e) {
			 LOGGER.error("Lexico :: {}",word);
		}
		 return  null;
	}
	
}
