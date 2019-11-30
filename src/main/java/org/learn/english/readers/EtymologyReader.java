package org.learn.english.readers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.learn.english.util.FileUtil;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

@Service
public class EtymologyReader {
	
	Gson gson=new Gson();
	
	public void readOrigins() {
		Optional<String> readFileAsString = FileUtil.readFileAsString(FileUtil.DIR+"nonRoots.json");
    	readFileAsString.ifPresent(data->{
    		Gson gson=new Gson();
    		String[] words = gson.fromJson(data, String[].class);
    	    LinkedBlockingQueue<String> queue=new LinkedBlockingQueue<String>();
    	    Arrays.asList(words).stream().forEach(w-> queue.add(w));
    	    ExecutorService executorService=Executors.newFixedThreadPool(4);
    	    
    	    Map<String,String> origins=new HashMap<>();
    	    
    	    AmericanHeritage americanHeritage=new AmericanHeritage();
    	    americanHeritage.queue=queue;
    	    Lexico lexico=new Lexico();
    	    lexico.queue=queue;
    	    OxfordDictionary oxfordDictionary=new OxfordDictionary();
    	    oxfordDictionary.queue=queue;
    	    WebsterDictionary websterDictionary=new WebsterDictionary();
    	    websterDictionary.queue=queue;
    	    executorService.submit(americanHeritage);
    	    executorService.submit(lexico);
    	    executorService.submit(oxfordDictionary);
    	    executorService.submit(websterDictionary);
    	    
    	    executorService.shutdown();
    	   while(!executorService.isTerminated()) {
    		   try {
				Thread.sleep(500000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	   }
    	    FileUtil.writeData(gson.toJson(origins), FileUtil.DIR+"roots.json");
    	    
    	});
		
	}

}
