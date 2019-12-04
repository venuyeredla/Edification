package org.learn.english.config;


import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.NitriteCollection;
import org.dizitart.no2.objects.ObjectRepository;
import org.learn.english.models.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "org.learn.english")
public class EnglishConfig {

    @Bean
    public HttpSolrClient httpSolrClient(){
        HttpSolrClient httpSolrClient=new HttpSolrClient.Builder("http://localhost:8983/solr/Dictionary").build();
        return  httpSolrClient;
    }

    @Bean
    public Nitrite nitriteDb(){
        Nitrite nitriteDB = Nitrite.builder()
                .compressed()
                .filePath("data/eng.db")
                .openOrCreate("english", "english");
        return nitriteDB;
    }

    @Bean(name = "dictionaryRepo")
    public ObjectRepository<Word> dictReposiotry(@Autowired Nitrite nitriteDB){
        ObjectRepository<Word> dictionaryRepository = nitriteDB.getRepository(Word.class);
        return dictionaryRepository;
    }

    @Bean(name="originWordsCollection")
    public NitriteCollection originWordsCollection(@Autowired Nitrite nitriteDB){
        NitriteCollection collection = nitriteDB.getCollection("origins");
        return collection;
    }
    @Bean(name="stopWordsCollection")
    public NitriteCollection stopWordsCollection(@Autowired Nitrite nitriteDB){
        NitriteCollection collection = nitriteDB.getCollection("stopwords");
        return collection;
    }

}
