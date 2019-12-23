import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { FormaPlacanjaComponent } from './components/forma-placanja/forma-placanja.component';


const routes: Routes = [
  { path: '', component: FormaPlacanjaComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
