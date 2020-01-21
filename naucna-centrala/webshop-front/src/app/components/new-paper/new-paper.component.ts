import { Component, OnInit } from '@angular/core';
import { EndpointsService } from 'src/app/services/endpoints.service';
import { Router, ActivatedRoute } from '@angular/router';
import { NgForm } from '@angular/forms';
import { NotifierService } from 'angular-notifier';
import { AuthenticationService } from 'src/app/services/authentication.service';

@Component({
  selector: 'app-new-paper',
  templateUrl: './new-paper.component.html',
  styleUrls: ['./new-paper.component.scss']
})
export class NewPaperComponent implements OnInit {

  private formModel = [
    {
      fieldId:"naziv",
      fieldValue:""
    },
    {
      fieldId:"clanarina",
      fieldValue:""
    },
    {
      fieldId:"komeSeNaplacuje",
      fieldValue:null
    }
  ]

  private naucneOblasti = [
    {
      fieldId:"naucnaOblast1",
      fieldValue:null
    }
  ];

  private naciniPlacanja = [
    {
      fieldId:"nacinPlacanja1",
      fieldValue:null
    }
  ]

  private processId = "";
  private oblasti=["Naučna oblast 1"];
  private placanja=["Način plaćanja 1"];
  private listaOblasti=[];
  private listaPlacanja=[];
  private hidden=false;
  
  private readonly notifier: NotifierService;

  constructor(private activatedRoute: ActivatedRoute,
    private endpoints:EndpointsService,private router: Router,
    notifierService: NotifierService,
    private authenticationService:AuthenticationService) { 
    this.notifier = notifierService;
  }

  ngOnInit() {

    this.processId = this.activatedRoute.snapshot.paramMap.get('processId');
    if(this.processId==null){
      this.startProcess();
    }else{
      this.getCorrectionData(this.processId);
    }
    
    this.getOblasti();
    this.getPlacanja();
  }

  getCorrectionData(id){
    this.endpoints.getCorrectionData(id).subscribe(
      res => {
        this.formModel[0].fieldValue=res.naziv;
        this.formModel[1].fieldValue=res.clanarina;
        this.formModel[2].fieldValue=res.komeSeNaplacuje;

        let id = {
          fieldId:"id",
          fieldValue:res.id
        }
        this.formModel.push(id);
        
        let i = 0;
        for(let no of res.naucneOblasti){
          if(i==0){
            this.naucneOblasti[0].fieldValue=no;
          }else{
            let body = {
              fieldId:"naucnaOblast"+i,
              fieldValue:no
            }
            this.oblasti.push("Naučna oblast "+i);
            this.naucneOblasti.push(body);
          }

          i = i+1;
        }

        let j = 0;
        for(let np of res.naciniPlacanja){
          if(j==0){
            this.naciniPlacanja[0].fieldValue=np;
          }else{
            let body = {
              fieldId:"nacinPlacanja"+j,
              fieldValue:np
            }
            this.placanja.push("Način plaćanja "+j);
            this.naciniPlacanja.push(body);
          }

          j = j+1;
        }
      },
      err => {
        console.log(err); 
      }
    )
  }

  startProcess(){
    this.endpoints.startNewPaperProcess(this.authenticationService.currentUserValue.username).subscribe(
      res => {
        this.processId=res;
      },
      err => {
        console.log(err); 
      }
    )
  }

  getOblasti(){
    this.endpoints.getOblasti().subscribe(
      res => {
          this.listaOblasti=res;
      },
      err => {
        console.log(err); 
        alert("An error has occured while getting science fields");
      }
    )
  }

  getPlacanja(){
    this.endpoints.getPlacanja().subscribe(
      res => {
          this.listaPlacanja=res;
      },
      err => {
        console.log(err); 
        alert("An error has occured while getting payments");
      }
    )
  }

  addOblast(){
    var index = this.naucneOblasti.length+1;

    var oblast = {
      fieldId:"naucnaOblast"+index,
      fieldValue:null
    }

    this.naucneOblasti.push(oblast);
    this.oblasti.push("Naučna oblast "+index);
  }

  removeOblast(){
    var index1 = this.naucneOblasti.length-1;
    this.naucneOblasti.splice(index1,1);

    var index2 = this.oblasti.length-1;
    this.oblasti.splice(index2,1);
  }

  addPlacanje(){
    var index = this.naciniPlacanja.length+1;

    var placanje = {
      fieldId:"nacinPlacanja"+index,
      fieldValue:null
    }

    this.naciniPlacanja.push(placanje);
    this.placanja.push("Način plaćanja "+index);
  }

  removePlacanje(){
    var index1 = this.naciniPlacanja.length-1;
    this.naciniPlacanja.splice(index1,1);

    var index2 = this.placanja.length-1;
    this.placanja.splice(index2,1);
  }

  onSubmit(newPaperForm:NgForm){
    this.hidden=true;
    for(let o of this.naucneOblasti){
      this.formModel.push(o);
    }
    for(let p of this.naciniPlacanja){
      this.formModel.push(p);
    }
    if(newPaperForm.valid===true){
      this.endpoints.submitNewPaper(this.formModel,this.processId).subscribe(
        res => {
            console.log(res);
            this.endpoints.getValidation(res).subscribe(
              res => {
                if(res==true){
                  this.notifier.notify("success", "Novi časopis je validan. Da bi ste završili dodavanje časopisa unesite urednike i recenzente.");
                  this.router.navigate(['/newPaper/'+this.processId]);
                }else{
                  this.notifier.notify("error", "Novi časopis nije validan.");
                  this.hidden=false
                }
              },
              err => {
                console.log(err); 
                alert("An error has occured while getting validation");
                this.hidden=false;
              }
            )
        },
        err => {
          console.log(err); 
          alert("An error has occured while registrating");
          this.hidden=false;
        }
      )
    }
  }

}
