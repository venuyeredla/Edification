package org.learn.english.config;


import org.apache.solr.client.solrj.impl.HttpSolrClient;
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
}
