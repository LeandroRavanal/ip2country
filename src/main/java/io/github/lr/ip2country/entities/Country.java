package io.github.lr.ip2country.entities;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.github.lr.ip2country.deserializers.CountryDeserializer;
import io.github.lr.ip2country.utils.UtilsHelper;

/**
 * Entidad de Pais.
 * 
 * @author lravanal
 *
 */
@JsonDeserialize(using = CountryDeserializer.class)
public class Country implements Serializable {

	private static final long serialVersionUID = 3545011383095292847L;

	public static final String CODE_AR = "AR";

	private static final String COMMA = ",";
	
	private String name;
	private String code;
	private Map<String, String> languages;
	private List<Currency> currencies;
	private List<String> timezones;
	private double latitude;
	private double longitude;
	
	private boolean skipDistance;
	private int distance;
	private long count;
	
	public Country(String name, String code, Map<String, String> languages, List<Currency> currencies, List<String> timezones, double latitude, double longitude, boolean skipDistance) {
		this.name = name;
		this.code = code;
		this.languages = languages;
		this.currencies = currencies;
		this.timezones = timezones;
		this.latitude = latitude;
		this.longitude = longitude;
		this.skipDistance = skipDistance;
		
		if (!this.skipDistance) {
			this.distance = UtilsHelper.distance2AR(this.latitude, this.longitude);
		}
	}

	public Country(String name, String code, Map<String, String> languages, List<Currency> currencies, List<String> timezones) {
		this(name, code, languages, currencies, timezones, 0, 0, true);
	}

	public String getName() {
		return name;
	}
	public String getCode() {
		return code;
	}
	public Map<String, String> getLanguages() {
		return languages;
	}
	public List<Currency> getCurrencies() {
		return currencies;
	}
	public List<String> getTimezones() {
		return timezones;
	}
	public double getLatitude() {
		return latitude;
	}
	public double getLongitude() {
		return longitude;
	}

	public boolean isSkipDistance() {
		return skipDistance;
	}
	public int getDistance() {
		return distance;
	}
	public long getCount() {
		return this.count;
	}
	
	public void setCount(long count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return String.format("Country [name=%s  code=%s  languages=%s  currencies=%s  timezones=%s  latlng=%.2f-%.2f  skipDistance=%b  distance=%d  count=%d]", getName(), getCode(), getLanguages().keySet().stream().map(key -> String.format("%s=%s", key, getLanguages().get(key))).collect(Collectors.joining(COMMA)), getCurrencies().stream().map(currency -> currency.getCode()).collect(Collectors.joining(COMMA)), String.join(COMMA, getTimezones()), getLatitude(), getLongitude(), isSkipDistance(), getDistance(), getCount());
	}
	
}
