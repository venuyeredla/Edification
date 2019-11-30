package org.learn.english;

import org.learn.english.readers.GoogleDictionaryReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EnglishApplication implements CommandLineRunner {

	@Autowired
	GoogleDictionaryReader googleDictionaryReader;
	public static void main(String[] args) {
		SpringApplication.run(EnglishApplication.class, args);
	}
	@Override
	public void run(String... args) throws Exception {
		googleDictionaryReader.exportGoogleDict();
	}
}
