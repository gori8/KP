import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { IzdanjaCasopisaComponent } from './izdanja-casopisa.component';

describe('IzdanjaCasopisaComponent', () => {
  let component: IzdanjaCasopisaComponent;
  let fixture: ComponentFixture<IzdanjaCasopisaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ IzdanjaCasopisaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IzdanjaCasopisaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
