import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { EndpointsService } from 'src/app/services/endpoints.service';

@Component({
  selector: 'app-paypal-subscription',
  templateUrl: './paypal-subscription.component.html',
  styleUrls: ['./paypal-subscription.component.scss']
})
export class PaypalSubscriptionComponent implements OnInit {

  private body={
    type:"INFINITE",
    numCicles:0,
    planId:null
  }

  private plan={
    period:null,
    ucestalostPerioda:null,
    cena:null
  }

  private hidden = false;

  constructor(private activatedRoute: ActivatedRoute, private endpoints: EndpointsService) { }

  ngOnInit() {
    this.body.planId=this.activatedRoute.snapshot.paramMap.get('uuid');
    this.getPlan(this.body.planId);
  }

  getPlan(uuid){
    this.endpoints.getPlan(uuid).subscribe(
      res => {
          this.plan = res;
      },
      err => {
        console.log(err);  
      }
    );
  }

  onSubmit(form){
    this.hidden=true;
    if(form.valid===true){
      if(this.body.type=="INFINITE"){
        this.body.numCicles=0;
      }
      console.log(this.body);
    
      this.endpoints.paypalSubscription(this.body).subscribe(
        res => {
            console.log(res);
            window.location.href = res;
        },
        err => {
          this.hidden=false;
          console.log(err);  
        }
      );
    }
  }

}
