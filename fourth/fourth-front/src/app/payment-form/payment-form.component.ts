import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NgForm } from '@angular/forms';
import { EndpointsService } from '../services/endpoints.service';

@Component({
  selector: 'app-payment-form',
  templateUrl: './payment-form.component.html',
  styleUrls: ['./payment-form.component.scss']
})
export class PaymentFormComponent implements OnInit {

  private hidden = false;

  formModel = {
    buyerId:null,
    uuid:null,
  }

  constructor(private endpoints: EndpointsService,private activatedRoute: ActivatedRoute) { }

  ngOnInit() {
    this.formModel.uuid = this.activatedRoute.snapshot.paramMap.get('uuid');
  }

  onSubmit(paymentForm:NgForm){
    if(paymentForm.valid===true){
      this.hidden=true;
      this.endpoints.paymentConfirmed(this.formModel).subscribe(
        res => {
            console.log(res);
            window.location.href = res;
        },
        err => {
          this.hidden=false;
          console.log(err); 
          alert("An error has occured while confirming a payment");
        }
      )
    }
  }

}
