import { Component, OnInit } from '@angular/core';
import { EndpointsService } from 'src/app/services/endpoints.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-transaction-complete',
  templateUrl: './transaction-complete.component.html',
  styleUrls: ['./transaction-complete.component.scss']
})
export class TransactionCompleteComponent implements OnInit {

  success = "";
  uuid = "";
  casopis = {
    naziv:null,
    placen:null
  };

  constructor(private activatedRoute: ActivatedRoute, private endpoints: EndpointsService) { }

  ngOnInit() {
    this.success = this.activatedRoute.snapshot.paramMap.get('success');
    this.uuid = this.activatedRoute.snapshot.paramMap.get('uuid');
    this.endpoints.getCasopis(this.uuid).subscribe(
      res => {
          this.casopis=res;
      },
      err => {
        console.log(err); 
      }
    )
  }

}
