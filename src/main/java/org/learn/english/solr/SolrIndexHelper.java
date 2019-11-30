package org.learn.english.solr;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;
import org.learn.english.models.GWord;
import org.learn.english.models.Word;
import org.learn.english.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.google.gson.Gson;

@Service
public class SolrIndexHelper {

	@Autowired
	private HttpSolrClient httpSolrClient;
	Gson gson=new Gson();

	public void indexWebsterDictonary() {
		try {
			Optional<String> webSterData = FileUtil.readFileAsString(FileUtil.DIR+"VenuDict.json");
			if(webSterData.isPresent()){
				Map<String, String> orginMap = loadGdictionary();
				
				Word[] words = gson.fromJson(webSterData.get(), Word[].class);
				List<SolrInputDocument> docs=new ArrayList<SolrInputDocument>();
				Arrays.asList(words).stream().forEach(word->{
					SolrInputDocument doc=new SolrInputDocument();
					doc.addField("id", "dict-"+word.getWord());
					doc.addField("word_s", word.getWord());
					doc.addField("word", word.getWord());
						doc.addField("meanings", word.getMeanings());
					if(Objects.nonNull(word.getSyn()) && !CollectionUtils.isEmpty(word.getSyn())){
						List<String> synonyms = word.getSyn().stream().map(StringUtils::lowerCase).collect(Collectors.toSet()).stream().map(StringUtils::capitalize).collect(Collectors.toList());
						doc.addField("synonyms", synonyms);
						word.setSyn(synonyms);
					}
					if(Objects.nonNull(word.getAnt()) && !CollectionUtils.isEmpty(word.getAnt())){
						List<String> antonyms = word.getAnt().stream().map(StringUtils::lowerCase).collect(Collectors.toSet()).stream().map(StringUtils::capitalize).collect(Collectors.toList());
						doc.addField("antonyms", antonyms);
						word.setAnt(antonyms);
					}
					doc.addField("type", "Dictionary");
					char charAt = word.getWord().trim().charAt(0);
					doc.addField("alphabet", charAt);
					String origin = orginMap.get(word.getWord());
					
					if (Objects.nonNull(origin)) {
						doc.addField("origin", origin);
						word.setOrigin(origin);
					}
					docs.add(doc);
					if(docs.size()>=5000) {
						try {
							httpSolrClient.add(docs);
							docs.clear();
						} catch (SolrServerException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
					}
					
				});
				System.out.println("No of documents indexed ::"+docs.size());
				httpSolrClient.add(docs);
				httpSolrClient.commit();
				httpSolrClient.close();
				FileUtil.writeData(gson.toJson(words), FileUtil.DIR+"Generated//VenuDict.json");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	  public Map<String,String> loadGdictionary() throws IOException{
	        Optional<String> googleData = FileUtil.readFileAsString(FileUtil.DIR+"GoogleDict1.json");
	        Map<String,String> originMap=new HashMap<>();
	        if(googleData.isPresent()){
	        	
	            GWord[] words = gson.fromJson(googleData.get(), GWord[].class);
	           long count= Arrays.asList(words).stream().filter(gWord -> Objects.nonNull(gWord.getOrigin())).count();

	            Arrays.asList(words).stream().filter(gWord -> Objects.nonNull(gWord.getOrigin())).forEach(gWord ->{
	               System.out.println(gWord.getWord()+"------"+gWord.getOrigin().replace("?","'"));
	                originMap.put(StringUtils.lowerCase(gWord.getWord()), gWord.getOrigin().replace("?","'"));
	            });
	            System.out.println("size "+count);
	            
	           // Files.writeFile(gson.toJson(originMap),new File(FileUtil.DIR + "origins.json"));
	        }
	        
	       return originMap;

	        }
	
	
	
	
	
	
	


	public void mergeNew() throws IOException {
		Optional<String> webSterData = FileUtil.readFileAsString(FileUtil.DIR+"Latest\\VenuDict.json");
		Optional<String> antSynsData = FileUtil.readFileAsString(FileUtil.DIR+"Latest\\remainedAntsSyns.json");
		if(webSterData.isPresent() && antSynsData.isPresent()){
			Word[] updatedWebsters=gson.fromJson(webSterData.get(),Word[].class);
			Word[] newSynAnts = gson.fromJson(antSynsData.get(), Word[].class);
			Map<String, Word> synAntMap = new HashMap<>();
			Arrays.asList(newSynAnts).stream().forEach(sn->{
				String wdasdfa = sn.getWord().trim().replace("_", "");
				synAntMap.put(StringUtils.lowerCase(wdasdfa),sn);
			});
			List<Word> updatedList = Arrays.stream(updatedWebsters).map(word -> {

				Word synAnt = synAntMap.remove(word.getWord());
				//System.out.println("Merging word ::"+ word.getWord());
				if (synAnt != null) {
					if(Objects.nonNull(synAnt.getAnt())){
						if(Objects.isNull(word.getAnt())){
							word.setAnt(synAnt.getAnt());
						}else {
							word.getAnt().addAll(synAnt.getAnt());
						}
					}
					if(Objects.nonNull(synAnt.getSyn())) {
						if (Objects.isNull(word.getSyn())) {
							word.setSyn(synAnt.getSyn());
						} else {
							word.getSyn().addAll(synAnt.getSyn());
						}
					}
				}
				return word;
			}).collect(Collectors.toList());
			
			FileUtil.writeData(gson.toJson(gson.toJson(updatedList)), FileUtil.DIR+"Generated\\VenuDict.json");
			System.out.println("Remained size ::"+synAntMap.values().size());
			FileUtil.writeData(gson.toJson(synAntMap.values()), FileUtil.DIR+"Generated\\remainedAntsSyns.json");
		}
	}

    public void indexFile(){
		Optional<String> dataOptional = FileUtil.readFileAsString("Phrasalverbs.json");
		dataOptional.ifPresent(data ->{
			List<SolrInputDocument> docs=new ArrayList<SolrInputDocument>();
			List dataList = gson.fromJson(data, List.class);
			dataList.forEach(map ->{
				SolrInputDocument doc=new SolrInputDocument();
				Map dataMap=(Map) map;
				String word =(String) dataMap.get("word");
				doc.addField("id", "Phrasalverb--"+word);
				doc.addField("word", dataMap.get("word"));
				doc.addField("examples", Arrays.asList(dataMap.get("example")));
				doc.addField("meanings", Arrays.asList(dataMap.get("meaning")));
				doc.addField("type", "Phrasalverb");
				char charAt = word.trim().charAt(0);
				docs.add(doc);
			});

			System.out.println("No of documents :: "+docs.size());
			try {
				httpSolrClient.add(docs);
				httpSolrClient.commit();
				httpSolrClient.close();
			} catch (SolrServerException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		});
	}

	public void readANTextFile() throws IOException {
		List<String> lines = Files.lines(Paths.get(FileUtil.DIR + "AntSyn.txt")).collect(Collectors.toList());
        List<Word> words=new ArrayList<>();
		String[] linesArray = lines.toArray(new String[lines.size()]);
		Word word=null;
		for(int i=0;i<lines.size();){
			String line=linesArray[i];
			if(line.contains("KEY:")){
				word=new Word();
				word.setWord(this.cleanWord(line.replace("KEY:","")));

				line=linesArray[++i];
				String synonym=null;
				if(line.contains("SYN:")){
					synonym=line.replace("SYN:","");
					line=linesArray[++i];
					while(!line.contains("ANT:")&& !StringUtils.isBlank(line)){
						synonym=synonym+line;
						line=linesArray[++i];
					}
				}
				String antonym=null;
				if(line.contains("ANT:")){
					 antonym=line.replace("ANT:","");
					if(i<lines.size()-1){
						line=linesArray[++i];
						while(!StringUtils.isEmpty(line)){
							antonym=antonym+line;
							line=linesArray[++i];
						}
					}
				}

                if(Objects.nonNull(synonym)){
					word.setSyn(Arrays.asList(synonym.split(",")).stream().map(str-> this.cleanWord(str)).collect(Collectors.toList()));
				}

				if(Objects.nonNull(antonym)){
					word.setAnt(Arrays.asList(antonym.split(",")).stream().map(str->this.cleanWord(str)).collect(Collectors.toList()));
				}


				words.add(word);

			}
				i++;
		}
		System.out.println("Words size : :: "+words.size());
		FileUtil.writeData(gson.toJson(words), FileUtil.DIR+"Dicts\\newAntsSyns.json");
	}

	private  String cleanWord(String str){
	       str=str.replace(".","").trim();
	return  str;
}


	public void exportDictionary(){
		try {
			SolrQuery solrQuery=new SolrQuery();
			solrQuery.setQuery("-origin:*");
			solrQuery.setFields("word");
			solrQuery.setRows(102217);
			QueryResponse response = httpSolrClient.query(solrQuery);
			List<String> webSterWordsList = response.getResults().stream().map(doc -> {
				String word = (String) doc.getFirstValue("word");
				return word;
			}).collect(Collectors.toList());
			
			Collections.sort(webSterWordsList);
			FileUtil.writeData(gson.toJson(webSterWordsList), FileUtil.DIR+"nonRoots.json");
			httpSolrClient.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
