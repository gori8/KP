import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { EndpointsService } from 'src/app/services/endpoints.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-banka-form',
  templateUrl: './banka-form.component.html',
  styleUrls: ['./banka-form.component.scss']
})
export class BankaFormComponent implements OnInit {

  constructor(private endpoints: EndpointsService,private activatedRoute: ActivatedRoute) {
  }

  private hidden = false;

  formModel = {
    pan:null,
    holderName:null,
    securityCode:null,
    validTo:null
  }

  transaction="";

  ngOnInit() {
    this.transaction = this.activatedRoute.snapshot.paramMap.get('transaction');
    console.log(this.transaction);
  }

  onSubmit(paymentForm:NgForm){
    this.hidden=true;
    if(paymentForm.valid===true){
      this.endpoints.paymentConfirmed(this.formModel,this.transaction).subscribe(
        res => {
            console.log(res);
            window.location.href = res.url;
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
