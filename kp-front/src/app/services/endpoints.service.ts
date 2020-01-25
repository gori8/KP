import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';

const BASE_URL_USER_AND_PAYMENT = "https://localhost:8771/userandpayment/api";

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

  public getJSON(name): Observable<any> {
    return this.http.get(`${BASE_URL_USER_AND_PAYMENT}/form/${name}`);
  }

  public registerOnMs(body,msUrl): Observable<any>{

    return this.http.post(msUrl,body, {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      })
    });
  } 

  public getImage(folder,name) : Observable<any> {
    return this.http.get(`${BASE_URL_USER_AND_PAYMENT}/image/${folder}/${name}`);
  }

  public paymentRegistrationCompleted(body): Observable<any>{

    return this.http.post(`${BASE_URL_USER_AND_PAYMENT}/registration/complete`,body, {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'responseType': 'text'
      })
    });
  }
}
