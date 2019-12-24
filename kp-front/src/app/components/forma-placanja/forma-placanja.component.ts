import { Component, OnInit } from '@angular/core';
import  *  as  data  from  '../../../json/config.json';

@Component({
  selector: 'app-forma-placanja',
  templateUrl: './forma-placanja.component.html',
  styleUrls: ['./forma-placanja.component.scss']
})
export class FormaPlacanjaComponent implements OnInit {

  constructor() { }

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
    this.forma = (data as any).default;

    
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

    if(this.forma.button !== undefined){
      this.button = this.forma.button;
    }

    if(this.forma.header !== undefined){
      this.header = this.forma.header;
    }
    
    this.imageSource = this.forma.image;
  }


  onSubmit(paymentForm){
    if(paymentForm.valid===true){
      console.log("Validna forma");
    }
  }
}
