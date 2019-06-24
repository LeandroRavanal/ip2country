package io.github.lr.ip2country.controllers;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.lr.ip2country.aspects.Logging;
import io.github.lr.ip2country.entities.Country;
import io.github.lr.ip2country.entities.Summary;
import io.github.lr.ip2country.managers.Ip2CountryManager;
import io.github.lr.ip2country.managers.SummaryManager;
import io.github.lr.ip2country.utils.UtilsHelper;

/**
 * Controlador que agrupa las operaciones (puntos de acceso) a la aplicacion.
 * 
 * @author lravanal
 *
 */
@RestController	
@RequestMapping("/api/v1")
@Validated
public class FrontController {

	public final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static final String TIME_PATTERN = "HH:mm:ss";
	private static final String DATE_TIME_PATTERN = "dd/MM/yyyy HH:mm:ss";
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN).withZone(ZoneOffset.systemDefault());
	
	private static final String PARAM_IP_REGEXP = "^(25[0-5]|2[0-4][0-9]|[0-1]?[0-9]?[0-9])(\\.(25[0-5]|2[0-4][0-9]|[0-1]?[0-9]?[0-9])){3}$";
	private static final String PARAM_IP_MSG = "IP param is not valid";
	
	@Autowired private Ip2CountryManager ip2CountryManager;
	@Autowired private SummaryManager summaryManager;
	
	@Value("${currency.base}") private String currencyBase;
	
	@Logging
	@PostMapping(value = "/traceip")
	public ResponseEntity<String> traceip(@Valid @Pattern(regexp = PARAM_IP_REGEXP, message = PARAM_IP_MSG) @RequestParam String ip) {
		Country country = ip2CountryManager.consume(ip);
		
		return new ResponseEntity<String>(response(ip, country), HttpStatus.OK);
	}
	
	@Logging
	@GetMapping(value = "/summary")
	public ResponseEntity<String> summary() {
		Summary summary = summaryManager.summary();
		
		return new ResponseEntity<String>(response(summary), HttpStatus.OK);
	}
	
	private String response(String ip, Country country) {
		StringBuilder builder = new StringBuilder();

		String languages = country.getLanguages().keySet().stream().map(language -> String.format("%s (%s)", country.getLanguages().get(language), language)).collect(Collectors.joining(", "));
		String currencies = country.getCurrencies().stream().map(currency -> String.format("%s (1 %s = %.4f %s)", currency.getCode(), currency.getCode(), currency.getRate(), currencyBase)).collect(Collectors.joining(", "));
		String timezones = country.getTimezones().stream().map(timezone -> String.format("%s (%s)", DateTimeFormatter.ofPattern(TIME_PATTERN).withZone("UTC".equals(timezone) ? ZoneOffset.UTC : ZoneOffset.of(timezone.substring(3))).format(Instant.now()), timezone)).collect(Collectors.joining(" o "));
		
		builder
			.append("IP: ").append(ip).append(", fecha actual: ").append(DATE_TIME_FORMATTER.format(Instant.now())).append(Strings.LINE_SEPARATOR)
			.append("País: ").append(country.getName()).append(Strings.LINE_SEPARATOR)
			.append("ISO Code: ").append(country.getCode()).append(Strings.LINE_SEPARATOR)
			.append("Idiomas: ").append(languages).append(Strings.LINE_SEPARATOR)
			.append("Monedas: ").append(currencies).append(Strings.LINE_SEPARATOR)
			.append("Horas: ").append(timezones).append(Strings.LINE_SEPARATOR);
		
		if (!country.isSkipDistance()) {
			builder.append("Distancia estimada: ").append(country.getDistance()).append(" kms (").append(UtilsHelper.AR_LAT).append(", ").append(UtilsHelper.AR_LNG).append(") a (").append(country.getLatitude()).append(", ").append(country.getLongitude()).append(")").append(Strings.LINE_SEPARATOR);
		}
		
		String response = builder.toString();
		
		logger.debug(response);
		
		return response;
	}
	
	private String response(Summary summary) {
		Country nearestCountry = summary.getNearestCountry();
		Country farthestCountry = summary.getFarthestCountry();

		StringBuilder builder = new StringBuilder();
		if (nearestCountry != null) {
			builder.append("País más cercano: ").append(nearestCountry.getName()).append(" Distancia: ").append(nearestCountry.getDistance()).append(" kms ").append(" Invocaciones: ").append(nearestCountry.getCount()).append(Strings.LINE_SEPARATOR);
		}
		if (farthestCountry != null) {
			builder.append("País más lejano: ").append(farthestCountry.getName()).append(" Distancia: ").append(farthestCountry.getDistance()).append(" kms ").append(" Invocaciones: ").append(farthestCountry.getCount()).append(Strings.LINE_SEPARATOR);
		}
		builder.append("Distancia Promedio: ").append(summary.getAverangeDistance()).append(Strings.LINE_SEPARATOR);
		
		String response = builder.toString();
		
		logger.debug(response);
		
		return response;
	}
	
}
