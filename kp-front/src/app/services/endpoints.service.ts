import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';

const BASE_URL_USER_AND_PAYMENT = "http://localhost:8090/api";

@Injectable({
  providedIn: 'root'
})

export class EndpointsService {

  constructor(private http: HttpClient) { }

  getNacinePlacanjaZaCasopis(casopisId): Observable<any>{

    return this.http.get(`${BASE_URL_USER_AND_PAYMENT}/payment/${casopisId}`);
  }

  callSelectedMicroservice(url): Observable<any>{
    
    return this.http.post(`http://localhost:8092${url}`,{casopisId:1,amount:2000}, {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'responseType': 'text'
      })
    });
  }
}
