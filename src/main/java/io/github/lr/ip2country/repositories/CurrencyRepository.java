package io.github.lr.ip2country.repositories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import io.github.lr.ip2country.entities.Currency;

/**
 * Repositorio para el cacheo de cotizacion por moneda.
 * 
 * @author lravanal
 *
 */
@Repository
public class CurrencyRepository {
	
	public final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Cacheable(cacheNames = "currency", key = "#code")
	public Currency addCurrency(String name, String code) {
		logger.debug("Creating new Currency {}: {}", code, name);
		
		return new Currency(name, code);
	}

	@Cacheable(cacheNames = "currency", key = "#code")
	public Currency findByCode(String code) {
		logger.debug("Should be returned Precached Currency ({})", code);
		
		return null;
	}

	@CachePut(cacheNames = "currency", key = "#currency.code")
	public Currency updateRate(Currency currency, double rate) {
		logger.debug("Updating Currency rate {}: {}", currency.getCode(), rate);
		
		currency.setRate(rate);
		
		return currency;
	}

}
