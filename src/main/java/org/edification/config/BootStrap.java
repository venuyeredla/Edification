package org.edification.config;

import com.google.gson.Gson;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.NitriteCollection;
import org.dizitart.no2.objects.ObjectRepository;
import org.edification.models.Clause;
import org.edification.models.DictWord;
import org.edification.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "org.edification")
public class BootStrap {

    @Bean
    public Nitrite eruditeDB(){
        Nitrite nitriteDB = Nitrite.builder().compressed()
                            .filePath(FileService.getDBPath("erudite.db"))
                            .openOrCreate("erudite", "erudite");
        return nitriteDB;
    }

    @Bean(name = "dictionaryRepo")
    public ObjectRepository<DictWord> dictionaryRepo(@Autowired Nitrite eruditeDB){
        ObjectRepository<DictWord> dictionaryRepo = eruditeDB.getRepository("dictionary",DictWord.class);
        return dictionaryRepo;
    }

    @Bean(name = "clausesRepo")
    public ObjectRepository<Clause> clausesRepo(@Autowired Nitrite eruditeDB){
        ObjectRepository<Clause> clausesRepo = eruditeDB.getRepository("clauses", Clause.class);
        return clausesRepo;
    }

    @Bean
    public HttpSolrClient httpSolrClient(){
        HttpSolrClient httpSolrClient=new HttpSolrClient.Builder("http://localhost:8983/solr/dict").build();  //
        return  httpSolrClient;
    }
    
    @Bean(name="stopWordsCollection")
    public NitriteCollection stopWordsCollection(@Autowired Nitrite nitriteDB){
        NitriteCollection collection = nitriteDB.getCollection("stopwords");
        return collection;
    }

    @Bean
    public Gson gson(){
        return new Gson();
    }
}