package org.edification.service;

import com.google.gson.Gson;
import org.dizitart.no2.Cursor;
import org.dizitart.no2.Document;
import org.dizitart.no2.NitriteCollection;
import org.dizitart.no2.filters.Filters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
public class TermsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TermsService.class);

    @Autowired
    @Qualifier("stopWordsCollection")
    NitriteCollection stopWordsCollection;

    @Autowired
    FileService fileService;

    @Autowired
    Gson gson;

    public boolean loadStopWords(){
        Set<String> stopWords = fileService.loadStopWords();
        if(!CollectionUtils.isEmpty(stopWords)){
            stopWords.stream().forEach(stopword-> stopWordsCollection.insert(Document.createDocument("key",stopword)));
            return  true;
        }
        return  false;
    }

    public Set<String> getStopWords(){
        Cursor stopWordsCursor = stopWordsCollection.find();
        Set<String> stopWords=new HashSet<>();
        for (Document d:stopWordsCursor){
            String key = (String)d.get("key");
            stopWords.add(key);
        }
        return stopWords;
    }

    public boolean addNewWord(String newWord){
        Cursor resultCursor = stopWordsCollection.find(Filters.eq("key", newWord));
        if(resultCursor.size()==0){
            stopWordsCollection.insert(Document.createDocument("key",newWord));
            return true;
        }
        return false;
    }
    public boolean doesExisits(String newWord){
        Cursor resultCursor = stopWordsCollection.find(Filters.eq("key", newWord));
        if(resultCursor.size()==0){
            return false;
        }else if(resultCursor.size()>=0){
            return true;
        }
        return false;
    }

    public String writeKnownWordsTofile() {
        Set<String> stopWords = this.getStopWords();
        String filePath = this.fileService.writeKnownWordsTofile(stopWords);
        return filePath;
    }
}