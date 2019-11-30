package org.learn.english.Readers;

import org.junit.Test;
import org.learn.english.readers.AmericanHeritage;
import org.learn.english.readers.Lexico;
import org.learn.english.readers.OxfordDictionary;
import org.learn.english.readers.WebsterDictionary;

public class AmericanHeritageTest {
	
	@Test
	public void americanHeritage() {
		AmericanHeritage americanHeritage=new AmericanHeritage();
		americanHeritage.readOrigin("ingress");
		
	}
	
	@Test
	public void oxfordDictionary() {
		OxfordDictionary americanHeritage=new OxfordDictionary();
		americanHeritage.readOrigin("replenish");
		
	}
	
	@Test
	public void lexico() {
		Lexico americanHeritage=new Lexico();
		americanHeritage.readOrigin("verity");
		
	}
	
	@Test
	public void Webster() {
		WebsterDictionary americanHeritage=new WebsterDictionary();
		americanHeritage.readOrigin("transgress");
		
	}

}
