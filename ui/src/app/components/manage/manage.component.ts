import { Component, OnInit } from '@angular/core';
import { SearchService } from 'src/app/services/search.service';
import { ApiService } from 'src/app/services/api.service';

@Component({
  selector: 'app-manage',
  templateUrl: './manage.component.html',
  styleUrls: ['./manage.component.css']
})
export class ManageComponent implements OnInit {

    newWords: any;
    knownWords: any;


  constructor(private searchService: SearchService, private apiService: ApiService) { }

     ngOnInit() {
       this.newWords=new Array();
    }

      loadFromFile() {
        let url = "http://localhost:2020/edification/api/review/import";
        console.log("Loading knowns from file.")
      }
      writeToFile() {
        const url = "http://localhost:2020/edification/api/review/export";
        window.open(url);
      }

      goToKnown() {
        window.open('http://localhost:2020/edification/api/review/known');
      }

      getNewWords(){
         this.apiService.getNewWords().subscribe(data => {
              this.newWords=data;
          });
      }

      addKnown(idx) {
        let word = this.newWords[idx];
        let wordList = word.split('--');
        console.log('Adding :: ' + wordList[0]);
        this.newWords.splice(idx, 1)
        this.apiService.addKnown(wordList[0]).subscribe(data => { console.log('Addded ? ' + data)});
      }


     getDefinition() {
       window.open('http://localhost:2020/edification/api/dict/define/abjectly');
      }

     importDictionary(){
       let url = 'http://localhost:2020/edification/api/dict/import'
     }

     exportDictionary(){
       let url = 'http://localhost:2020/edification/api/dict/export'
       window.open(url);
     }

     dictionarySize() {
       let url = 'http://localhost:2020/edification/api/dict/dictsize';
     }

     indexDictionary(){
       let url = 'http://localhost:2020/edification/api/dict/indexdict'
     }

     indexClauses(){
       let url = 'http://localhost:2020/edification/api/dict/import'
     }

}
