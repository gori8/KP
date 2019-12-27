import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';

const BASE_URL = "http://localhost:8093";

@Injectable({
  providedIn: 'root'
})
export class EndpointsService {

  constructor(private http: HttpClient) { }

  callMicroservice(): Observable<any>{
    return this.http.post(`${BASE_URL}/api/bitcoin/prepare`,{uuid:"30b751e4-41c7-41fd-9a8c-c52665acee9c",amount:1,redirectUrl:"http://localhost:4200"}
    ,{
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'responseType': 'text'
      })
    }
    );
  }
}
