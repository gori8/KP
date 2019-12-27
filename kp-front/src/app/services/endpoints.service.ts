import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';

const BASE_URL_USER_AND_PAYMENT = "http://localhost:8090/api";
const BASE_MICROSERVICE_URL = "http://localhost:8093";

@Injectable({
  providedIn: 'root'
})

export class EndpointsService {

  constructor(private http: HttpClient) { }

  getNacinePlacanjaZaCasopis(casopisId): Observable<any>{

    return this.http.get(`${BASE_URL_USER_AND_PAYMENT}/payment/${casopisId}`);
  }

  callSelectedMicroservice(url,uuid): Observable<any>{
    
    return this.http.post(`${BASE_MICROSERVICE_URL}${url}`,{casopisId:uuid,amount:2000}, {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'responseType': 'text'
      })
    });
  }
}
