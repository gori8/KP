import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddScientificWorkComponent } from './add-scientific-work.component';

describe('AddScientificWorkComponent', () => {
  let component: AddScientificWorkComponent;
  let fixture: ComponentFixture<AddScientificWorkComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddScientificWorkComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddScientificWorkComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
