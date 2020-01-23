import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { EndpointsService } from 'src/app/services/endpoints.service.js';

@Component({
  selector: 'app-forma-placanja',
  templateUrl: './forma-placanja.component.html',
  styleUrls: ['./forma-placanja.component.scss']
})
export class FormaPlacanjaComponent implements OnInit {

  constructor(private endpoints:EndpointsService,private activatedRoute: ActivatedRoute) {}

  uuid=null;
  body={};
  nacinPlacana=null;
  forma=null;
  button="Submit";
  header="";
  imageSource = undefined;
  rows = [
    {
      cols:[]
    }
  ]

  ngOnInit() {

    this.nacinPlacana = this.activatedRoute.snapshot.paramMap.get('nacinPlacanja');
    this.uuid = this.activatedRoute.snapshot.paramMap.get('uuid');

    this.getJson();
  }

  getJson(){
    this.endpoints.getJSON(this.nacinPlacana).subscribe(
      data => {
        this.forma=data;
        console.log(this.forma);
        
        for(let field of this.forma.form){

          if(field.validation===undefined){
            field.validation = {pattern:".*"};
          }else if(field.validation.pattern===undefined){
            field.validation.pattern = ".*";
          }
    
          if(field.row != null || field.col != null){
            if(this.rows[field.row] === undefined){
              this.rows[field.row]={cols:[]};
            }
            this.rows[field.row].cols[field.col] = field;
          }
        }
    
        this.rows = this.rows.filter(function (el) {
          return el != null;
        });
    
        for(let r of this.rows){
          r.cols= r.cols.filter(function (el) {
            return el != null;
          });
        }
    
        if(this.forma.button !== undefined && this.forma.button.name !== undefined){
          this.button = this.forma.button.name;
        }
    
        if(this.forma.header !== undefined){
          this.header = this.forma.header;
        }
        
        this.imageSource = this.forma.image;
      }
    );
  }

  onSubmit(paymentForm){
    if(paymentForm.valid===true){
      
      for(let item of this.forma.form){
        if(item.type!="reset"){
          this.body[item.id]=item.model;
        }
      }
      console.log(this.body); 
    }
  }
}
