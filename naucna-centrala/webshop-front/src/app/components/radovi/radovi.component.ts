import { Component, OnInit, ɵNOT_FOUND_CHECK_ONLY_ELEMENT_INJECTOR } from '@angular/core';
import { EndpointsService } from 'src/app/services/endpoints.service';
import { NotifierService } from 'angular-notifier';

@Component({
  selector: 'app-radovi',
  templateUrl: './radovi.component.html',
  styleUrls: ['./radovi.component.scss']
})
export class RadoviComponent implements OnInit {

  query={
    naslov:null,
    kljucneReci:null,
    apstrakt:null,
    naucnaOblast:null,
    sadrzaj:null,
    autor:null,
    nazivCasopisa:null
  }

  boolQueryGroups:any = []
  boolQueryOperators:any =[]
  boolQuery:any=[];

  radovi=[];

  private readonly notifier: NotifierService;

  constructor(private endpoints:EndpointsService,notifierService: NotifierService) { 
    this.notifier = notifierService;
  }

  ngOnInit() {
  }

  searchByField(field,value){

    var simpleQuery={
      field:field,
      value:value
    }

    this.endpoints.searchByField(simpleQuery).subscribe(
      res => {
        this.radovi = res;   
        console.log(this.radovi);
          
      },
      error => {
        console.log(error); 
        this.notifier.notify("error", error);
      });
  }

  addGroup(){

    var group = {
      elements:[],
      operators:[]
    }

    this.boolQueryGroups.push(group);

    if(this.boolQueryGroups.length-1!=0){
      this.boolQueryOperators.push("AND");
    }    
  }

  removeGroup(){
    var index = this.boolQueryGroups.length-1;
    this.boolQueryGroups.splice(index,1);
    this.boolQueryOperators.splice(index-1,1);
  }

  addElement(){
    
    var element = {
      field:null,
      value:null,
      operator:null
    }

    this.boolQuery.push(element);  
  }

  removeElement(){
    var index = this.boolQuery.length-1;
    this.boolQuery.splice(index,1);
  }

  executeBool(){
    /*var boolQuery = {
      boolQueryGroups:this.boolQueryGroups,
      boolQueryOperators:this.boolQueryOperators
    }

    console.log(boolQuery);*/

    this.endpoints.searchByBool(this.boolQuery).subscribe(
      res => {
        this.radovi = res;   
        console.log(this.radovi);
      },
      error => {
        console.log(error); 
        this.notifier.notify("error", error);
      });

  }


  /*open(content) {
    this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title',windowClass : "myCustomModalClass"}).result.then((result) => {
      console.log(result);
    }, (reason) => {
      console.log(reason);
    });
  }*/
}
