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
    return this.http.get(`${BASE_URL}/api/bitcoin/prepare`);
  }
}
