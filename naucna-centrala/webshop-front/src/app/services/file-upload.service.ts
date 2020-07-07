import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class FileUploadService {

  private endpoint = 'http://localhost:9000/scientific-work';

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
}
