package io.github.lr.ip2country.entities;

import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.github.lr.ip2country.deserializers.RatesDeserializer;

/**
 * Entidad de Cotizaciones.
 * 
 * @author lravanal
 *
 */
@JsonDeserialize(using = RatesDeserializer.class)
public class Rates {
	
	private static final String COMMA = ",";
	
	private String date;
	private Instant instant;
	private String base;
	private Map<String, Double> rates;
	
	public Rates(String date, Instant instant, String base, Map<String, Double> rates) {
		this.date = date;
		this.instant = instant;
		this.base = base;
		this.rates = rates;
	}

	public String getDate() {
		return date;
	}
	public Instant getInstant() {
		return instant;
	}
	public String getBase() {
		return base;
	}
	public Map<String, Double> getRates() {
		return rates;
	}
	
	@Override
	public String toString() {
		return String.format("Rates [date=%s  instant=%s  base=%s  rates=%s]", getDate(), getInstant(), getBase(), getRates().keySet().stream().map(key -> String.format("%s=%.4f", key, getRates().get(key))).collect(Collectors.joining(COMMA)));
	}
	
}
