package holmesm.swgoh.lambda;

import java.util.Set;
import java.util.stream.Collectors;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import holmesm.swgoh.client.SWGOHClient;
import swgoh.api.model.Guild;

public class GetGuildAllyCodes implements RequestHandler<PlayerIdentifier,GuildAllyCodes>{

	@Override
	public GuildAllyCodes handleRequest(PlayerIdentifier arg0, Context arg1) {
		SWGOHClient client = new SWGOHClient();
		Guild guild = client.getGuildInformation(Integer.parseInt(arg0.getAllyCode()));
		Set<PlayerSummary> players = guild.getRoster().stream().map(player -> PlayerSummary.fromGuildPlayer(player)).collect(Collectors.toSet());
		return new GuildAllyCodes(players);
	}

}
