package io.github.lr.ip2country.managers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.github.lr.ip2country.entities.Country;
import io.github.lr.ip2country.entities.Ip2Country;
import io.github.lr.ip2country.services.Ip2CountryService;

/**
 * Administrador que obtiene el pais dada una IP.
 * 
 * @author lravanal
 *
 */
@Component
public class Ip2CountryManager {

	public final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired private Ip2CountryService ip2CountryService;
	@Autowired private CountryManager countryManager;
	@Autowired private CurrencyManager currencyManager;
	
	public Country consume(String ip) {
		Ip2Country ip2Country = ip2CountryService.consume(ip);
		
		Country country = countryManager.findCountryByCode(ip, ip2Country.getCode());
		
		countryManager.updateCountryCount(ip, country.getCode());
		
		currencyManager.updateRates(country);
		
		return country;
	}
	
}
