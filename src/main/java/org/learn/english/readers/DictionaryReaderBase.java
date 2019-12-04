package org.learn.english.readers;

import org.dizitart.no2.Document;
import org.dizitart.no2.NitriteCollection;
import org.dizitart.no2.WriteResult;

import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;

import static org.dizitart.no2.Document.createDocument;

public abstract class DictionaryReaderBase implements Callable<String> {

    LinkedBlockingQueue<String> queue=null;
    NitriteCollection nitriteCollection;
    @Override
    public String call() throws Exception {
        while (!queue.isEmpty()) {
            try {
                String word = queue.take();
                String origin=this.getOrigin(word);
                if(origin!=null) {
                    Document doc = createDocument("word", word)
                                                .put("origin", origin);
                    WriteResult insertedDoc = nitriteCollection.insert(doc);
                }
                Thread.sleep(5000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public abstract String getOrigin(String word);
}
