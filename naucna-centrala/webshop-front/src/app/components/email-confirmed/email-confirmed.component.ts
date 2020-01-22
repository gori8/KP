import { Component, OnInit } from '@angular/core';
import { EndpointsService } from 'src/app/services/endpoints.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-email-confirmed',
  templateUrl: './email-confirmed.component.html',
  styleUrls: ['./email-confirmed.component.scss']
})
export class EmailConfirmedComponent implements OnInit {

  private hidden = true;

  constructor(private endpoints:EndpointsService,private activatedRoute: ActivatedRoute) { }

  ngOnInit() {
    this.activateAccount(this.activatedRoute.snapshot.paramMap.get('processId'));
  }

  activateAccount(processId){
    this.endpoints.activateAccount(processId).subscribe(
      res => {
          this.hidden=false;
      },
      err => {
        console.log(err); 
        alert("An error has occured while activating account");
      }
    )
  }

}
