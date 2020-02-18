import { Component, OnInit } from '@angular/core';
import { EndpointsService } from 'src/app/services/endpoints.service';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-all-papers',
  templateUrl: './all-papers.component.html',
  styleUrls: ['./all-papers.component.scss']
})
export class AllPapersComponent implements OnInit {

  currentUser$: Observable<any>;
  private casopisi = [];
  private pretplaceniCasopisi = [];

  constructor(private endpoints:EndpointsService,private authenticationService: AuthenticationService) {
    this.currentUser$=this.authenticationService.currentUser;
   }

  ngOnInit() {
    this.getAllPapers();

    if(this.authenticationService.currentUserValue != null && this.authenticationService.currentUserValue.role=="Registrovani korisnik"){
      this.getBoughtPapers(this.authenticationService.currentUserValue.username);
    }
  }

  getBoughtPapers(username){
    this.endpoints.getBoughtPapers(username).subscribe(
      res => {
          this.pretplaceniCasopisi = res;

          for(let pc of this.pretplaceniCasopisi){
            for(let c of this.casopisi){
              if(c.id==pc.id){

                //let date = pc.datumIsticanja.split('T')[0];
                if(pc.datumIsticanja == '12.12.9999.'){
                  c["datumIsticanja"]="daljnjeg.";
                }else{
                  //c["datumIsticanja"]=date;
                  c["datumIsticanja"]=pc.datumIsticanja;
                }
                break;
              }
            }
          }
      },
      err => {
        console.log(err); 
      }
    )
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
