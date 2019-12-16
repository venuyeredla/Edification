package org.learn.english.repositories;


import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.learn.english.config.EnglishTestConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes= {EnglishTestConfig.class})
public class OriginRepositoryTest {

    @Autowired
    OriginReposiotry originReposiotry;

    @Test
    public void testOrigins(){
        Map<String, String> originsMap = originReposiotry.getOrigins();
        System.out.println("Size ::"+originsMap.size());
        originsMap.forEach((k,v)->{ System.out.println(k+" --> "+v);});
    }

    @Test
    @Ignore
    public void updateOrigin(){
        originReposiotry.updateOrigin("egression","Mid 16th century from Latin egressus ‘gone out’, from the verb egredi, from ex- ‘out’ + gradi ‘to step’.");
    }

    @Test
    @Ignore
    public  void emptyOrigins(){
        originReposiotry.deleteAll();
    }
}
