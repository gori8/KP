import { Component, OnInit } from '@angular/core';
import { EndpointsService } from 'src/app/services/endpoints.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-bitcoin-redirect',
  templateUrl: './bitcoin-redirect.component.html',
  styleUrls: ['./bitcoin-redirect.component.scss']
})
export class BitcoinRedirectComponent implements OnInit {

  constructor(private endpoints: EndpointsService, private activatedRoute: ActivatedRoute) { }

  ngOnInit() {

    var transaction = this.activatedRoute.snapshot.paramMap.get('transaction');
    var response = this.activatedRoute.snapshot.paramMap.get('response');

    this.endpoints.callMicroservice(transaction,response).subscribe(
      res => {
          window.location.href = res;
      },
      err => {
        console.log(err);
        alert("An error has occured while calling a bitcoin microservice");
      }
    )
  }

}
