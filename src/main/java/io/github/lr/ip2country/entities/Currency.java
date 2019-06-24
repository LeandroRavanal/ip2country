package io.github.lr.ip2country.entities;

import java.io.Serializable;

/**
 * Entidad de Moneda.
 * 
 * @author lravanal
 *
 */
public class Currency implements Serializable {

	private static final long serialVersionUID = 1649023502600960681L;
	
	private String name;
	private String code;
	private double rate;

	public Currency(String name, String code) {
		this.name = name;
		this.code = code;
	}
	
	public String getName() {
		return name;
	}
	public String getCode() {
		return code;
	}
	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	@Override
	public String toString() {
		return String.format("Currency [name=%s  code=%s  rate=%.4f]", getName(), getCode(), getRate());
	}
	
}
