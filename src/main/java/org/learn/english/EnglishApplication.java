package org.learn.english;

import org.learn.english.readers.EtymologyReader;
import org.learn.english.readers.GDictionary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EnglishApplication /*implements CommandLineRunner*/ {

	private static final Logger LOGGER = LoggerFactory.getLogger(EnglishApplication.class);

	@Autowired
	GDictionary gDictionary;

	@Autowired
	EtymologyReader etymologyReader;
	public static void main(String[] args) {
		SpringApplication.run(EnglishApplication.class, args);
	}
	/*@Override
	public void run(String... args) throws Exception {
		LOGGER.info("Missing origin : {} -- {}","venu","origin");
	//	googleDictionaryReader.exportGoogleDict();
		etymologyReader.readOrigins();
	}*/
}
