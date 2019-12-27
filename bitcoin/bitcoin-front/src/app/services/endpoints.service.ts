import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';

const BASE_URL = "http://localhost:8093";

@Injectable({
  providedIn: 'root'
})
export class EndpointsService {

  constructor(private http: HttpClient) { }

  callMicroservice(transaction, response): Observable<any>{
    return this.http.post(`${BASE_URL}/bitcoin/${transaction}/${response}`, {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'responseType': 'text'
      })
    });
  }
}
