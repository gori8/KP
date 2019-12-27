import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { FormaPlacanjaComponent } from './components/forma-placanja/forma-placanja.component';
import { IzborPlacanjaComponent } from './components/izbor-placanja/izbor-placanja.component';

const routes: Routes = [
  { path: 'payment', component: FormaPlacanjaComponent },
  { path: ':uuid', component: IzborPlacanjaComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
