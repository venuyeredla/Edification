package org.learn.english.Readers;

import org.junit.Test;
import org.learn.english.readers.AmericanHeritage;
import org.learn.english.readers.LexicoDictionary;
import org.learn.english.readers.OxfordDictionary;
import org.learn.english.readers.WebsterDictionary;

public class DictionaryReadersTest {
	
	@Test
	public void americanHeritage() {
		AmericanHeritage americanHeritage=new AmericanHeritage();
		americanHeritage.getOrigin("ingress");
		
	}
	
	@Test
	public void oxfordDictionary() {
		OxfordDictionary oxfordDictionary=new OxfordDictionary();
		oxfordDictionary.getOrigin("replenish");
		
	}
	
	@Test
	public void lexico() {
		LexicoDictionary lexico=new LexicoDictionary();
		lexico.getOrigin("verity");
		
	}
	
	@Test
	public void Webster() {
		WebsterDictionary websterDictionary=new WebsterDictionary();
		websterDictionary.getOrigin("transgress");
		
	}

}
