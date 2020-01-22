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
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule,
    NotifierModule.withConfig(customNotifierOptions)
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
