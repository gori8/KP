import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';

const BASE_URL = "https://192.168.43.161:8091/payment";

@Injectable({
  providedIn: 'root'
})
export class EndpointsService {

  constructor(private http: HttpClient) { }

  paymentConfirmed(formModel,transaction): Observable<any>{
    return this.http.post(`${BASE_URL}/${transaction}`, formModel,{
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'responseType': 'text'
      })
    });
  }
}
