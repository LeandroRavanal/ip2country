package io.github.lr.ip2country.services;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import io.github.lr.ip2country.aspects.Logging;
import io.github.lr.ip2country.entities.Ip2Country;
import io.github.lr.ip2country.utils.UtilsHelper;

/**
 * Servicio para el consumo del servicio web de IP a pais.
 * 
 * @author lravanal
 *
 */
@Service
public class Ip2CountryService {
	
	public final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${ip2country.url}") private String url;
	
	@Autowired RestTemplate restTemplate;
	
	@Logging
	public Ip2Country consume(String ip) {
		logger.debug("Searching Ip2Country: {}", ip);
		
	    ResponseEntity<Ip2Country> response = restTemplate.exchange(MessageFormat.format(url, ip), HttpMethod.GET, null, Ip2Country.class);
	    
	    UtilsHelper.checkStatusResponse(response, "Ip2Country");
	    
	    return response.getBody();
	}
	
}
