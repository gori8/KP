import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { EndpointsService } from 'src/app/services/endpoints.service';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-izdanja-casopisa',
  templateUrl: './izdanja-casopisa.component.html',
  styleUrls: ['./izdanja-casopisa.component.scss']
})
export class IzdanjaCasopisaComponent implements OnInit {

  private casopis=null;
  currentUser$: Observable<any>;

  constructor(private activatedRoute: ActivatedRoute,
    private endpoints:EndpointsService,
    private authenticationService: AuthenticationService) { 
      this.currentUser$=this.authenticationService.currentUser
    }

  ngOnInit() {
    this.getNumbersForCasopis(this.activatedRoute.snapshot.paramMap.get('id'));
  }

  getNumbersForCasopis(id){
    this.endpoints.getNumbersForCasopis(id).subscribe(
      res => {
        this.casopis = res
      },
      err => {
        console.log(err); 
      }
    );
  }

  public pay(izdanjeUuid){

    var user = this.authenticationService.currentUserValue;

    this.endpoints.pay(izdanjeUuid,user.username).subscribe(
      res => {
        window.location.href = res;
      },
      err => {
        console.log(err);
      }
    )
  }

}
