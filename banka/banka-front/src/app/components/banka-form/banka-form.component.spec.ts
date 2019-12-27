import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BankaFormComponent } from './banka-form.component';

describe('BankaFormComponent', () => {
  let component: BankaFormComponent;
  let fixture: ComponentFixture<BankaFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BankaFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BankaFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
