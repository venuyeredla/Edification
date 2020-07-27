package org.edification.service;

import org.dizitart.no2.objects.Cursor;
import org.dizitart.no2.objects.ObjectRepository;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.edification.config.BootStrapTest;
import org.edification.models.DictWord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes= {BootStrapTest.class})
public class DictionaryServiceTest {

    @Autowired
    DictionaryService dictionaryService;

    @Autowired
    @Qualifier("dictionaryRepo")
    ObjectRepository<DictWord> dictionaryRepo;

    @Test
    @Ignore
    public void loadDictionaryTest(){
        this.dictionaryService.loadDictionaryFromFile();
    }

    @Test
    @Ignore
    public void writeDictionaryTest(){
        this.dictionaryService.writeDictionaryToFile();
    }

    @Test
    @Ignore
    public void readDictionary(){
        Cursor<DictWord> googleDict = dictionaryRepo.find();
        ArrayList<String> wordsList=new ArrayList<>();
        googleDict.forEach(dictWord ->{
            //System.out.println(dictWord.getWord());
            wordsList.add(dictWord.getWord());
        });
        Map<String, Long> groupByCount = wordsList.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        groupByCount.forEach((k,v)->{
            if(v>1){
                System.out.println(k+" -- "+v);
            }
         });
        System.out.println("Number of entries in Google dict."+googleDict.size() +"  and in map ."+groupByCount.size());
    }

    @Test
    public void getDefinition(){
        List<DictWord> dictWords = this.dictionaryService.readDefintions("egregious");
        dictWords.stream().forEach(dictWord -> {
            System.out.println( dictWord.getWord()+"  "+dictWord.getOrigin());
        });
    }
}
