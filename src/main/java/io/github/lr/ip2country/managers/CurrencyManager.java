package io.github.lr.ip2country.managers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.github.lr.ip2country.entities.Country;
import io.github.lr.ip2country.entities.Currency;
import io.github.lr.ip2country.entities.Rates;
import io.github.lr.ip2country.repositories.CurrencyRepository;
import io.github.lr.ip2country.services.CurrencyService;

/**
 * Administrador que obtiene las cotizaciones de las monedas y actualiza cada uno.
 * 
 * @author lravanal
 *
 */
@Component
public class CurrencyManager {

	public final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static CurrencyManager INSTANCE;

	@Autowired private CurrencyService currencyService;
	@Autowired private CurrencyRepository currencyRepository;
	
	public CurrencyManager() {
		INSTANCE = this;
	}
	
	public static CurrencyManager getInstance() {
		return INSTANCE;
	}
	
	public void init() {
		try {
			Rates rates = currencyService.findAllRates();
			
			updateCurrencies(rates);
		} catch (Exception e) {
			logger.error("Error executing Currency Rates", e);
		}
	}
	
	public Currency getCurrency(String name, String code) {
		return currencyRepository.addCurrency(name, code);
	}
	
	public void updateCurrencies(Rates rates) {
		rates.getRates().forEach((code, rate) -> {
			Currency currency = currencyRepository.findByCode(code);
			
			if (currency != null) {
				currencyRepository.updateRate(currency, rate);
			}
		});
	}

	public void updateRates(Country country) {
		country.getCurrencies().stream().forEach(countryCurrency -> {
			Currency currency = currencyRepository.findByCode(countryCurrency.getCode());
			
			if (currency != null) {
				countryCurrency.setRate(currency.getRate());
			}
		});
	}
	
}
