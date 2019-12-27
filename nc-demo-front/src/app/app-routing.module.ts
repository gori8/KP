import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { KupovinaComponent } from './components/kupovina/kupovina.component';


const routes: Routes = [
  { path:'', component: KupovinaComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
