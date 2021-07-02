package holmesm.swgoh.lambda;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GuildAllyCodes {

	private Set<PlayerSummary> players;
	
	
	public String getCSV() {
		StringBuilder builder = new StringBuilder();
		for(PlayerSummary player:players) {
			builder.append(player.toCSV());
			builder.append("\n");
		}
		builder.deleteCharAt(builder.length()-1);
		return builder.toString();
	}
}
