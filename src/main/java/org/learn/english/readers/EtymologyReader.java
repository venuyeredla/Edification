package org.learn.english.readers;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.dizitart.no2.NitriteCollection;
import org.learn.english.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

@Service
public class EtymologyReader {


	private static final Logger LOGGER = LoggerFactory.getLogger(EtymologyReader.class);

	Gson gson=new Gson();
	@Autowired
	@Qualifier("originWordsCollection")
	NitriteCollection nitriteCollection;

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

    	    AmericanHeritage americanHeritage=new AmericanHeritage();
    	    americanHeritage.queue=queue;
			americanHeritage.nitriteCollection=nitriteCollection;

    	    LexicoDictionary lexico=new LexicoDictionary();
    	    lexico.queue=queue;
			lexico.nitriteCollection=nitriteCollection;

    	    OxfordDictionary oxfordDictionary=new OxfordDictionary();
    	    oxfordDictionary.queue=queue;
			oxfordDictionary.nitriteCollection=nitriteCollection;

    	    WebsterDictionary websterDictionary=new WebsterDictionary();
    	    websterDictionary.queue=queue;
			websterDictionary.nitriteCollection=nitriteCollection;

			GDictionary gDictOriginReader=new GDictionary();
			gDictOriginReader.queue=queue;
			gDictOriginReader.nitriteCollection=nitriteCollection;

    	    executorService.submit(americanHeritage);
    	    executorService.submit(lexico);
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

}
