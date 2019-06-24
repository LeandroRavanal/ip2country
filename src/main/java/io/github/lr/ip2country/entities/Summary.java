package io.github.lr.ip2country.entities;

/**
 * Entidad de Resumen.
 * 
 * @author lravanal
 *
 */
public class Summary {

	private static final String NONE = "NONE";
	
	private Country nearestCountry;
	private Country farthestCountry;
	private long averangeDistance;
	
	public Summary(Country nearestCountry, Country farthestCountry, long averangeDistance) {
		this.nearestCountry = nearestCountry;
		this.farthestCountry = farthestCountry;
		this.averangeDistance = averangeDistance;
	}

	public Country getNearestCountry() {
		return nearestCountry;
	}
	public Country getFarthestCountry() {
		return farthestCountry;
	}
	public long getAverangeDistance() {
		return averangeDistance;
	}
	
	@Override
	public String toString() {
		return String.format("Summary [nearestCountry=%s  farthestCountry=%s  averangeDistance=%d]", getNearestCountry() != null ? getNearestCountry().getName() : NONE, getFarthestCountry() != null ? getFarthestCountry().getName() : NONE, getAverangeDistance());
	}
	
}
