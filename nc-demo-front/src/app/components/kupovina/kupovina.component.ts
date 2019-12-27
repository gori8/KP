import { Component, OnInit } from '@angular/core';
import { EndpointsService } from 'src/app/services/endpoints.service';

@Component({
  selector: 'app-kupovina',
  templateUrl: './kupovina.component.html',
  styleUrls: ['./kupovina.component.scss']
})
export class KupovinaComponent implements OnInit {

  selectedCasopis = null;
  casopisi=[];

  constructor(private endpoints: EndpointsService) { }

  ngOnInit() {
    this.getCasopise();
  }

  public getCasopise(){
    this.endpoints.getCasopise().subscribe(
      res => {
        this.casopisi = res;
        this.selectedCasopis = res[0].userAndPaymentId;
      },
      err => {
        console.log(err);
      }
    )
  }
  
  public kupi(){
    console.log(this.selectedCasopis);
    
    this.endpoints.pay(this.selectedCasopis).subscribe(
      res => {
        window.location.href = res;
      },
      err => {
        console.log(err);
      }
    )
  }

}
