package io.github.lr.ip2country.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Aspecto relacionado con el logging, informa el tiempo demorado en realizar el proceso.
 * 
 * @author lravanal
 *
 */
@Aspect
@Component
public class LoggingAspect {
	private final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

	@Around("@annotation(Logging)")
	public Object logging(ProceedingJoinPoint joinPoint) throws Throwable {
		long start = System.currentTimeMillis();

		logger.debug("Executing: {}", joinPoint.getSignature().toShortString());

		try {
			return joinPoint.proceed();
		
		} finally {
			long executionTime = System.currentTimeMillis() - start;

			logger.debug("{} executed in {} ms", joinPoint.getSignature().toShortString(), executionTime);
		}
	}
	
}
