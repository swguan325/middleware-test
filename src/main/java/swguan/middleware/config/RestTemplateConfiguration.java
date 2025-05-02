package swguan.middleware.config;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfiguration {

	@Bean
	public HttpClient httpClient() {
		return HttpClientBuilder.create().setMaxConnTotal(100).setMaxConnPerRoute(50).build();
	}

	private HttpComponentsClientHttpRequestFactory requestFactory(int connectTimeout, int readTimeout) {
		final HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		factory.setReadTimeout(readTimeout);
		factory.setConnectTimeout(connectTimeout);
		factory.setHttpClient(httpClient());
		return factory;
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate(requestFactory(10000, 5000));
	}

}
