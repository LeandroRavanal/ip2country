package io.github.lr.ip2country.repositories;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import io.github.lr.ip2country.entities.Country;
import io.github.lr.ip2country.exceptions.NonExistentElementException;
import io.github.lr.ip2country.services.CountryService;

/**
 * Repositorio para el cacheo de paises y su cantidad de accesos.
 * 
 * @author lravanal
 *
 */
@Repository
public class CountryRepository {
	
	public final Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final String COUNTRY_COUNT = "countrycount_";
	
	@Autowired private CountryService countryService;
	
    @Autowired private RedisTemplate<String, Long> redisTemplate;
	
	@Cacheable(cacheNames = "countries")
	public List<Country> findAll() {
		logger.debug("Find all Country list");
		
		return countryService.findAllCountries();
	}
	
	public void addCountryCount(String code) {
		ValueOperations<String, Long> valueOperations = redisTemplate.opsForValue();
		
		Boolean flag = valueOperations.setIfAbsent(COUNTRY_COUNT + code, 0L);
		
		if (Boolean.TRUE.equals(flag)) {
			logger.debug("Set Country Count {}: {}", code, 0);
		}
	}
	
	public Long findCountryCount(String ip, String code) {
		ValueOperations<String, Long> valueOperations = redisTemplate.opsForValue();
		
		Long count = (Long) valueOperations.get(COUNTRY_COUNT + code);
		
		if (count == null) {
			logger.debug("Should be returned Precached Country Count {}", code);

			throw new NonExistentElementException(ip, code);
		}
		
		logger.debug("Country Count Returned {}: {}", code, count);

		return count;
	}
	
	public Long incrementCount(String code) {
		ValueOperations<String, Long> valueOperations = redisTemplate.opsForValue();
		
		Long count = valueOperations.increment(COUNTRY_COUNT + code);
		
		logger.debug("Country Count Increased {}: {}", code, count);
		
		return count;
	}
	
}
