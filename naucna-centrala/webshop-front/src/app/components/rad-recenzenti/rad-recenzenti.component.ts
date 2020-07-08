import { Component, OnInit } from '@angular/core';
import { NotifierService } from 'angular-notifier';
import { ActivatedRoute, Router } from '@angular/router';
import { EndpointsService } from 'src/app/services/endpoints.service';

@Component({
  selector: 'app-rad-recenzenti',
  templateUrl: './rad-recenzenti.component.html',
  styleUrls: ['./rad-recenzenti.component.scss']
})
export class RadRecenzentiComponent implements OnInit {

  private hidden=false;

  recenzenti=[];

  radId=null;

  recIds=[];

  private readonly notifier: NotifierService;

  constructor(private activatedRoute: ActivatedRoute,
    private endpoints:EndpointsService,private router: Router,
    notifierService: NotifierService) {
      this.notifier = notifierService;
     }

  ngOnInit() {
    this.radId=this.activatedRoute.snapshot.paramMap.get('id');
    this.getRecByNaucnaOblast();
  }


  getRecByNaucnaOblast(){
    this.endpoints.getRecByNaucnaOblast(this.activatedRoute.snapshot.paramMap.get('id')).subscribe(
      res => {
        this.recenzenti=res;
      },
      error => {
        console.log(error); 
        this.notifier.notify("error", error);
      });
  }

  submit(){
    console.log(this.recIds);
    console.log(this.radId);

    var dto = {
      radId : this.radId,
      recIds : this.recIds
    }

    this.endpoints.finishAddingScientificWork(dto).subscribe(
      res => {
        this.notifier.notify("success", `Naučni rad je uspešno dodat.`);
        this.router.navigate(['']);
      },
      error => {
        console.log(error); 
        this.notifier.notify("error", error);
      });
  }


  checkValue(values: any, id){
    console.log(values.currentTarget.checked);

    if(values.currentTarget.checked){
      this.addToList(id);
    }else{
      this.removeFromList(id);
    }

 }

  addToList(id){
    this.recIds.push(id);
  }

  removeFromList(id){

    for(let i = 0; i<this.recIds.length;i++){
      if(this.recIds[i] == id){
        this.recIds.splice(i,1);
        break;
      }
    }

  }

  geoSearch(){
    this.endpoints.geoSearch(this.radId).subscribe(
      res => {
        this.recenzenti=res;
      },
      error => {
        console.log(error); 
        this.notifier.notify("error", error);
      });
  }

  moreLikeThis(){
    this.endpoints.moreLikeThis(this.radId).subscribe(
      res => {
        this.recenzenti=res;
      },
      error => {
        console.log(error); 
        this.notifier.notify("error", error);
      });
  }

}
