import { DictWord } from './dict-word';

export class SolrResponse {
    dictWords : DictWord[];  
    facets : Object[];

    initDocs(size:number){
        this.dictWords=new Array(size);
        return this.dictWords;
    }

}
