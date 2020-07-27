import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { SolrQuery } from '../model/solr-query';
import { DictWord } from '../model/dict-word';
import { DictWordMeaning } from '../model/dict-word-meaning';

@Injectable({
  providedIn: 'root'
})
export class SearchService {

  constructor(private httpClient: HttpClient) { }

  public searchSolr(solrQuery: SolrQuery): Observable<any> {
    let queryUrl = '/solr/dict/select?facet=on&facet.field=type&df=word&indent=off&rows=100&wt=json&q=' + solrQuery.query;  // &sort=word_s+asc
    if (solrQuery.filter) {
      let fqObj = solrQuery.filter;
      queryUrl = queryUrl + '&fq=' + fqObj;
    }
    return this.httpClient.get(queryUrl);
  }

  public transform(data, solrResponse) {
    let response = data['response'];
    let docs = response['docs'];
    let dictWords = solrResponse.initDocs(docs.length);
    // console.log(docs) ;
    for (let i = 0; i < docs.length; i++) {
      let doc = docs[i];
      let dictWord = new DictWord();
      let type = doc['type'];
      dictWord.type = type;
      // console.log(dictWord.type)
      if (type == 'Dictionary') {
            dictWord.word = doc['word'][0];
            if (doc['origin']) {
              dictWord.origin = doc['origin'][0];
            } else {
              dictWord.origin = '';
            }
            // console.log(dictWord);
            let meaningsString = doc['meanings'][0];
            let meanings: DictWordMeaning[] = JSON.parse(meaningsString);
            dictWord.isfull=doc['isfull'];
            dictWord.isknown=doc['isknown'];
            dictWord.meanings = meanings;
      } else {
        dictWord.word = doc['phrase'][0];
        dictWord.means = doc['meanings'];
        dictWord.examples = doc['examples'];
      }
      dictWords[i] = dictWord;
      // console.log(meanings);
      // console.log(dictWord);
    }
    this.transformFacets(data, solrResponse);
  }

  transformFacets(data, solrResponse) {
    console.log('Transforming facets');
    let facetCounts = data['facet_counts'];
    let facets = new Array();
    if (facetCounts) {
      let facetFields = facetCounts['facet_fields'];
      if (facetFields) {
        let typeFacets = facetFields['type'];
        if (typeFacets) {
          for (let i = 0; i < typeFacets.length; i = i + 2) {
            let label = typeFacets[i];
            let count = typeFacets[i + 1];
            let facet = { 'field': 'type', 'label': label, 'count': count };
            facets.push(facet);
          }
        }
      }
    }
    solrResponse.facets = facets;
  }






}
