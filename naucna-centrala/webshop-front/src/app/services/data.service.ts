import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DataService {

  private numberOfTasks = new BehaviorSubject<number>(0);
  currentNumberOfTasks = this.numberOfTasks.asObservable();

  constructor() { }

  substractNumberOfTasks(numberOfTasks){
    this.numberOfTasks.next(numberOfTasks-1);
  }

  changeNumberOfTasks(numberOfTasks){
    this.numberOfTasks.next(numberOfTasks);
  }
}
