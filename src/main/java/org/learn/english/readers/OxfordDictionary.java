package org.learn.english.readers;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class OxfordDictionary  extends DictionaryReaderBase {

	private static final Logger LOGGER = LoggerFactory.getLogger(OxfordDictionary.class);
	
 private static	String URL="https://www.oxfordlearnersdictionaries.com/definition/english/";

public String getOrigin(String word) {
	 try {
		Document document = Jsoup.connect(URL+word).get();
		String origin = document.getElementsByClass("p").text();
		 LOGGER.info("OxfordDictionary :: {} -> {} ",word,origin);
		return origin;
	} catch (IOException e) {
		 LOGGER.error("OxfordDictionary :: {}",word);
	}
	 return null;
}
}
