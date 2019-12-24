import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-izbor-placanja',
  templateUrl: './izbor-placanja.component.html',
  styleUrls: ['./izbor-placanja.component.scss']
})
export class IzborPlacanjaComponent implements OnInit {

  constructor(private router: Router) { }

  naciniPlacanja = [];
  selectedPaymentMethod = null;

  ngOnInit() {
    this.naciniPlacanja.push({value:1,name:'Banka'});
    this.naciniPlacanja.push({value:2,name:'PayPal'});
    this.naciniPlacanja.push({value:3,name:'Bit Coin'});

    this.selectedPaymentMethod = 1;
  }

  submitPaymetMethod(){
    this.router.navigate([`payment`]);
  }

}
