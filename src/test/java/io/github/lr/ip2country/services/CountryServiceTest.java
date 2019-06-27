package io.github.lr.ip2country.services;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import io.github.lr.ip2country.entities.Country;

@RunWith(SpringRunner.class)
@EnableAutoConfiguration
@Import(CountryService.class)
@TestPropertySource(properties = {"country.concurrent.search=true", "country.url=/", "country.url.regions=/{0}"})
public class CountryServiceTest {

	@Autowired private CountryService countryService;
	
	@MockBean private RestTemplate restTemplate;
	
	@Test
	public void countriesList() {
		Country[] africa = 		{ createCountry("Ghana", "GH"), 	createCountry("South Africa", "ZA") };
		Country[] americas = 	{ createCountry("Argentina", "AR"), createCountry("Mexico", "MX") };
		Country[] asia = 		{ createCountry("India", "IN"), 	createCountry("Japan", "JP") };
		Country[] europe = 		{ createCountry("France", "FR"), 	createCountry("Italy", "IT") };
		Country[] oceania = 	{ createCountry("Australia", "AU"), createCountry("Fiji", "FJ") };
		
		List<Country> countries = Stream.concat(Stream.concat(Stream.concat(Stream.concat(Stream.of(africa), Stream.of(americas)), Stream.of(asia)), Stream.of(europe)), Stream.of(oceania)).collect(Collectors.toList());
		
		ParameterizedTypeReference<List<Country>> parameterizedTypeReference = parameterizedTypeReference();
		
		Mockito.when(restTemplate.exchange("/africa", HttpMethod.GET, null, parameterizedTypeReference)).thenReturn(createResponse(africa));
		Mockito.when(restTemplate.exchange("/americas", HttpMethod.GET, null, parameterizedTypeReference)).thenReturn(createResponse(americas));
		Mockito.when(restTemplate.exchange("/asia", HttpMethod.GET, null, parameterizedTypeReference)).thenReturn(createResponse(asia));
		Mockito.when(restTemplate.exchange("/europe", HttpMethod.GET, null, parameterizedTypeReference)).thenReturn(createResponse(europe));
		Mockito.when(restTemplate.exchange("/oceania", HttpMethod.GET, null, parameterizedTypeReference)).thenReturn(createResponse(oceania));

		List<Country> allCountries = countryService.findAllCountries();
		
		Assert.assertEquals(new HashSet<>(allCountries), new HashSet<>(countries));
	}
	
	private Country createCountry(String name, String code) {
		return new Country(name, code, null, null, null);
	}

	private ParameterizedTypeReference<List<Country>> parameterizedTypeReference() {
		return new ParameterizedTypeReference<List<Country>>() {};
	}
	
	private ResponseEntity<List<Country>> createResponse(Country[] countries) {
		return new ResponseEntity<>(Arrays.asList(countries), HttpStatus.OK);
	}
	
}
