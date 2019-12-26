import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { EndpointsService } from 'src/app/services/endpoints.service';

@Component({
  selector: 'app-izbor-placanja',
  templateUrl: './izbor-placanja.component.html',
  styleUrls: ['./izbor-placanja.component.scss']
})
export class IzborPlacanjaComponent implements OnInit {

  constructor(private router: Router, private endpoints: EndpointsService) { }

  naciniPlacanja = [];
  selectedPaymentMethod = null;

  ngOnInit() {
    this.getNacinePlacanjaZaCasopis();
  }

  submitPaymetMethod(){

    for(let np of this.naciniPlacanja){
      if(np.value == this.selectedPaymentMethod){
        this.endpoints.callSelectedMicroservice(np.url).subscribe(
          res => {
              //console.log(res);
              window.location.href = res;
          },
          err => {
            console.log(err);
            
            alert("An error has occured while usgin url "+np.url);
          }
        )
        break;
      }
    }
  }

  public getNacinePlacanjaZaCasopis(){
    this.endpoints.getNacinePlacanjaZaCasopis(1).subscribe(
      res => {
        this.naciniPlacanja = res;
        this.selectedPaymentMethod = res[0].value;
      },
      err => {
        alert("An error has occured while getting payment methods")
      }
    )
  }

}
