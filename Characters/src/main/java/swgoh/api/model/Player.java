package swgoh.api.model;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Player {
	private String allyCode;
	private String id;
	private String name;
	private int level;
	private String guildRefId;
	private List<PlayerStat> stats;
	private List<Toon> roster;
	private Map<String,Long> statMap;
	
	
	public long getCharGP() {
		if(statMap==null) {
			setStats();
		}
		return statMap.get("Galactic Power (Characters):");
	}
	
	public long getShipGP() {
		if(statMap==null) {
			setStats();
		}
		System.out.println(statMap);
		return statMap.get("Galactic Power (Ships):");
	}
	
	public void setStats() {
		statMap = stats.stream().collect(Collectors.toMap(stat->stat.getNameKey(), stat->stat.getValue()));
		//System.out.println(statMap);
	}
}
	