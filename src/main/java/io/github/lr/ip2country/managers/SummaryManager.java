package io.github.lr.ip2country.managers;

import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.github.lr.ip2country.entities.Country;
import io.github.lr.ip2country.entities.Summary;
import io.github.lr.ip2country.repositories.CountryRepository;

/**
 * Administrador que obtiene el resumen de informacion.
 * 
 * @author lravanal
 *
 */
@Component
public class SummaryManager {
	
	public final Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final String INTERNAL = "INTERNAL";
	
	@Autowired private CountryRepository countryRepository;
	
	public Summary summary() {
		List<Country> countries = countryRepository.findAll();
		countries.stream().forEach(country -> country.setCount(countryCount(country.getCode())));
		
		Country nearestCountry = countries.stream().filter(c -> !c.isSkipDistance() && c.getCount() > 0).min(Comparator.comparing(Country::getDistance)).orElse(null);
		Country farthestCountry = countries.stream().filter(c -> !c.isSkipDistance() && c.getCount() > 0).max(Comparator.comparing(Country::getDistance)).orElse(null);
		
		long acumDistance = countries.stream().filter(c -> !c.isSkipDistance() && c.getCount() > 0).mapToLong(c -> c.getDistance() * c.getCount()).sum();
		long acumCount = countries.stream().filter(c -> !c.isSkipDistance() && c.getCount() > 0).mapToLong(c -> c.getCount()).sum();

		return new Summary(nearestCountry, farthestCountry, acumCount > 0 ? acumDistance / acumCount : 0);
	}

	private Long countryCount(String code) {
		return countryRepository.findCountryCount(INTERNAL, code);
	}
	
}
