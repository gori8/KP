import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BitcoinRedirectComponent } from './bitcoin-redirect.component';

describe('BitcoinRedirectComponent', () => {
  let component: BitcoinRedirectComponent;
  let fixture: ComponentFixture<BitcoinRedirectComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BitcoinRedirectComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BitcoinRedirectComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
