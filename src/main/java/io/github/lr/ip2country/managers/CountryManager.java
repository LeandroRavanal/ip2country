package io.github.lr.ip2country.managers;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.github.lr.ip2country.entities.Country;
import io.github.lr.ip2country.exceptions.NonExistentElementException;
import io.github.lr.ip2country.repositories.CountryRepository;

/**
 * Administrador que obtiene los paises e incrementa su cantidad de accesos.
 * 
 * @author lravanal
 *
 */
@Component
public class CountryManager {
	
	public final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired private CountryRepository countryRepository;

	public void init() {
		List<Country> countries = countryRepository.findAll();
		
		countries.forEach(country -> countryRepository.addCountryCount(country.getCode()));
	}

	public Country findCountryByCode(String ip, String code) {
		List<Country> countries = countryRepository.findAll();
		
		Optional<Country> optCountry = countries.stream().filter(country -> country.getCode().equals(code)).findFirst();
		
		if (!optCountry.isPresent()) {
			throw new NonExistentElementException(ip, code);
		}
		
		return optCountry.get();
	}
	
	public void updateCountryCount(String ip, String code) {
		synchronized (code.intern()) {
			countryRepository.incrementCount(code);
		}
	}

}
