package io.github.lr.ip2country.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;

/**
 * Utilitario con funciones comunes. Contiene el calculo de distancia.
 * 
 * @author lravanal
 *
 */
public class UtilsHelper {

	private static final Logger logger = LoggerFactory.getLogger(UtilsHelper.class);
	
	public static final double AR_LAT = -34;
	public static final double AR_LNG = -64;		
	
	public static int distance2AR(double lat, double lng) {
		return (int) (distance(AR_LAT, lat, AR_LNG, lng, 0.0, 0.0) / 1000);
	}
	
	/**
	 * Calculate distance between two points in latitude and longitude 
	 * taking into account height difference. 
	 * If you are not interested in height difference pass 0.0.
	 * 
	 * lat1, lon1 Start point lat2, lon2 End point 
	 * el1 Start altitude in meters el2 End altitude in meters
	 * @returns Distance in Meters
	 */
	private static double distance(double lat1, double lat2, double lon1, double lon2, double el1, double el2) {

	    final int R = 6371; // Radius of the earth

	    double latDistance = Math.toRadians(lat2 - lat1);
	    double lonDistance = Math.toRadians(lon2 - lon1);
	    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
	            + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
	            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	    double distance = R * c * 1000; // convert to meters

	    double height = el1 - el2;

	    distance = Math.pow(distance, 2) + Math.pow(height, 2);

	    return Math.sqrt(distance);
	}
	
	@SuppressWarnings("serial")
	public static void checkStatusResponse(ResponseEntity<?> responseEntity, String operationName) {
		if (!responseEntity.getStatusCode().is2xxSuccessful()) {
			logger.info("Operation Incompleted ({}): {}", operationName, responseEntity.getStatusCodeValue());
			
			throw new HttpStatusCodeException(responseEntity.getStatusCode()) {};
		}
	}
	
}
