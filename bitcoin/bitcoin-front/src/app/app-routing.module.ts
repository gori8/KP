import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { BitcoinRedirectComponent } from './components/bitcoin-redirect/bitcoin-redirect.component';


const routes: Routes = [
  { path:"bitcoin", component: BitcoinRedirectComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
