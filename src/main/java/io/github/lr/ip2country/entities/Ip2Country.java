package io.github.lr.ip2country.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.github.lr.ip2country.deserializers.Ip2CountryDeserializer;

/**
 * Entidad de IP a pais.
 * 
 * @author lravanal
 *
 */
@JsonDeserialize(using = Ip2CountryDeserializer.class)
public class Ip2Country {

	private String name;
	private String code;
	
	public Ip2Country(String name, String code) {
		this.name = name;
		this.code = code;
	}

	public String getName() {
		return name;
	}
	public String getCode() {
		return code;
	}
	
	@Override
	public String toString() {
		return String.format("Ip2Country [name=%s  code=%s]", getName(), getCode());
	}
	
}
