package org.learn.english.solr;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegExTester {

    @Test
    public void testRegex(){
        List<String> samples=new ArrayList<>();
        samples.add("A nitrogenous base C10H9N, extracted from coal-tar naphtha, as an oily liquid. It is a member of the quinoline series, and is probably identical with lepidine.");
        samples.add("In the United States, a commission appointed by the President, consisting of three members, not more than two of whom may be adherents of the same party, which has the control, through examinations, of appointments and promotions in the classified civil service. It was created by act of Jan, 16, 1883 (22 Stat. 403).");
        samples.forEach(meanings->{
            Arrays.asList(meanings.split("[1-9][.]")).stream().forEach(System.out::println);
        });



    }
}
