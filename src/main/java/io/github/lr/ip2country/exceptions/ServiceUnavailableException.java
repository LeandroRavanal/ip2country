package io.github.lr.ip2country.exceptions;

/**
 * Excepcion asociada al estado de servicio no disponible.
 * 
 * @author lravanal
 *
 */
public class ServiceUnavailableException extends RuntimeException {

	private static final long serialVersionUID = 51678221963558895L;
	
	private static final String MSG = "Service Unavailable";
	
	public ServiceUnavailableException() {
		super(MSG);
	}
	
	public ServiceUnavailableException(Throwable t) {
		super(MSG, t);
	}
	
}
