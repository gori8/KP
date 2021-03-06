package rs.ac.uns.ftn.paypal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import java.io.FileInputStream;
import java.security.KeyStore;

@EnableDiscoveryClient
@SpringBootApplication
@EnableScheduling
public class PaypalApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaypalApplication.class, args);
	}



}
