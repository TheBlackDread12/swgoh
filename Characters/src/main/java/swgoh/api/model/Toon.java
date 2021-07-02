package swgoh.api.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Toon {
	private String id;
	private String defId;
	private String nameKey;
	private int rarity;
	private int level;
	private int gear;
	private List<Skill> skills;
	private List<Crew> crew;
	@Override
	public String toString() {
		return "Toon [id=" + id + ", defId=" + defId + ", nameKey=" + nameKey + ", rarity=" + rarity + ", level="
				+ level + ", gear=" + gear + ", skills=" + skills + ", crew=" + crew + "]";
	}
}
