import { Component, OnInit, ElementRef, ViewChild } from '@angular/core';

declare var paypal;


@Component({
  selector: 'app-paypal-component',
  templateUrl: './paypal-component.component.html',
  styleUrls: ['./paypal-component.component.scss']
})
export class PaypalComponentComponent implements OnInit {
  @ViewChild('paypal', { static: true }) paypalElement: ElementRef;


  constructor() { }

  ngOnInit() {
    paypal.Button.render({
      env: 'sandbox', // Or 'production'
      // Set up the payment:
      // 1. Add a payment callback
      payment: function(data, actions) {
        console.log(data);
        // 2. Make a request to your server
        return fetch('http://localhost:8090/api/paypal',{
          method:'post',
          body:JSON.stringify({
            casopisID:1,
            email:'sb-4t1vg791084@business.example.com',
            redirectUrl:'blabla'
          }),
          headers:{
            'Accept': 'application/json',
            'Content-Type':'application/json'
          }
        })
          .then(res => {
            if (res.ok) {
              return res.json().then(json => {
                return json.id;
              });
            }
            // 3. Return res.id from the response
            //window.location.href=res.approvalUrl;
            //return res.id;
          }, err => {
            console.log(err);
          });
      },
      // Execute the payment:
      // 1. Add an onAuthorize callback
      onAuthorize: function(data, actions) {
        // 2. Make a request to your server
        return fetch('http://localhost:8090/api/paypal/execute',{ 
          method:'post',
          body:JSON.stringify({
              paymentID: data.paymentID,
              payerID:   data.payerID
          }),
          headers:{
            'Accept': 'application/json',
            'Content-Type':'application/json'
          }
        })
          .then(function(res) {
            // 3. Show the buyer a confirmation message.
          });
      }
    },'paypal');
  }

}
