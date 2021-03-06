import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './components/home/home.component';
import { SideBarComponent } from './side-bar/side-bar.component';
import { RegistrationComponent } from './components/registration/registration.component';
import { EmailConfirmedComponent } from './components/email-confirmed/email-confirmed.component';
import { LoginComponent } from './components/login/login.component';
import { HttpClientModule, HTTP_INTERCEPTORS } from "@angular/common/http";
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ValidatedComponent } from './components/validated/validated.component';
import { ErrorInterceptor, JwtInterceptor } from './_helpers';
import { AdminTasksComponent } from './components/admin-tasks/admin-tasks.component';
import { NewPaperComponent } from './components/new-paper/new-paper.component';
import { NotifierModule, NotifierOptions } from "angular-notifier";
import { UrednikRecenzentComponent } from './components/urednik-recenzent/urednik-recenzent.component';
import { MyPapersComponent } from './components/my-papers/my-papers.component';
import { AllPapersComponent } from './components/all-papers/all-papers.component';
import { IzdanjeRegistracijaComponent } from './components/izdanje-registracija/izdanje-registracija.component';
import { IzdanjaCasopisaComponent } from './components/izdanja-casopisa/izdanja-casopisa.component';
import { PaymentResponseComponent } from './components/payment-response/payment-response.component';
import { KupljeniPredmetiComponent } from './components/kupljeni-predmeti/kupljeni-predmeti.component';
import { AddPlansComponent } from './components/add-plans/add-plans.component';
import { SelectPlanComponent } from './components/select-plan/select-plan.component';
import { AddScientificWorkComponent } from './components/add-scientific-work/add-scientific-work.component';
import { RadoviComponent } from './components/radovi/radovi.component';
import { NgxExtendedPdfViewerModule } from 'ngx-extended-pdf-viewer';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { RadRecenzentiComponent } from './components/rad-recenzenti/rad-recenzenti.component';

const customNotifierOptions: NotifierOptions = {
  position: {
		horizontal: {
			position: 'right',
      distance: 12,
		},
		vertical: {
			position: 'top',
			distance: 12,
			gap: 10
		}
	},
  theme: 'material',
  behaviour: {
    autoHide: 10000,
    onClick: 'hide',
    onMouseover: 'pauseAutoHide',
    showDismissButton: true,
    stacking: 4
  },
  animations: {
    enabled: true,
    show: {
      preset: 'slide',
      speed: 300,
      easing: 'ease'
    },
    hide: {
      preset: 'fade',
      speed: 300,
      easing: 'ease',
      offset: 50
    },
    shift: {
      speed: 300,
      easing: 'ease'
    },
    overlap: 150
  }
};

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    SideBarComponent,
    RegistrationComponent,
    EmailConfirmedComponent,
    LoginComponent,
    ValidatedComponent,
    AdminTasksComponent,
    NewPaperComponent,
    UrednikRecenzentComponent,
    MyPapersComponent,
    AllPapersComponent,
    IzdanjeRegistracijaComponent,
    IzdanjaCasopisaComponent,
    PaymentResponseComponent,
    KupljeniPredmetiComponent,
    AddPlansComponent,
    SelectPlanComponent,
    AddScientificWorkComponent,
    RadoviComponent,
    RadRecenzentiComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule,
    NgbModule,
    NgxExtendedPdfViewerModule,
    NotifierModule.withConfig(customNotifierOptions)
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
