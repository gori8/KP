import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';

const BASE_URL = "http://localhost:9000/webshop";
const SW_URL = 'http://localhost:9000/scientific-work';


@Injectable({
  providedIn: 'root'
})
export class EndpointsService {
  
  constructor(private http: HttpClient) { }

  startProcess(): Observable<any>{
    return this.http.get(`${BASE_URL}/registration`);
  }

  startNewPaperProcess(username): Observable<any>{
    return this.http.get(`${BASE_URL}/newPaper/${username}`);
  }

  getCorrectionData(processId): Observable<any>{
    return this.http.get(`${BASE_URL}/correction/${processId}`);
  }

  getUrednikeRecenzente(processId): Observable<any>{
    return this.http.get(`${BASE_URL}/uredniciRecenzenti/${processId}`);
  }

  getOblasti(): Observable<any>{
    return this.http.get(`${BASE_URL}/sciencefields`);
  }

  getPlacanja(): Observable<any>{
    return this.http.get(`${BASE_URL}/payments`);
  }

  submitRegistration(formModel,taskId): Observable<any>{
    return this.http.post(`${BASE_URL}/registration/${taskId}`, formModel,{
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'responseType': 'text'
      })
    });
  }

  submitNewPaper(formModel,processId): Observable<any>{
    return this.http.post(`${BASE_URL}/newPaper/${processId}`, formModel,{
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'responseType': 'text'
      })
    });
  }

  getCasopis(id): Observable<any>{
    return this.http.get(`${BASE_URL}/papers/${id}`);
  }

  getNumbersForCasopis(id): Observable<any>{
    return this.http.get(`${BASE_URL}/papers/${id}/numbers`);
  }

  getNumbersForCasopisAndKupac(casopisId,username): Observable<any>{
    return this.http.get(`${BASE_URL}/bought/${username}/${casopisId}`);
  }

  submitIzdanje(izdanje): Observable<any>{
    return this.http.post(`${BASE_URL}/numbers`, izdanje,{
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      })
    });
  }

  submitRad(rad): Observable<any>{
    return this.http.post(`${BASE_URL}/scientificWorks`, rad,{
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      })
    });
  }

  submitUredniciRecenzenti(body,processId): Observable<any>{
    return this.http.post(`${BASE_URL}/newPaper/uredniciRecenzenti/${processId}`, body,{
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'responseType': 'text'
       })
    });
  }

  submitPlans(body,id): Observable<any>{
    return this.http.post(`${BASE_URL}/newPaper/plans/${id}`, body,{
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
       })
    });
  }

  aktivacijaCaspisa(body): Observable<any>{
    return this.http.post(`${BASE_URL}/potvrdaCasopisa`,body,{
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      })
    });
  }

  getValidation(processId): Observable<any>{
    return this.http.get(`${BASE_URL}/registration/validation/${processId}`);
  }

  activateAccount(processId): Observable<any>{
    return this.http.post(`${BASE_URL}/registration/confirmation/${processId}`,null,{
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'responseType': 'text'
      })
    });
  }

  potvrdaRecenzenta(dto): Observable<any>{
    return this.http.post(`${BASE_URL}/potvrdaRecenzenta`,dto,{
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      })
    });
  }

  getNumberOfTasks(username): Observable<any>{
    return this.http.get(`${BASE_URL}/numberOfTasks/${username}`);
  }

  getPlansForPaper(id): Observable<any>{
    return this.http.get(`${BASE_URL}/plans/${id}`);
  }

  submitSubscription(body,type): Observable<any>{

    if(type=="paypal"){
      return this.http.post(`${BASE_URL}/paypalSubscription/init`,body, {
        headers: new HttpHeaders({
          'Content-Type': 'application/json',
          'responseType': 'text'
        })
      });
    }else{
      return this.http.post(`${BASE_URL}/pay/${body.uuid}/${body.username}`,null, {
        headers: new HttpHeaders({
          'Content-Type': 'application/json',
          'responseType': 'text'
        })
      });
    }
  }

  getTasks(username): Observable<any>{
    return this.http.get(`${BASE_URL}/tasks/${username}`);
  }

  getMyPapers(username): Observable<any>{
    return this.http.get(`${BASE_URL}/myPapers/${username}`);
  }

  getAllPapers(): Observable<any>{
    return this.http.get(`${BASE_URL}/papers`);
  }

  getBoughtPapers(username): Observable<any>{
    return this.http.get(`${BASE_URL}/boughtPapers/${username}`);
  }

  getBoughtItems(username): Observable<any>{
    return this.http.get(`${BASE_URL}/bought/${username}`);
  }

  getTasksLinks(username): Observable<any>{
    return this.http.get(`${BASE_URL}/tasksLinks/${username}`);
  }

  getTasksPlans(username): Observable<any>{
    return this.http.get(`${BASE_URL}/tasksPlans/${username}`);
  }

  public pay(uapId,username): Observable<any>{

    return this.http.post(`${BASE_URL}/pay/${uapId}/${username}`,null, {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'responseType': 'text'
      })
    });
  }

  public addScientificWork(dto): Observable<any>{
    return this.http.post(SW_URL,dto,{
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      })
    });
  }

  public getNaucneOblastiByIzdanjeId(izdanjeId): Observable<any>{
    return this.http.get(`${SW_URL}/naucneOblasti/${izdanjeId}`);
  }

  public searchByField(dto): Observable<any>{
    return this.http.post(`${SW_URL}/simple`,dto,{
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      })
    });
  }

  public searchByBool(dto): Observable<any>{
    return this.http.post(`${SW_URL}/bool`,dto,{
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      })
    });
  }
}
