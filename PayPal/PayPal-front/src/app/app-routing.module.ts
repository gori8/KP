import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { PaypalComponentComponent } from './paypal-component/paypal-component.component';


const routes: Routes = [
  { path: '', component: PaypalComponentComponent }
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
