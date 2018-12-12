import{Adress} from "././adress";
import{Phone} from "././phone";
import { from } from "rxjs";

export interface User{
    id: number;
    name: string;
    adress: Adress;
    phones: Phone[];
}