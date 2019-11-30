package org.learn.english.readers;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class OxfordDictionary implements Callable<String>{
	
 private static	String URL="https://www.oxfordlearnersdictionaries.com/definition/english/replenish?q=";

 LinkedBlockingQueue<String> queue=null;
 Map<String,String> origins=null;
 
@Override
public String call() throws Exception {
	while (!queue.isEmpty()) {
		 try {
			    String word = queue.take();
			    Document document = Jsoup.connect(URL+word).get();
				String origin = document.getElementsByClass("p").text();
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
		String origin = document.getElementsByClass("p").text();
		System.out.println(word+ "  -- "+origin);
	} catch (IOException e) {
		e.printStackTrace();
	}
}


}
