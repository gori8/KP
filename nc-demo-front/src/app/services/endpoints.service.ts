import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';

const BASE_URL = "https://localhost:9000/nc";

@Injectable({
  providedIn: 'root'
})
export class EndpointsService {

  constructor(private http: HttpClient) { }

  public getCasopise(): Observable<any>{

    return this.http.get(`${BASE_URL}/casopis`);
  }

  public getCasopis(uuid): Observable<any>{

    return this.http.get(`${BASE_URL}/casopis/${uuid}`);
  }

  public pay(uapId): Observable<any>{

    return this.http.post(`${BASE_URL}/pay/${uapId}`,null, {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'responseType': 'text'
      })
    });
  }
}
