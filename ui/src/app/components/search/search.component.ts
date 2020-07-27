import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Observable, from } from 'rxjs';
import { SearchService } from '../../services/search.service'
import { SolrResponse } from 'src/app/model/solr-response';
import { SolrQuery } from 'src/app/model/solr-query';
import { DictWord } from 'src/app/model/dict-word';

export interface State {
  flag: string;
  name: string;
  population: string;
}

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {
  stateCtrl = new FormControl();
  filteredStates: Observable<State[]>;
  solrQuery: SolrQuery;
  solrResponse: SolrResponse;
  toRead: string[]
  constructor(private searchService: SearchService) {
  }

  ngOnInit() {
    this.solrResponse = new SolrResponse();
    this.solrQuery = new SolrQuery();
    this.listToRead();
    this.searchSolr();
  }

  onChooseTheWord(word) {
    this.solrQuery.query = word;
    this.newSearch();
  }

  applyFilter(filter) {
    this.solrQuery.filter = filter.field + ':' + filter.label;
    this.searchSolr();
  }

  removeFilter() {
    this.solrQuery.filter = '';
    this.searchSolr();
  }

  editWord(doc) {
    console.log(doc.readDef);
  }

  getDef(doc) {
    console.log(doc);
  }

  newSearch() {
    this.solrQuery.filter = '';
    this.searchSolr();
  }

  readDef(doc: DictWord) {
    //http://localhost:2020/edification/api/readDef/cuddle
    var url = 'http://localhost:2020/edification/api/dict/readDef/' + doc.word;
    window.open(url);
  }

  searchSolr() {
    this.searchService.searchSolr(this.solrQuery).subscribe(data => {
      this.searchService.transform(data, this.solrResponse);
    });
  }

  listToRead() {
    this.toRead = ['avarice',
      'subversive',
      'desperate',
      'sensitively',
      'assiduously',
      'doughty',
      'ditident',
      'beatific',
      'strip',
      'disorganize',
      'gloomy',
      'anachronsstic',
      'inarticulate',
      'execrable',
      'conglomeration',
      'bitterly',
      'bilk',
      'epic',
      'imprecation',
      'colossal',
      'howl',
      'duress',
      'browbeat',
      'arbiter',
      'indifferent',
      'curse',
      'emanate',
      'deceptive',
      'diligently',
      'dour',
      'compulsion',
      'gigantic',
      'swindle',
      'rebellious',
      'unesthetic',
      'embodiment',
      'dexteriiy',
      'meanness',
      'blissful',
      'abominable',
      'stingy',
      'denounce',
      'outdate',
      'devout',
      'divest',
      'bake',
      'stinginess',
      'embittered',
      'antagonize',
      'bankrupt',
      'correciness',
      'gregarious'
    ]

    let numberOfWords = this.toRead.length;
    let randomNum = Math.floor((Math.random() * numberOfWords) + 1);
    this.solrQuery.query = this.toRead[randomNum];
  }



}


