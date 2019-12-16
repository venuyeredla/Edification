package org.learn.english.repositories;

import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.dizitart.no2.Document;
import org.dizitart.no2.WriteResult;
import org.dizitart.no2.objects.Cursor;
import org.dizitart.no2.objects.ObjectRepository;
import org.dizitart.no2.objects.filters.ObjectFilters;
import org.learn.english.models.Word;
import org.learn.english.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class EnglishRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnglishRepository.class);

    @Autowired
    ObjectRepository<Word> dictRepository;
    @Autowired
    Gson gson;


    public List<Word> readDictionary(){
        Cursor<Word> dictCursor = dictRepository.find();
        List<Word> words=new ArrayList<>();
        for(Word word:dictCursor){
            words.add(word);
        }
        LOGGER.info("No of words in dictionary :: {}",words.size());
        return words;
    }

    public void writeToFile(){
        List<Word> dicionaryWords = this.readDictionary();
        FileUtil.writeData(gson.toJson(dicionaryWords),FileUtil.DIR+"exported\\dictionary.json");
    }


    public void deleteAll(){
        dictRepository.remove(ObjectFilters.ALL);
    }

    public void saveAll(List<Word> words){
         Word[] wordsArray=new Word[words.size()];
         words.toArray(wordsArray);
         dictRepository.insert(wordsArray);
    }
    public void saveAll(Word[] words){
        this.deleteAll();
        dictRepository.insert(words);
    }





   public List<Word> updateOrigins(Map<String,String> originMap){
        List<Word> tobeIndexed=new ArrayList<>();
        originMap.forEach((key,origin)->{
            origin= StringUtils.isBlank(origin)?"Unknown":origin;
            WriteResult writeResult = dictRepository.update(ObjectFilters.eq("word", key), Document.createDocument("origin", origin));
            Cursor<Word> wordsCursor = dictRepository.find(ObjectFilters.eq("word", key));
            for(Word w:wordsCursor){
                tobeIndexed.add(w);
            }
        });
        return tobeIndexed;
   }
}
