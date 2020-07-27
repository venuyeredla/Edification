import { Component, OnInit,AfterViewInit } from '@angular/core';
import {SearchService} from '../../services/search.service'
import { ApiService } from 'src/app/services/api.service';

@Component({
  selector: 'app-reader',
  templateUrl: './reader.component.html',
  styleUrls: ['./reader.component.css']
})
export class ReaderComponent implements OnInit ,AfterViewInit  {
  container: HTMLElement; 

  sentences: string[]
  allSentences: string[]
  indexNum: number;
  totalLines: number;
  constructor(private searchService: SearchService, private apiService: ApiService) {
    this.sentences = new Array()
   }

   ngAfterViewInit() {         
    this.container = document.getElementById("readerContainer");           
    this.container.scrollTop = this.container.scrollHeight;     
  }  


  ngOnInit() {
    this.apiService.reader().subscribe(data=>{
      this.allSentences=data;
      this.totalLines = this.allSentences.length;
      if( this.totalLines > 0) {
       this.sentences.push(this.allSentences[0])
        this.indexNum=0;
      }
    });
  }

  nextSentence(){
    this.indexNum++;
    if(this.indexNum<this.totalLines){
      this.sentences.push(this.allSentences[this.indexNum]);
    }
    this.ngAfterViewInit();
  }

}
