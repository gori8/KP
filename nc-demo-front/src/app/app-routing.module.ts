import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { KupovinaComponent } from './components/kupovina/kupovina.component';
import { TransactionCompleteComponent } from './components/transaction-complete/transaction-complete.component';


const routes: Routes = [
  { path:'', component: KupovinaComponent },
  { path:'casopis/:uuid', component: TransactionCompleteComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
