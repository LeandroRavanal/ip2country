package io.github.lr.ip2country.exceptions;

/**
 * Excepcion asociada a una cotizacion de moneda inexistente.
 * 
 * @author lravanal
 *
 */
public class CurrencyRatesException extends RuntimeException {

	private static final long serialVersionUID = 3858096665271103809L;
	
	private static final String MSG = "Rates Error %d - %s";
	
	public CurrencyRatesException(int code, String info) {
		super(String.format(MSG, code, info));
	}

}
