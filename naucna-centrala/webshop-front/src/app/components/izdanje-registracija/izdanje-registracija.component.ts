import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { EndpointsService } from 'src/app/services/endpoints.service';
import { NotifierService } from 'angular-notifier';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-izdanje-registracija',
  templateUrl: './izdanje-registracija.component.html',
  styleUrls: ['./izdanje-registracija.component.scss']
})
export class IzdanjeRegistracijaComponent implements OnInit {

  private izdanje={
    naziv:null,
    broj:null,
    cena:null,
    casopisId:null
  }
  private casopis=null;
  private hidden=false;
  
  private readonly notifier: NotifierService;

  constructor(private activatedRoute: ActivatedRoute,
    private endpoints:EndpointsService,private router: Router,
    notifierService: NotifierService) { 
      this.notifier = notifierService;
    }

  ngOnInit() {
    this.izdanje.casopisId = this.activatedRoute.snapshot.paramMap.get('casopisId');
    this.getCasopis(this.izdanje.casopisId);
  }

  getCasopis(id){
    this.endpoints.getCasopis(id).subscribe(
      res => {
        this.casopis = res
      },
      err => {
        console.log(err); 
      }
    );
  }

  onSubmit(form:NgForm){
    if(form.valid===true){
      this.hidden=true;
      this.endpoints.submitIzdanje(this.izdanje).subscribe(
        res => {
          this.notifier.notify("success", `Novo izdanje časopisa ${this.casopis.naziv} je uspešno dodato.`);
          this.router.navigate(['']);        
        },
        err => {
          console.log(err); 
          this.notifier.notify("error", "Greška: Novo izdanje nije dodato.");
          this.hidden=false
        }
      );
    }
  }

}
