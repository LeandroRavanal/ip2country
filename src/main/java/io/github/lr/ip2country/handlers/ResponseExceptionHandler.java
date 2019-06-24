package io.github.lr.ip2country.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import io.github.lr.ip2country.exceptions.NonExistentElementException;

/**
 * Gestor de las excepciones de negocio vinculadas a validaciones.
 * 
 * @author lravanal
 *
 */
@RestController
@ControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {
	
	public final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static final String MESSAGE_NON_EXISTENT_ELEMENT = "No se pudo completar la consulta para %s";
	
	@ExceptionHandler(NonExistentElementException.class)
	public final ResponseEntity<String> handlerNonExistentElementException(NonExistentElementException neee, WebRequest request) {
		logger.info(neee.getMessage());
		
		return new ResponseEntity<String>(String.format(MESSAGE_NON_EXISTENT_ELEMENT, neee.getValue()), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
