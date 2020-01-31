import { Component, OnInit } from '@angular/core';
import { EndpointsService } from 'src/app/services/endpoints.service';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-kupljeni-predmeti',
  templateUrl: './kupljeni-predmeti.component.html',
  styleUrls: ['./kupljeni-predmeti.component.scss']
})
export class KupljeniPredmetiComponent implements OnInit {

  currentUser$: Observable<any>;
  private casopisi:[];

  constructor(private endpoints:EndpointsService, private authenticationService: AuthenticationService) { 
    this.currentUser$=this.authenticationService.currentUser
  }

  ngOnInit() {
    this.getBoughtItems(this.authenticationService.currentUserValue.username);
  }

  getBoughtItems(username){
    this.endpoints.getBoughtItems(username).subscribe(
      res => {
        this.casopisi = res
      },
      err => {
        console.log(err); 
      }
    );
  }

}
