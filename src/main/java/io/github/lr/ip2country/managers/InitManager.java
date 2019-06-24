package io.github.lr.ip2country.managers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import io.github.lr.ip2country.monitors.CurrencyMonitor;

/**
 * Administrador inicial de obtencion de informacion.
 * 
 * @author lravanal
 *
 */
@Component
public class InitManager {
	
	public final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired private CountryManager countryManager;
	@Autowired private CurrencyManager currencyManager;
	@Autowired private CurrencyMonitor currencyMonitor;

	@EventListener
	public void init(ContextRefreshedEvent event) {
		logger.info("Starting App data");
		
		countryManager.init();
		
		currencyManager.init();
		
		currencyMonitor.init();
		
		logger.info("Ending App data");
	}
	
}
