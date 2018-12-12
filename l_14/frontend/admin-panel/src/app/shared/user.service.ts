import {Injectable} from '@angular/core';
import {User} from '././user';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class UserService{
    API_URL_USER = 'http://localhost:8080/MyApp/rest/admin/';

    constructor(private httpClient: HttpClient) { }

    getUsers() {
        return this.httpClient.get(`${this.API_URL_USER}`);
    }

    getUserById(userId: number) {
      const params = new HttpParams().set('id', String(userId));
        return this.httpClient.get(`${this.API_URL_USER}`, { params: params });
    }

    save(user: User) {
        const headers = new HttpHeaders()
        .set('Content-Type', 'application/x-www-form-urlencoded');
        // .set('Content-Type', 'application/json');
        const body = new HttpParams()
        .set('user', JSON.stringify(user));
      this.httpClient.post(`${this.API_URL_USER}`, body, { headers })
        .subscribe(
          val => {
            console.log('POST call successful value returned in body',
              val);
          },
          response => {
            console.log('POST call in error', response);
          },
          () => {
            console.log('The POST observable is now completed.');
          }
        );
    }

    remove(id: number) {
      const params = new HttpParams().set('id', String(id));
        this.httpClient.delete(`${this.API_URL_USER}`, { params: params })
        .subscribe(
          val => {
            console.log('DELETE call successful value returned in body',
              val);
          },
          response => {
            console.log('DELETE call in error', response);
          },
          () => {
            console.log('The DELETE observable is now completed.');
          }
        );
    }
}