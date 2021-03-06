import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { EndpointsService } from 'src/app/services/endpoints.service';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-select-plan',
  templateUrl: './select-plan.component.html',
  styleUrls: ['./select-plan.component.scss']
})
export class SelectPlanComponent implements OnInit {

  private body={
    period:null,
    ucestalostPerioda:null,
    cena:0,
    username:null,
    uuid:null
  };

  private initValue=null;

  private hidden=false;

  private planovi=[];

  private type=null;

  constructor(private activatedRoute: ActivatedRoute,
    private endpoints:EndpointsService,private router: Router,
    private authenticationService:AuthenticationService) { }

  ngOnInit() {
    this.type = this.activatedRoute.snapshot.paramMap.get('type')
    this.getPlans(this.activatedRoute.snapshot.paramMap.get('casopisId'));
    this.body.username=this.authenticationService.currentUserValue.username;
  }

  getPlans(casopisId) {
    this.endpoints.getPlansForPaper(casopisId).subscribe(
      res => {
        this.planovi = res;
      },
      err => {
        console.log(err); 
      }
    );
  }

  setBody(uuid) {
    let plan = this.planovi.find(element=>element.uuid==uuid)
    
    this.body.cena=plan.cena;
    this.body.period=plan.period;
    this.body.ucestalostPerioda=plan.ucestalostPerioda;
    this.body.uuid=plan.uuid;

    console.log(this.body);
    
 }

  onSubmit(form:NgForm){
    this.hidden=true;
    if(form.valid===true){
      this.endpoints.submitSubscription(this.body,this.type).subscribe(
        res => {
          window.location.href = res;
        },
        err => {
          console.log(err); 
          alert("An error has occured while subscibing");
          this.hidden=false;
        }
      );
    }
  }
}
