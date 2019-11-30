package org.learn.english.readers;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

@Component
public class AmericanHeritage implements Callable<String>{

	private static final String URL = "https://ahdictionary.com/word/search.html?q=";

	LinkedBlockingQueue<String> queue=null;
	Map<String,String> origins=null;
	@Override
	public String call() throws Exception {
		
		while (!queue.isEmpty()) {
			 try {
				    String word = queue.take();
					Document document = Jsoup.connect(URL+word).get();
					String origin = document.getElementsByClass("etyseg").text();
					if(origin!=null) {
						origins.put(word, origin);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		
		return null;
	}
	
	public void readOrigin(String word) {
		 try {
		Document document = Jsoup.connect(URL+word).get();
		 String origin = document.getElementsByClass("etyseg").text();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

}
