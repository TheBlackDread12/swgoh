package holmesm.swgoh.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import help.swgoh.api.SwgohAPI;
import help.swgoh.api.SwgohAPIBuilder;
import help.swgoh.api.SwgohAPIFilter;
import help.swgoh.api.SwgohAPI.Language;
import help.swgoh.api.SwgohAPI.PlayerField;
import help.swgoh.api.SwgohAPI.UnitsField;
import swgoh.api.model.Guild;
import swgoh.api.model.Player;

public class SWGOHClient {

	private static final String username = "YOUR USERNAME";
	private static final String password = "YOUR PASSWORD";
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	
	
	private SwgohAPI apiClient;
	
	
	public SWGOHClient() {
		 apiClient = new SwgohAPIBuilder()
			      .withUsername(username)
			      .withPassword(password)
			      .build();
	}
	
	public Guild getGuildInformation(int  memberAllyCode) {
		try {
			String guildJson = apiClient.getGuild(memberAllyCode).get();
			//System.out.println(guildJson);
			return MAPPER.readValue(guildJson, Guild[].class)[0];
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}
	
	public Player[] getPlayerDetailsForGuild(int memberAllyCode) {
		try {
			String guildJson = apiClient.getGuild(memberAllyCode).get();
			Guild[] guild = MAPPER.readValue(guildJson, Guild[].class);
			return getPlayerInformation(guild[0].getRoster()	.stream().map(stat->stat.getAllyCode()).collect(Collectors.toList()));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}
	
//public Player[] getSupportingData() {
//		
//		try {
//			String playerJson = apiClient.getSupportData(null, matchCriteria, language, filter)
//			
//			return MAPPER.readValue(playerJson, Player[].class);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
//		return null;
//	}
	
	
	public Player[] getPlayerInformation(List<Integer> allyCodes) {
		
		try {
			String playerJson = apiClient.getPlayers(allyCodes,Language.English,new SwgohAPIFilter()).get();
			
			return MAPPER.readValue(playerJson, Player[].class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}
	
	public static void main(String[] args) throws IOException {
		SWGOHClient client = new SWGOHClient();
		//client.getPlayerInformation(319316146);
		
		Map<String,String> discordIds = Maps.newHashMap();
		BufferedReader reader = new BufferedReader(new FileReader("GuildConfig\\discord"));
		String line = reader.readLine();
		while(line!=null) {
			System.out.println(line);
			String[] ids = line.split("::");
			discordIds.put(ids[1], ids[0]);
			line = reader.readLine();
		}
		reader.close();
		System.out.println("reading from SWGOH");
		Player[] allPlayers = client.getPlayerDetailsForGuild(319316146);
		List<Player> players = Lists.newArrayList(allPlayers);
		players.sort(new PlayerComparator());
		int shipGP=0,charGP = 0;
		StringBuilder builder = new StringBuilder();
		
		for(Player p:players) {
			long totalGp = p.getShipGP()+p.getCharGP();
			builder.append(discordIds.get(p.getName())+","+p.getName()+","+totalGp+","+p.getCharGP()+","+p.getShipGP()+"\n");
			shipGP+=p.getShipGP();
			charGP+=p.getCharGP();
		}
		
		FileWriter writer = new FileWriter(new File("guildOutput"+"\\"+System.currentTimeMillis()));
		writer.write(builder.toString());
		writer.flush();
		writer.close();
		System.out.println("ships:"+shipGP);
		System.out.println("chars"+charGP);
	}
	
	
	private static class PlayerComparator implements Comparator<Player>{

		@Override
		public int compare(Player arg0, Player arg1) {
			return arg0.getName().toLowerCase().compareTo(arg1.getName().toLowerCase());
		}
		
	}
	
}
