import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { RegistrationComponent } from './components/registration/registration.component';
import { ValidatedComponent } from './components/validated/validated.component';
import { EmailConfirmedComponent } from './components/email-confirmed/email-confirmed.component';
import { LoginComponent } from './components/login/login.component';
import { AdminTasksComponent } from './components/admin-tasks/admin-tasks.component';
import { NewPaperComponent } from './components/new-paper/new-paper.component';
import { UrednikRecenzentComponent } from './components/urednik-recenzent/urednik-recenzent.component';
import { MyPapersComponent } from './components/my-papers/my-papers.component';
import { AuthGuard } from './_guards';
import { AllPapersComponent } from './components/all-papers/all-papers.component';
import { IzdanjeRegistracijaComponent } from './components/izdanje-registracija/izdanje-registracija.component';
import { IzdanjaCasopisaComponent } from './components/izdanja-casopisa/izdanja-casopisa.component';
import { PaymentResponseComponent } from './components/payment-response/payment-response.component';

const routes: Routes = [
  { path : "", component : HomeComponent },
  { path : "registration", component : RegistrationComponent },
  { path : "validated", component : ValidatedComponent },
  { path : "activate/:processId", component : EmailConfirmedComponent },
  { path : "login", component : LoginComponent },
  { path : "tasks", component : AdminTasksComponent, canActivate:[AuthGuard] },
  { path : "newPaper", component : NewPaperComponent, canActivate:[AuthGuard] },
  { path : "correction/:processId", component : NewPaperComponent, canActivate:[AuthGuard] },
  { path : "newPaper/:processId", component : UrednikRecenzentComponent, canActivate:[AuthGuard] },
  { path : "myPapers", component : MyPapersComponent, canActivate:[AuthGuard] },
  { path : "papers", component : AllPapersComponent},
  { path : "number/:casopisId", component : IzdanjeRegistracijaComponent, canActivate:[AuthGuard] },
  { path : "papers/:id/numbers", component : IzdanjaCasopisaComponent},
  { path : "paymentresponse/:message", component : PaymentResponseComponent, canActivate:[AuthGuard]}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
