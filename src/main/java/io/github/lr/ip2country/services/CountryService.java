package io.github.lr.ip2country.services;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import io.github.lr.ip2country.aspects.RetryPolicy;
import io.github.lr.ip2country.entities.Country;
import io.github.lr.ip2country.utils.UtilsHelper;

/**
 * Servicio para el consumo de los servicios web de pais.
 * Los paises se obtienen Todos a la vez o por Regiones.
 * 
 * @author lravanal
 *
 */
@Service
public class CountryService {

	public final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static final String[] REGIONS = new String[] {"africa", "americas", "asia", "europe", "oceania"};
	private static final String ALL = "all";
	
	@Value("${country.concurrent.search}") private boolean concurrentSearch;

	@Value("${country.url}") private String url;
	@Value("${country.url.regions}") private String urlRegions;
	
	@Value("${processors.quantity:5}") private int processorsQuantity;
	
	@SuppressWarnings("unchecked")
	@RetryPolicy
	public List<Country> findAllCountries() {
		if (!concurrentSearch) {
			
			logger.debug("Search all Countries");

			return findCountries(url, ALL);
		}
		
		logger.debug("Search Countries by Region");

		final Queue<Country> countries = new ConcurrentLinkedQueue<>();
		
		final CompletableFuture<Void>[] futures = new CompletableFuture[REGIONS.length];
		
		// Multiprocessor - https://github.com/LeandroRavanal/querythreads
		final ExecutorService executor = Executors.newFixedThreadPool(processorsQuantity);
		
		for (int i = 0; i < REGIONS.length; i++) {
		
			final String region = REGIONS[i];
			final String urlRegion = MessageFormat.format(urlRegions, region);
			
			futures[i] = CompletableFuture.runAsync(() -> countries.addAll(findCountries(urlRegion, region)), executor);
		}
		
		try {
			CompletableFuture.allOf(futures).join();
		
		} catch (CompletionException ce) {
			logger.error("Could not be complete the segmented search", ce);
			
			throw ce;
		
		} finally {
			if (executor != null) {
				executor.shutdown();
			}
			
		}
		
		return new ArrayList<>(countries);
	}
	
	private List<Country> findCountries(String url, String region) {
		logger.debug("Searching Countries ({})", region);
		
	    RestTemplate restTemplate = new RestTemplate();
	    
	    ResponseEntity<List<Country>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Country>>(){});
	    
	    UtilsHelper.checkStatusResponse(response, "Countries");
	    
	    return response.getBody();
	}
	
}
