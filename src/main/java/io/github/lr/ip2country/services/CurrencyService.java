package io.github.lr.ip2country.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import io.github.lr.ip2country.aspects.RetryPolicy;
import io.github.lr.ip2country.entities.Rates;
import io.github.lr.ip2country.utils.UtilsHelper;

/**
 * Servicio para el consumo del servicio web de cotizaciones.
 * 
 * @author lravanal
 *
 */
@Service
public class CurrencyService {

	public final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Value("${currency.url}") private String url;
	
	@Autowired RestTemplate restTemplate;
	
	@RetryPolicy
	public Rates findAllRates() {
	    logger.debug("Searching Rates");
		
	    ResponseEntity<Rates> response = restTemplate.exchange(url, HttpMethod.GET, null, Rates.class);
	    
	    UtilsHelper.checkStatusResponse(response, "Rates");
	    
	    return response.getBody();
	}
	
}
