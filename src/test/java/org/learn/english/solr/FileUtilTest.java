package org.learn.english.solr;

import org.junit.Ignore;
import org.junit.Test;
import org.learn.english.util.FileUtil;

public class FileUtilTest {
	
	
	@Test
	@Ignore
	public void sortTest() {
		FileUtil.sortWords();
	}

	@Test
	public void stopWords() {

		FileUtil.loadStopWords();
	}

}
