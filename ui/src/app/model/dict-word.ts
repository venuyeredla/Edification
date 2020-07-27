import { DictWordMeaning } from './dict-word-meaning';

export class DictWord {
    word: string;
    origin: string;
    meanings: DictWordMeaning[];
    type: string;
    isfull: boolean;
    isknown: boolean;
    examples: string[];
    means: string[];
}
