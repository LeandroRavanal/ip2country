package io.github.lr.ip2country.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Bean de configuracion de servicios.
 * 
 * @author lravanal
 *
 */
@Configuration
public class ServiceConfiguration {

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
}
