package io.github.lr.ip2country.deserializers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import io.github.lr.ip2country.entities.Country;
import io.github.lr.ip2country.entities.Currency;
import io.github.lr.ip2country.managers.CurrencyManager;

/**
 * Deserializador para el Json de Pais.
 * 
 * @author lravanal
 *
 */
public class CountryDeserializer extends JsonDeserializer<Country> {
	
    @Override
    public Country deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        String name = node.get("name").asText();
        String code = node.get("alpha2Code").asText();
        
        Map<String, String> languages = new HashMap<>();
        node.get("languages").forEach(language -> {
        	if (language.has("iso639_1")) {
        		languages.put(language.get("iso639_1").asText(), language.get("name").asText());
        	}
        });

        List<Currency> currencies = new ArrayList<>();
        node.get("currencies").forEach(currency -> {
        	if (currency.has("name") && currency.has("code")) {
        		currencies.add(CurrencyManager.getInstance().getCurrency(currency.get("name").asText(), currency.get("code").asText()));
        	}
        });
        
        List<String> timezones = new ArrayList<>();
        node.get("timezones").forEach(timezone -> timezones.add(timezone.asText()));
        
        if (node.get("latlng").size() == 0) {
        	
        	return new Country(name, code, languages, currencies, timezones);
        }
        
        double latitude = node.get("latlng").get(0).asDouble();
        double longitude = node.get("latlng").get(1).asDouble();
        
        return new Country(name, code, languages, currencies, timezones, latitude, longitude, Country.CODE_AR.equals(code));
    }
    
}
