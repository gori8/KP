import { Component, OnInit } from '@angular/core';
import { EndpointsService } from 'src/app/services/endpoints.service';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { DataService } from 'src/app/services/data.service';

@Component({
  selector: 'app-admin-tasks',
  templateUrl: './admin-tasks.component.html',
  styleUrls: ['./admin-tasks.component.scss']
})
export class AdminTasksComponent implements OnInit {

  private tasks = [];
  private tasksLinks = [];
  private numberOfTasks=0;
  
  constructor(private endpoints:EndpointsService,private authenticationService: AuthenticationService,private data:DataService) { }

  ngOnInit() {

    this.data.currentNumberOfTasks.subscribe(numberOfTasks => this.numberOfTasks=numberOfTasks);
    this.getNumberOfTasks();
    this.getTasks();

    if(this.authenticationService.currentUserValue.role=="Urednik"){
      this.getTasksLinks();
    }
  }

  getTasksLinks(){
    this.endpoints.getTasksLinks(this.authenticationService.currentUserValue.username).subscribe(
      res => {
          this.tasksLinks = res;
        },
      err => {
        console.log(err); 
      }
    )
  }

  getNumberOfTasks(){
    var user = this.authenticationService.currentUserValue;
      if(user!=null && (user.role=="Administrator" || user.role=="Urednik")){
        this.endpoints.getNumberOfTasks(user.username).subscribe(
          res => {
              this.data.changeNumberOfTasks(res);
          },
          err => {
            console.log(err); 
          }
        )
      }
  }

  potvrdaRecenzenta(taskId,answer,index){
    
    var body = {
      taskId:taskId,
      answer:answer
    }
    
    this.endpoints.potvrdaRecenzenta(body).subscribe(
      res => {
        this.tasks.splice(index,1);
        this.data.substractNumberOfTasks(this.numberOfTasks);
      },
      err => {
        console.log(err); 
      }
    )
  }

  aktivacijaCasopisa(taskId,answer,index){
    var body = {
      taskId:taskId,
      answer:answer
    }
    
    this.endpoints.aktivacijaCaspisa(body).subscribe(
      res => {
        this.tasks.splice(index,1);
        this.data.substractNumberOfTasks(this.numberOfTasks);
      },
      err => {
        console.log(err); 
      }
    )
  }

  getTasks(){
    this.endpoints.getTasks(this.authenticationService.currentUserValue.username).subscribe(
      res => {
          this.tasks = res;
        },
      err => {
        console.log(err); 
      }
    )
  }

}
