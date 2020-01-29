import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-payment-response',
  templateUrl: './payment-response.component.html',
  styleUrls: ['./payment-response.component.scss']
})
export class PaymentResponseComponent implements OnInit {

  private message = null;

  constructor(private activatedRoute: ActivatedRoute) { }

  ngOnInit() {
    this.message = this.activatedRoute.snapshot.paramMap.get('message');
  }

}
