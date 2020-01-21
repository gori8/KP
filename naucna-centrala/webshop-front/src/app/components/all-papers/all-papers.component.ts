import { Component, OnInit } from '@angular/core';
import { EndpointsService } from 'src/app/services/endpoints.service';
import { AuthenticationService } from 'src/app/services/authentication.service';

@Component({
  selector: 'app-all-papers',
  templateUrl: './all-papers.component.html',
  styleUrls: ['./all-papers.component.scss']
})
export class AllPapersComponent implements OnInit {

  private casopisi = [];

  constructor(private endpoints:EndpointsService,private authenticationService: AuthenticationService) { }

  ngOnInit() {
    this.getAllPapers();
  }

  getAllPapers(){
    this.endpoints.getAllPapers().subscribe(
      res => {
          this.casopisi = res;
      },
      err => {
        console.log(err); 
      }
    )
  }

}
