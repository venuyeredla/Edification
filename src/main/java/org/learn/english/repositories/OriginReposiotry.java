package org.learn.english.repositories;


import org.dizitart.no2.Document;
import org.dizitart.no2.NitriteCollection;
import org.dizitart.no2.filters.Filters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class OriginReposiotry {
    @Autowired
    @Qualifier("originWordsCollection")
    NitriteCollection originWordsCollection;

    public void updateOrigin(String key,String value){
        originWordsCollection.update(Filters.eq("key",key), Document.createDocument("key",key).put("origin",value));
    }

    public Map<String,String> getOrigins(){
        org.dizitart.no2.Cursor documents = originWordsCollection.find();
        Map<String,String> originMap=new HashMap<>();
        for (Document d:documents){
            String word = (String)d.get("key");
            String origin = (String)d.get("origin");
            originMap.put(word,origin);
        }
        System.out.println("Total size :: "+originMap.size());
        return originMap;
    }

    public  void deleteAll(){
        originWordsCollection.remove(Filters.ALL);
    }

}
