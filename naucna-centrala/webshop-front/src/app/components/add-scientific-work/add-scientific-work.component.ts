import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { EndpointsService } from 'src/app/services/endpoints.service';
import { NotifierService } from 'angular-notifier';
import { NgForm } from '@angular/forms';
import { FileUploadService } from 'src/app/services/file-upload.service';

@Component({
  selector: 'app-add-scientific-work',
  templateUrl: './add-scientific-work.component.html',
  styleUrls: ['./add-scientific-work.component.scss']
})
export class AddScientificWorkComponent implements OnInit {

  private rad={
    naslov:null,
    kljucneReci:null,
    apstrakt:null,
    naucnaOblast:null,
    sadrzaj:null,
    izdanjeId:null
  }

  naucneOblasti=[];

  fileToUpload: File = null;

  private hidden=false;
  
  private readonly notifier: NotifierService;

  constructor(private activatedRoute: ActivatedRoute,private fileUploadService: FileUploadService,
    private endpoints:EndpointsService,private router: Router,
    notifierService: NotifierService) {
      this.notifier = notifierService;
     }

  ngOnInit() {
    this.rad.izdanjeId = this.activatedRoute.snapshot.paramMap.get('id');
    this.getNaucneOblasti();
  }

  onSubmit(form:NgForm){
    if(form.valid===true){
      this.hidden=true;

      this.fileUploadService.postFile(this.fileToUpload).subscribe(
        res => {
          this.rad.sadrzaj = res;
          this.addScientificWork(this.rad);
        },
        error => {
          console.log(error); 
          this.notifier.notify("error", error);
          this.hidden=false
        });
    }
  }

  handleFileInput(files: FileList) {
    this.fileToUpload = files.item(0);
  }

  addScientificWork(dto){
    this.endpoints.addScientificWork(dto).subscribe(
      res => {
        this.notifier.notify("success", `Izaberite recenzente.`);
        this.router.navigate(['/scientificWork/recenzenti/'+res]);       
      },
      error => {
        console.log(error); 
        this.notifier.notify("error", error);
        this.hidden=false
      });
  }

  getNaucneOblasti(){
    this.endpoints.getNaucneOblastiByIzdanjeId(this.activatedRoute.snapshot.paramMap.get('id')).subscribe(
      res => {
        this.naucneOblasti=res;     
      },
      error => {
        console.log(error); 
        this.notifier.notify("error", error);
      });
  }
  
}
