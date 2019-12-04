package org.learn.english.readers;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AmericanHeritage extends DictionaryReaderBase {

	private static final Logger LOGGER = LoggerFactory.getLogger(AmericanHeritage.class);

	private static final String URL = "https://ahdictionary.com/word/search.html?q=";

	@Override
	public String getOrigin(String word) {
		try {
			Document document = Jsoup.connect(URL+word).get();
			String origin = document.getElementsByClass("etyseg").text();
			LOGGER.info("AmericanHeritage :: {} -> {} ",word,origin);
			return origin;
		} catch (IOException e) {
			LOGGER.error("AmericanHeritage :: {}",word);
		}
		return null;
	}
}
