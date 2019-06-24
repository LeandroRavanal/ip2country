package io.github.lr.ip2country.deserializers;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import io.github.lr.ip2country.entities.Rates;
import io.github.lr.ip2country.exceptions.CurrencyRatesException;

/**
 * Deserializador para el Json de Cotizaciones.
 * 
 * @author lravanal
 *
 */
public class RatesDeserializer extends JsonDeserializer<Rates> {

	private static final String NO_DETAILS = "No details";
	
	@Override
	public Rates deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
		JsonNode node = jsonParser.getCodec().readTree(jsonParser);
		
		boolean success = node.get("success").asBoolean();
		
		if (success) {
			String date = node.get("date").asText();
	        Instant instant = Instant.ofEpochMilli(node.get("timestamp").asLong());
	        String base = node.get("base").asText();
			
	        Map<String, Double> rates = new HashMap<>();
	        node.get("rates").fields().forEachRemaining(field -> rates.put(field.getKey(), field.getValue().asDouble()));
	        
	        return new Rates(date, instant, base, rates);
		} else {
			
			JsonNode errorNode = node.get("error");
			
			int errorCode = errorNode.has("code") ? errorNode.get("code").asInt() : 0;
			String errorInfo = errorNode.has("info") ? errorNode.get("info").asText() : NO_DETAILS;
			
			throw new CurrencyRatesException(errorCode, errorInfo);
		}
	}

}
