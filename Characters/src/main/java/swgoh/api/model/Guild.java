package swgoh.api.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Guild {
	private String id;
	private String name;
	private List<GuildPlayerSummary> roster;
	
	
	@Getter
	@AllArgsConstructor
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class GuildPlayerSummary{
		private int allyCode;
		private int level;
		private String name;
		private int gp;
		private int gpChar;
		private int gpShip;
	}
}
