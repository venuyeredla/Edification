package org.edification.config;


import com.google.gson.Gson;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;
import org.edification.models.DictWord;
import org.edification.service.DictionaryService;
import org.edification.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

//@Configuration
//@ComponentScan(basePackages = "org.edification")
public class BootStrapTest {

    @Bean
    public Gson gson(){
        return new Gson();
    }

    @Bean
    public Nitrite englishDB(){
        Nitrite nitriteDB = Nitrite.builder().compressed()
                .filePath(FileService.getDBPath("erudite.db")).openOrCreate("erudite", "erudite");
        return nitriteDB;
    }

    @Bean(name = "dictionaryRepo")
    public ObjectRepository<DictWord> dictionaryRepo(@Autowired Nitrite googleDB){
        ObjectRepository<DictWord> gDictRepo = googleDB.getRepository("dictionary",DictWord.class);
        return gDictRepo;
    }

    @Bean
    public DictionaryService dictionaryService(){
        return  new DictionaryService();
    }


}
