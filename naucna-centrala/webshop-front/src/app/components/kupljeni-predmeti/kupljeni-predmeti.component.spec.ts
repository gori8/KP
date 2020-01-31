import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { KupljeniPredmetiComponent } from './kupljeni-predmeti.component';

describe('KupljeniPredmetiComponent', () => {
  let component: KupljeniPredmetiComponent;
  let fixture: ComponentFixture<KupljeniPredmetiComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ KupljeniPredmetiComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(KupljeniPredmetiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
