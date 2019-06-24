package io.github.lr.ip2country.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;

import io.github.lr.ip2country.exceptions.ServiceUnavailableException;

/**
 * Aspecto relacionado con la politica de reintento para el consumo de un servicio web.
 * En el caso que la respuesta sea erronea y no se supere el maximo de reintentos realiza una nueva llamada. 
 * El tiempo de espera entre llamadas aumenta en forma parabolica.
 * La cantidad maxima de reintentos es configurable. No se reintenta si es cero.
 * 
 * @author lravanal
 *
 */
@Aspect
@Component
public class RetryPolicyAspect {
	
	public final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${retry.policy.maxRetries}") private int maxRetries;
	
	@Around("@annotation(RetryPolicy)")
	public Object retryPolicy(ProceedingJoinPoint joinPoint) throws Throwable {
		long start = System.currentTimeMillis();
		
		Object object = null;
		
		RetryPoliceData retryData = new RetryPoliceData();
		
		while (retryData.isFirstIntent() || retryData.canRetry()) {
			object = internalRetryPolicy(joinPoint, retryData);
			
			if (retryData.canRetry()) {
				int sleepSeconds = retryData.getIntent() * retryData.getIntent() + retryData.getIntent() + 1;

				logger.debug("Waiting {} seconds before trying again", sleepSeconds);
				
				Thread.sleep(sleepSeconds * 1_000);
				
				logger.info("Retry: {} by state code: {}", joinPoint.getSignature().toShortString(), retryData.getMessage());
			}

			retryData.addIntent();
		}

		long executionTime = System.currentTimeMillis() - start;
		 
		logger.debug("{} executed in {} ms", joinPoint.getSignature().toShortString(), executionTime);
		
		return object;
	}
	
	
	private Object internalRetryPolicy(ProceedingJoinPoint joinPoint, RetryPoliceData retryData) throws Throwable {
		retryData.disable();
		
		try {
			return joinPoint.proceed();
			
		} catch (HttpStatusCodeException hsce) {
			if (retryData.getIntent() < maxRetries && 
					  (HttpStatus.NOT_FOUND.equals(hsce.getStatusCode())
					|| HttpStatus.TOO_MANY_REQUESTS.equals(hsce.getStatusCode())
					|| HttpStatus.INTERNAL_SERVER_ERROR.equals(hsce.getStatusCode())
					|| HttpStatus.BAD_GATEWAY.equals(hsce.getStatusCode())
					|| HttpStatus.SERVICE_UNAVAILABLE.equals(hsce.getStatusCode())
					|| HttpStatus.GATEWAY_TIMEOUT.equals(hsce.getStatusCode()))) {
				
				retryData.enable();
				retryData.setMessage(hsce.getStatusCode().toString());
				
				return null;
				
			} else {
				logger.error("Could not be complete the operation: {}", joinPoint.getSignature().toShortString());
				
				throw new ServiceUnavailableException(hsce);
				
			}
		}
	}
	
	class RetryPoliceData {
		private boolean retry;
		private int intent;
		private String message;
		
		public boolean isFirstIntent() {
			return intent == 0;
		}
		public boolean canRetry() {
			return retry;
		}
		public void enable() {
			this.retry = true;
		}
		public void disable() {
			this.retry = false;
		}
		public int getIntent() {
			return intent;
		}
		public void addIntent() {
			this.intent++;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
	}
	
}
