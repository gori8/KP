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
    cena:null,
    username:null,
    uuid:null
  };

  private hidden=false;

  private planovi=[];

  constructor(private activatedRoute: ActivatedRoute,
    private endpoints:EndpointsService,private router: Router,
    private authenticationService:AuthenticationService) { }

  ngOnInit() {
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

  setBody(plan) {
    this.body.cena=plan.cena;
    this.body.period=plan.period;
    this.body.ucestalostPerioda=plan.ucestalostPerioda;
    this.body.uuid=plan.uuid;
 }

  onSubmit(form:NgForm){
    this.hidden=true;
    if(form.valid===true){
      this.endpoints.submitSubscription(this.body).subscribe(
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
