import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';

const BASE_URL_USER_AND_PAYMENT = "https://localhost:8771/userandpayment/api";
const BASE_MICROSERVICE_URL = "http://localhost:8093";

@Injectable({
  providedIn: 'root'
})

export class EndpointsService {

  constructor(private http: HttpClient) { }

  getNacinePlacanjaZaCasopis(casopisId): Observable<any>{

    return this.http.get(`${BASE_URL_USER_AND_PAYMENT}/method/${casopisId}`);
  }

  callSelectedMicroservice(url,uuid): Observable<any>{
    
    return this.http.post(`${url}`,{casopisUuid:uuid}, {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'responseType': 'text'
      })
    });
  }
}
