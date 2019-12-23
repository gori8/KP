import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'nc-demo-front';

  bankaUspesna(){
    console.log("banka-uspesna");
    
  }

  bankaNeuspesna(){
    console.log("banka-neuspesna");
    
  }

  paypalUspesna(){
    console.log("paypal-uspesna");
    
  }

  paypalNeuspesna(){
    console.log("paypal-neuspesna");
    
  }

  bitcoinUspesna(){
    console.log("bitcoin-uspesna");
    
  }

  bitcoinNeuspesna(){
    console.log("bitcoin-neuspesna");
    
  }
}
