import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NotifierService } from 'angular-notifier';
import { NgForm } from '@angular/forms';
import { RegistrationService } from '../_services/registration/registration.service';

@Component({
  selector: 'app-add-plans',
  templateUrl: './add-plans.component.html',
  styleUrls: ['./add-plans.component.scss']
})
export class AddPlansComponent implements OnInit {

  private id = null;
  private readonly notifier: NotifierService;
  private planovi = [];
  private hidden=false;
  private planoviModel=[];

  constructor(private activatedRoute: ActivatedRoute,private endpoints:RegistrationService,private router: Router,notifierService: NotifierService) {
    this.notifier = notifierService;
   }

  ngOnInit() {
    this.addPlan();
    this.id = this.activatedRoute.snapshot.paramMap.get('id');
  }

  addPlan(){
    this.planovi.push("");
    this.planoviModel.push(
      {
        period:null,
        ucestalostPerioda:null,
        cena:null
      }
    );
  }

  removePlan(){
    var index = this.planovi.length-1;
    this.planovi.splice(index,1);
    this.planoviModel.splice(index,1);
  }

  onSubmit(form:NgForm){
    this.hidden=true;

    if(form.valid===true){
      this.endpoints.submitPlans(this.planoviModel,this.id).subscribe(
        res => {
          if(res == true){
            this.notifier.notify("success", "Uspešno dodati planovi plaćanja. Da bi časopis bio vidljiv mora biti odobren od strane administratora.");
            this.router.navigate(['']);
          }else if(res == false){
            this.notifier.notify("error", "Nevalidan unos planova plaćanja.");
          }
        },
        err => {
          console.log(err); 
          this.notifier.notify("error", "Greška pri dodavanju planova plaćanja.");
          this.hidden=false;
        }
      )
    }
  }
}
