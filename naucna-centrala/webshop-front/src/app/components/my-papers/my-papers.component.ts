import { Component, OnInit } from '@angular/core';
import { EndpointsService } from 'src/app/services/endpoints.service';
import { AuthenticationService } from 'src/app/services/authentication.service';

@Component({
  selector: 'app-my-papers',
  templateUrl: './my-papers.component.html',
  styleUrls: ['./my-papers.component.scss']
})
export class MyPapersComponent implements OnInit {

  private casopisi = [];

  constructor(private endpoints:EndpointsService,private authenticationService: AuthenticationService) { }

  ngOnInit() {
    this.getAllPapers(this.authenticationService.currentUserValue.username);
  }

  getAllPapers(username){
    this.endpoints.getMyPapers(username).subscribe(
      res => {
          this.casopisi = res;
      },
      err => {
        console.log(err); 
      }
    )
  }

}
