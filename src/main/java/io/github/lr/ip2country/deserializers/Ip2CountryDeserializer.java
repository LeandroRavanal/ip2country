package io.github.lr.ip2country.deserializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import io.github.lr.ip2country.entities.Ip2Country;

/**
 * Deserializador para el Json de IP a pais.
 * 
 * @author lravanal
 *
 */
public class Ip2CountryDeserializer extends JsonDeserializer<Ip2Country> {

	@Override
	public Ip2Country deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
		JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        String name = node.get("countryName").asText();
        String code = node.get("countryCode").asText();
		
		return new Ip2Country(name, code);
	}

}
