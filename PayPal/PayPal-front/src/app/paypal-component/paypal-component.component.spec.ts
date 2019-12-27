import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PaypalComponentComponent } from './paypal-component.component';

describe('PaypalComponentComponent', () => {
  let component: PaypalComponentComponent;
  let fixture: ComponentFixture<PaypalComponentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PaypalComponentComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PaypalComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
