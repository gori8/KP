import { Component, OnInit, ElementRef, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

declare var paypal;


@Component({
  selector: 'app-paypal-component',
  templateUrl: './paypal-component.component.html',
  styleUrls: ['./paypal-component.component.scss']
})
export class PaypalComponentComponent implements OnInit {
  @ViewChild('paypal', { static: true }) paypalElement: ElementRef;

  constructor(private activatedRoute: ActivatedRoute) { }

  ngOnInit() {
    this.callPayPal(this.activatedRoute.snapshot.paramMap.get('uuid'));
  }

  callPayPal(uuid){
    paypal.Button.render({
      env: 'sandbox', // Or 'production'
      // Set up the payment:
      // 1. Add a payment callback
      payment: function(data, actions) {
        console.log(uuid);
        // 2. Make a request to your server
        return fetch('http://192.168.43.161:8771/paypal/api/paypal',{
          method:'post',
          body:JSON.stringify({
            casopisUuid:uuid,
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
        return fetch('http://192.168.43.161:8771/paypal/api/paypal/execute',{ 
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
            res.json().then(json => {
              window.location.href=json;
            });
          });
      }
    },'paypal');
  }

}
