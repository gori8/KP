import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from '../services/authentication.service';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { EndpointsService } from '../services/endpoints.service';
import { DataService } from '../services/data.service';

@Component({
  selector: 'app-side-bar',
  templateUrl: './side-bar.component.html',
  styleUrls: ['./side-bar.component.scss']
})
export class SideBarComponent implements OnInit {

  currentUser$: Observable<any>;
  private numberOfTasks = 0;

  constructor(private endpoints:EndpointsService, private authenticationService: AuthenticationService, private router: Router
    , private data:DataService) { 
    this.currentUser$=this.authenticationService.currentUser;  
  }

  ngOnInit() {

    this.data.currentNumberOfTasks.subscribe(numberOfTasks => this.numberOfTasks=numberOfTasks);

    this.currentUser$.subscribe(res => {
      var user = this.authenticationService.currentUserValue;
      if(user!=null && (user.role=="Administrator" || user.role=="Urednik")){
        this.endpoints.getNumberOfTasks(user.username).subscribe(
          res => {
              this.data.changeNumberOfTasks(res);
          },
          err => {
            console.log(err); 
          }
        )
      }
    });
  }

  logout() {
    this.authenticationService.logout();
    this.router.navigate(['/login']);
}
}
