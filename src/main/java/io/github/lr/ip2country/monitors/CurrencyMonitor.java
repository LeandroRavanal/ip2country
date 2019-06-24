package io.github.lr.ip2country.monitors;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.github.lr.ip2country.entities.Rates;
import io.github.lr.ip2country.managers.CurrencyManager;
import io.github.lr.ip2country.services.CurrencyService;

/**
 * Monitor para el consumo reiterado de cotizaciones (polling).
 * El inicio e intervalo son configurables.
 * 
 * @author lravanal
 *
 */
@Component
public class CurrencyMonitor {
	
	public final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired private CurrencyManager currencyManager;
	@Autowired private CurrencyService currencyService;
	
	@Value("${currency.monitor.autoStart}") private boolean autoStart;
	@Value("${currency.monitor.delaySeconds}") private int delaySeconds;

	private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
	
	public void init() {
		if (autoStart) {
			start();
		}
	}
	
	private void start() {
		logger.info("Starting Currency monitor");
		
		executor.scheduleWithFixedDelay(() -> executeCurrencyPolling(), delaySeconds, delaySeconds, TimeUnit.SECONDS);
	}
	
	private void executeCurrencyPolling() {
		try {
			logger.info("Executing Currency monitor");
			
			doExecuteCurrencyPolling();
		} catch (Exception e) {
			logger.error("Error executing Currency polling", e);
		}
	}
	
	private void doExecuteCurrencyPolling() {
		Rates rates = currencyService.findAllRates();
		
		currencyManager.updateCurrencies(rates);
	}

	@PreDestroy
	public void dispose() {
		if (executor != null) {
			logger.info("Ending Currency monitor");

			executor.shutdown();
		}
	}
	
}
