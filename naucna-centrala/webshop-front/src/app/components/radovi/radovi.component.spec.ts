import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RadoviComponent } from './radovi.component';

describe('RadoviComponent', () => {
  let component: RadoviComponent;
  let fixture: ComponentFixture<RadoviComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RadoviComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RadoviComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
