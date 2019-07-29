package io.github.lr.ip2country.entities;

import java.util.Objects;

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
	
	@Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Ip2Country)) {
            return false;
        }
        Ip2Country c = (Ip2Country) o;
        return code != null && code.equals(c.code);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(code);
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
