package rs.ac.uns.ftn.paypal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() throws Exception{
		/*KeyStore clientStore = KeyStore.getInstance("JKS");
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
		return new RestTemplate(requestFactory);*/
        return new RestTemplate();
    }

}
