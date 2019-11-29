package org.learn.english.solr;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.learn.english.config.EnglishTestConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.URISyntaxException;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes= {EnglishTestConfig.class})
public class SolrIndexHelperTest {
	@Autowired
   private SolrIndexHelper solrHelper;

	@Test
	public void indexWebsterTest() {
		solrHelper.indexWebsterDictonary();
	}
	@Test
	@Ignore
	public void indexFile() {
		solrHelper.indexFile();
	}


	@Test
	@Ignore
	public void merge() throws IOException {
		SolrIndexHelper dictionaryIndexer=new SolrIndexHelper();
		//dictionaryIndexer.merge();
		//dictionaryIndexer.readANTextFile();
		//dictionaryIndexer.mergeNew();
	}


	@Test
	@Ignore
	public void testDeleteIndex() throws SolrServerException, IOException, URISyntaxException {
		HttpSolrClient httpSolrClient=new HttpSolrClient.Builder("http://localhost:8983/solr/Dictionary").build();
		httpSolrClient.deleteByQuery("type:Dictionary");
		httpSolrClient.commit();
		httpSolrClient.close();
	}

}
