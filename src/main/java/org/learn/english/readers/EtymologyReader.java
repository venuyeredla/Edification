package org.learn.english.readers;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.lang3.StringUtils;
import org.dizitart.no2.Document;
import org.dizitart.no2.NitriteCollection;
import org.learn.english.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import javax.annotation.PostConstruct;

@Service
public class EtymologyReader {

	private static final Logger LOGGER = LoggerFactory.getLogger(EtymologyReader.class);

	@Autowired
	AmericanHeritage americanHeritage;
	@Autowired
	OxfordDictionary oxfordDictionary;
	@Autowired
	WebsterDictionary websterDictionary;
	@Autowired
	GDictionary gDictOriginReader;
	@Autowired
	LexicoDictionary lexicoDictionary;

	Gson gson=new Gson();
	@Autowired
	@Qualifier("originWordsCollection")
	NitriteCollection nitriteCollection;

	@PostConstruct
	public void init(){

	}


	public void readOrigins() {
		Optional<String> readFileAsString = FileUtil.readFileAsString(FileUtil.DIR+"nonRoots.json");
		LOGGER.info("Starting origin Reader..");
    	readFileAsString.ifPresent(data->{
    		Gson gson=new Gson();
    		String[] words = gson.fromJson(data, String[].class);
			LOGGER.info("Total words size :: {} ",words.length);
    	    LinkedBlockingQueue<String> queue=new LinkedBlockingQueue<String>();
    	    Arrays.asList(words).stream().forEach(w-> queue.add(w));
    	    ExecutorService executorService=Executors.newFixedThreadPool(5);
    	    americanHeritage.queue=queue;
			americanHeritage.nitriteCollection=nitriteCollection;
			lexicoDictionary.queue=queue;
			lexicoDictionary.nitriteCollection=nitriteCollection;
    	    oxfordDictionary.queue=queue;
			oxfordDictionary.nitriteCollection=nitriteCollection;
    	    websterDictionary.queue=queue;
			websterDictionary.nitriteCollection=nitriteCollection;
			gDictOriginReader.queue=queue;
			gDictOriginReader.nitriteCollection=nitriteCollection;
    	    executorService.submit(americanHeritage);
    	    executorService.submit(lexicoDictionary);
    	    executorService.submit(oxfordDictionary);
    	    executorService.submit(websterDictionary);
			executorService.submit(gDictOriginReader);
    	    executorService.shutdown();
    	   while(!executorService.isTerminated()) {
    		   try {
				Thread.sleep(500000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	   }
    	});
	}


	public Map<String,String> readOrigins(List<String> wordsList){
         Map<String,String> origins=new HashMap<>();
         wordsList.stream().forEach(w->{
			 String origin = gDictOriginReader.getOrigin(w);
			 if(StringUtils.isBlank(origin)){
				 origin = americanHeritage.getOrigin(w);
			 }
			 if(StringUtils.isBlank(origin)){
				 origin = oxfordDictionary.getOrigin(w);
			 }
			 if(StringUtils.isBlank(origin)){
				 origin = websterDictionary.getOrigin(w);
			 }

			 if(StringUtils.isBlank(origin)){
				 origin = lexicoDictionary.getOrigin(w);
			 }
			 origins.put(w,origin);
			 nitriteCollection.insert(Document.createDocument("key",w).put("origin",origin));
		 });
		return  origins;
	}

}
