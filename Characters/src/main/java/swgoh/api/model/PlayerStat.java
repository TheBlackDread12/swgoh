package swgoh.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayerStat {
	private String nameKey;
	private long value;
}
