package swgoh.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Skill {
	
	private String id;
	private int tier;
	private String nameKey;
	private boolean isZeta;
}
