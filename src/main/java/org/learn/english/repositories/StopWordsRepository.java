package org.learn.english.repositories;


import org.dizitart.no2.Document;
import org.dizitart.no2.NitriteCollection;
import org.dizitart.no2.filters.Filters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class StopWordsRepository {
    @Autowired
    @Qualifier("stopWordsCollection")
    NitriteCollection stopWordsCollection;

    public void cleanSaveAllStopWords(Set<String> stopWords){
        stopWordsCollection.remove(Filters.ALL);
        stopWords.stream().forEach(word->{
            stopWordsCollection.insert(Document.createDocument("key",word));
        });
    }

    public void saveAllStopWords(Set<String> stopWords){
        stopWords.stream().forEach(word->{
            stopWordsCollection.insert(Document.createDocument("key",word));
        });
    }

    public Set<String> getStopWords(){
        org.dizitart.no2.Cursor stopWordsCursor = stopWordsCollection.find();
        Set<String> stopWords=new HashSet<>();
        for (Document d:stopWordsCursor){
            String key = (String)d.get("key");
            stopWords.add(key);
        }
        return stopWords;
    }

}
