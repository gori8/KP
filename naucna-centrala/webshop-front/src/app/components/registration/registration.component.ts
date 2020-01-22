import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { EndpointsService } from 'src/app/services/endpoints.service'
import { Router } from '@angular/router';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent implements OnInit {

  private formModel = [
    {
      fieldId:"ime",
      fieldValue:""
    },
    {
      fieldId:"prezime",
      fieldValue:""
    },
    {
      fieldId:"grad",
      fieldValue:""
    },
    {
      fieldId:"drzava",
      fieldValue:""
    },
    {
      fieldId:"titula",
      fieldValue:""
    },
    {
      fieldId:"email",
      fieldValue:""
    },
    {
      fieldId:"username",
      fieldValue:""
    },
    {
      fieldId:"password",
      fieldValue:""
    },
    {
      fieldId:"recenzent",
      fieldValue:false
    },
    {
      fieldId:"naucnaOblast1",
      fieldValue:null
    }
  ]

  private taskId = "";
  private oblasti=["Naučna oblast 1"];
  private listaOblasti=[];
  private hidden=false;

  constructor(private endpoints:EndpointsService,private router: Router) { }

  ngOnInit() {
    this.startProcess();
    this.getOblasti();
  }

  startProcess(){
    this.endpoints.startProcess().subscribe(
      res => {
          this.taskId=res;
      },
      err => {
        console.log(err); 
        alert("An error has occured while starting a process");
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

  addOblast(){

    var index = this.formModel.length - 8;

    var oblast = {
      fieldId:"naucnaOblast"+index,
      fieldValue:null
    }

    this.formModel.push(oblast);
    this.oblasti.push("Naučna oblast "+index);
  }

  removeOblast(){
    var index1 = this.formModel.length-1;
    this.formModel.splice(index1,1);

    var index2 = this.oblasti.length-1;
    this.oblasti.splice(index2,1);
  }

  onSubmit(regForm:NgForm){
    this.hidden=true;
    if(regForm.valid===true){
      this.endpoints.submitRegistration(this.formModel,this.taskId).subscribe(
        res => {
            console.log(res);
            this.endpoints.getValidation(res).subscribe(
              res => {
                  if(res==true){
                    this.router.navigate(['/validated']);
                  }else{
                    alert("Forma nije validna");
                    this.hidden=false;
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
