import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { BankaFormComponent } from './components/banka-form/banka-form.component';


const routes: Routes = [
  { path: 'banka/card/:transaction', component: BankaFormComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
