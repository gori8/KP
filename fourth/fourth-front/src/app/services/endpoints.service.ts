import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';

const BASE_URL = "https://192.168.43.161:8771/fourth/api/fourth/execute";

@Injectable({
  providedIn: 'root'
})
export class EndpointsService {

  constructor(private http: HttpClient) { }

  paymentConfirmed(formModel): Observable<any>{
    return this.http.post(`${BASE_URL}`, formModel,{
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'responseType': 'text'
      })
    });
  }
}
