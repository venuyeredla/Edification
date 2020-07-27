package org.edification.service;

import com.google.gson.Gson;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;
import org.edification.models.Clause;
import org.edification.models.DictWord;
import org.edification.models.Sentence;
import org.edification.nlp.NlpAnalysisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IndexerService {
	private static final Logger LOGGER = LoggerFactory.getLogger(IndexerService.class);

	@Autowired
	private HttpSolrClient httpSolrClient;

	@Autowired
	Gson gson;

	HttpSolrClient sentencesSolrClient=null;
	
	@Autowired
	NlpAnalysisService nlpAnalysis;
	
	 @Autowired
	 TermsService stopWordsService;

	@PostConstruct
	public  void init(){
		sentencesSolrClient=new HttpSolrClient.Builder("http://localhost:8983/solr/sentences").build();
	}

	public void indexDictionary(List<DictWord> dictWords){
		try {
			List<SolrInputDocument> docs=new ArrayList<SolrInputDocument>();
			dictWords.stream().forEach(word->{
				SolrInputDocument doc=new SolrInputDocument();
				doc.addField("id", "dict-"+word.getWord());
				doc.addField("word_s", word.getWord());
				doc.addField("word", word.getWord());
				doc.addField("origin", word.getOrigin());
				doc.addField("type", "Dictionary");
				char charAt = word.getWord().trim().charAt(0);
				doc.addField("alphabet", charAt+"");

				doc.addField("prefix", null);
				doc.addField("root", null);
				doc.addField("suffix", null);
				doc.addField("isknown", word.isKnown());
				doc.addField("isfull", word.isFull());
				doc.addField("importance", 0);
				String jsonString = gson.toJson(word.getMeanings());
				doc.addField("meanings", jsonString);

				docs.add(doc);
				if(docs.size()>=500) {
					try {
						httpSolrClient.add(docs);
						LOGGER.info("Writing docs of size -- {}",docs.size());
						docs.clear();
					} catch (SolrServerException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			LOGGER.info("Writing last set of docs -- {}",docs.size());
			httpSolrClient.add(docs);
			httpSolrClient.commit();
			//httpSolrClient.close();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	
	public void indexToSolr(List<Clause> clauses){
        try {
            List<SolrInputDocument> docs=new ArrayList<SolrInputDocument>();
                clauses.stream().forEach(clause->{
                SolrInputDocument doc=new SolrInputDocument();
                doc.addField("id", "clause-"+clause.getClause());
                doc.addField("phrase", clause.getClause());
                doc.addField("type", clause.getType());
                doc.addField("examples",clause.getExamples());
                doc.addField("meanings",clause.getMeanings());
                doc.addField("importance", 0);
                docs.add(doc);
                if(docs.size()>=500) {
                    try {
                        httpSolrClient.add(docs);
                        LOGGER.info("Writing docs of size -- {}",docs.size());
                        docs.clear();
                    } catch (SolrServerException | IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
            LOGGER.info("Writing last set of docs -- {}",docs.size());
            httpSolrClient.add(docs);
            httpSolrClient.commit();
            //httpSolrClient.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

	public void indexSentences(List<Sentence> sentences){
		try {
			nlpAnalysis.getDirSentences("C:\\work\\wenglish\\scripts\\");
			List<SolrInputDocument> docs=new ArrayList<SolrInputDocument>();
			sentences.stream().forEach(sentence->{
				SolrInputDocument doc=new SolrInputDocument();
				doc.addField("id", sentence.getSeries()+"-"+sentence.getEpisode()+"-"+sentence.getSentence());
				doc.addField("sentence", sentence.getSentence());
				doc.addField("after", sentence.getAfter());
				doc.addField("before", sentence.getBefore());
				doc.addField("series", sentence.getSeries());
				doc.addField("episode", sentence.getEpisode());
				docs.add(doc);
				/*	try {
						httpSolrClient.add(docs);
						LOGGER.info("Writing docs of size -- {}",docs.size());
						docs.clear();
					} catch (SolrServerException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				LOGGER.info("Writing last set of docs -- {}",docs.size());
				sentencesSolrClient.add(docs)*/;
			});
			LOGGER.info("Writing last set of docs -- {}",docs.size());
			sentencesSolrClient.add(docs);
			//httpSolrClient.close();
		}catch (Exception e){
			e.printStackTrace();
		}
	}


	public List<String> getWords(String queryTerm){
		try {
			SolrQuery solrQuery=new SolrQuery();
			solrQuery.setQuery(queryTerm);
			solrQuery.setFilterQueries("type:Dictionary");
			solrQuery.setFields("word");
			solrQuery.setRows(102217);
			QueryResponse response = httpSolrClient.query(solrQuery);
			List<String> wordsList = response.getResults().stream().map(doc -> {
				String word = (String) doc.getFirstValue("word");
				return word;
			}).collect(Collectors.toList());
		//	httpSolrClient.close();
			return wordsList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

  	public void deleteByQuery(String query) {
		try {
			LOGGER.info("Deleting index with query = "+query);
			httpSolrClient.deleteByQuery(query);
			httpSolrClient.commit();
			//	httpSolrClient.close();
			LOGGER.info("Deleting done sucessfully.");
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
  	
}
