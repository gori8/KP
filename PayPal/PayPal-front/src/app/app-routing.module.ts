import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { PaypalComponentComponent } from './paypal-component/paypal-component.component';


const routes: Routes = [
  { path: ':uuid', component: PaypalComponentComponent }
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
