package io.github.lr.ip2country.exceptions;

/**
 * Excepcion asociada a un elemento inexistente.
 * 
 * @author lravanal
 *
 */
public class NonExistentElementException extends RuntimeException {

	private static final long serialVersionUID = 2828551272020231911L;

	private static final String MSG = "Non Existent Element (%s) : %s";
	
	private String value;
	
	public NonExistentElementException(String value, String code) {
		super(String.format(MSG, code, value));
		
		this.value = value;
	}
	
	public NonExistentElementException(String value, String code, Throwable t) {
		super(String.format(MSG, code, value), t);
		
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
