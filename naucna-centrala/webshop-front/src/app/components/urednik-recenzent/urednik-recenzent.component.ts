import { Component, OnInit } from '@angular/core';
import { NotifierService } from 'angular-notifier';
import { EndpointsService } from 'src/app/services/endpoints.service';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { NgForm } from '@angular/forms';
import { elementStyleProp } from '@angular/core/src/render3';

@Component({
  selector: 'app-urednik-recenzent',
  templateUrl: './urednik-recenzent.component.html',
  styleUrls: ['./urednik-recenzent.component.scss']
})
export class UrednikRecenzentComponent implements OnInit {

  private body = {
    "urednici":[],
    "recenzenti":[null,null]
  }

  private urednici = [];
  private recenzenti = ["",""];

  private uredniciList=[];
  private recenzentiList=[];

  private processId="";
  private hidden=false;

  private readonly notifier: NotifierService;

  constructor(private activatedRoute: ActivatedRoute,private endpoints:EndpointsService,private router: Router,notifierService: NotifierService,private authenticationService:AuthenticationService) {
    this.notifier = notifierService;
   }

  ngOnInit() {

    this.processId = this.activatedRoute.snapshot.paramMap.get('processId');
    this.getUrednikeRecenzente(this.processId);
  }

  getUrednikeRecenzente(processId){
    this.endpoints.getUrednikeRecenzente(processId).subscribe(
      res => {
          console.log(res);
          
          this.uredniciList=res.urednici;
          this.recenzentiList=res.recenzenti;
      },
      err => {
        console.log(err); 
      }
    )
  }

  addUrednik(){
    this.urednici.push("");
    this.body.urednici.push(null);
  }

  removeUrednik(){
    var index = this.urednici.length-1;
    this.urednici.splice(index,1);
    this.body.urednici.splice(index,1);
  }

  addRecenzent(){
    this.recenzenti.push(""); 
    this.body.recenzenti.push(null);
  }

  removeRecenzent(){
    var index = this.recenzenti.length-1;
    this.recenzenti.splice(index,1);
    this.body.recenzenti.splice(index,1);
  }

  onSubmit(newPaperForm:NgForm){
    this.hidden=true;

    if(newPaperForm.valid===true){
      this.endpoints.submitUredniciRecenzenti(this.body,this.processId).subscribe(
        res => {
          if(res == true){
            this.notifier.notify("success", "Novi časopis je poslat administratoru. Da bi časopis bio vidljiv mora biti odobren od strane administratora.");
            this.router.navigate(['']);
          }else if(res == false){
            this.notifier.notify("error", "Nevalidan unos recenzenata i urednika.");
          }
        },
        err => {
          console.log(err); 
          this.notifier.notify("error", "Nevalidan unos recenzenata i urednika.");
          this.hidden=false;
        }
      )
    }
  }
}
