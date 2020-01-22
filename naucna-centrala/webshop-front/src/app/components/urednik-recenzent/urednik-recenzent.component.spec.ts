import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UrednikRecenzentComponent } from './urednik-recenzent.component';

describe('UrednikRecenzentComponent', () => {
  let component: UrednikRecenzentComponent;
  let fixture: ComponentFixture<UrednikRecenzentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UrednikRecenzentComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UrednikRecenzentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
