package swgoh.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Crew {
	private String unitId;
	private int gp;
	private int cp;
}
