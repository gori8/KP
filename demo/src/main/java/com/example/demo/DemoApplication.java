package com.example.demo;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}


	@Bean
	public RestTemplate restTemplate() throws Exception{
		KeyStore clientStore = KeyStore.getInstance("JKS");
		clientStore.load(new FileInputStream("src/main/resources/identity.jks"), "secret".toCharArray());
		KeyStore trustStore = KeyStore.getInstance("JKS");
		trustStore.load(new FileInputStream("src/main/resources/truststore.jks"), "secret".toCharArray());

		SSLContextBuilder sslContextBuilder = new SSLContextBuilder();
		sslContextBuilder.setProtocol("TLS");
		sslContextBuilder.loadKeyMaterial(clientStore, "secret".toCharArray());
		sslContextBuilder.loadTrustMaterial(trustStore,null);

		SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContextBuilder.build());
		CloseableHttpClient httpClient = HttpClients.custom()
				.setSSLSocketFactory(sslConnectionSocketFactory)
				.build();
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
		requestFactory.setConnectTimeout(10000); // 10 seconds
		requestFactory.setReadTimeout(10000); // 10 seconds
		return new RestTemplate(requestFactory);
	}



}
