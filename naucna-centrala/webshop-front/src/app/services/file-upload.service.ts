import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class FileUploadService {

  private endpoint = 'http://localhost:9000/scientific-work';
  private headers = new HttpHeaders();

  constructor(private http: HttpClient) { }

  postFile(fileToUpload: File): Observable<any> {
    const formData: FormData = new FormData();
    formData.append('file', fileToUpload, fileToUpload.name);
   
    return this.http.post(this.endpoint+"/pdf", formData,{
      headers: new HttpHeaders({
        'responseType': 'text'
      })
    });
  }

  getPdf(filename): Observable<any>{
    this.headers = this.headers.set('Content-Type', 'application/pdf');
    return this.http.get(`${this.endpoint}/pdf/`+filename,{headers: this.headers,
    responseType: 'blob'});
  }
}
