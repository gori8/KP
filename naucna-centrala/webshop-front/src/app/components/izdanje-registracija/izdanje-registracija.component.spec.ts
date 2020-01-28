import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { IzdanjeRegistracijaComponent } from './izdanje-registracija.component';

describe('IzdanjeRegistracijaComponent', () => {
  let component: IzdanjeRegistracijaComponent;
  let fixture: ComponentFixture<IzdanjeRegistracijaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ IzdanjeRegistracijaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IzdanjeRegistracijaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
