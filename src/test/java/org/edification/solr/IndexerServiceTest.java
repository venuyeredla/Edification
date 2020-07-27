package org.edification.solr;

import org.edification.config.BootStrapTest;
import org.edification.service.IndexerService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes= {BootStrapTest.class})
public class IndexerServiceTest {
	@Autowired
   private IndexerService indexerService;


	@Test
	@Ignore
	public void testGetWords(){
		List<String> words = indexerService.getWords("gress");
		words.stream().forEach(System.out::println);
	}

	@Test
	@Ignore
	public void deleteDictionary(){
		indexerService.deleteByQuery("type:Dictionary");
	}

	@Test
	@Ignore
	public void testRegex(){
		List<String> samples=new ArrayList<>();
		samples.add("A nitrogenous base C10H9N, extracted from coal-tar naphtha, as an oily liquid. It is a member of the quinoline series, and is probably identical with lepidine.");
		samples.add("In the United States, a commission appointed by the President, consisting of three members, not more than two of whom may be adherents of the same party, which has the control, through examinations, of appointments and promotions in the classified civil service. It was created by act of Jan, 16, 1883 (22 Stat. 403).");
		samples.forEach(meanings->{
			Arrays.asList(meanings.split("[1-9][.]")).stream().forEach(System.out::println);
		});
	}

}
