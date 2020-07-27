import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private basePath: string;

  constructor(private httpClient: HttpClient) {
    this.basePath = '/edification/api/'
   }

 public reader(): Observable<any> {
    return this.httpClient.get(this.basePath + 'read');
  }

  public getNewWords(): Observable<any> {
      return this.httpClient.get(this.basePath + 'review/listnewwords');
  }

  public getKnownWords(): Observable<any> {
    return this.httpClient.get(this.basePath + 'review/getknown');
}
  public addKnown(word: string): Observable<any> {
    return this.httpClient.get(this.basePath + 'review/addnew/' + word);
  }
}