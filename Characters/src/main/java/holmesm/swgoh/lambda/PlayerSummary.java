package holmesm.swgoh.lambda;

import lombok.AllArgsConstructor;
import lombok.Getter;
import swgoh.api.model.Guild.GuildPlayerSummary;
import swgoh.api.model.Player;

@Getter
@AllArgsConstructor
public class PlayerSummary {

	private String name;
	private String allyCode;
	private int level;
	private long charGp;
	private long shipGp;
	
	public static PlayerSummary fromPlayer(Player p) {
		return new PlayerSummary(p.getName(),p.getAllyCode(),p.getLevel(),p.getCharGP(),p.getShipGP());
	}
	
	public static PlayerSummary fromGuildPlayer(GuildPlayerSummary playerSummary) {
		return new PlayerSummary(playerSummary.getName(),playerSummary.getAllyCode()+"",playerSummary.getLevel(),playerSummary.getGpChar(),playerSummary.getGpShip());
	}
	
	public String toCSV() {
		StringBuilder builder = new StringBuilder();
		builder.append(allyCode);
		builder.append(",");
		builder.append(name);
		builder.append(",");
		builder.append(charGp);
		builder.append(",");
		builder.append(shipGp);
		builder.append(",");
		builder.append(level);
		return builder.toString();
	}
}
