import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RadRecenzentiComponent } from './rad-recenzenti.component';

describe('RadRecenzentiComponent', () => {
  let component: RadRecenzentiComponent;
  let fixture: ComponentFixture<RadRecenzentiComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RadRecenzentiComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RadRecenzentiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
