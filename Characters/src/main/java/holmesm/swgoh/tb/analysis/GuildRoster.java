package holmesm.swgoh.tb.analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class GuildRoster {

	
	private Map<String,PlayerName> players;
	
	public GuildRoster (String rosterLocation, String overridesLocation) throws IOException {
		
		players = Maps.newHashMap();
		
		
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(rosterLocation)));
		String line = reader.readLine();
		int rankCount = 1;
		while(line!=null) {
			String name = line.toLowerCase();
			players.put(name, new PlayerName(name,rankCount));
			line = reader.readLine();
			rankCount++;
		}
		reader.close();
		
		reader = new BufferedReader(new FileReader(new File(overridesLocation)));
		line = reader.readLine();
		while(line!=null) {
			String[] names = line.toLowerCase().split(":");
			PlayerName player = players.get(names[0]);
			for(int i=1;player!=null&&i<names.length;i++) {
				player.addAlias(names[i]);
			}
			line = reader.readLine();
		}
		reader.close();
	}
	
	
	public List<PlayerName> getOrderedNames(){
		List<PlayerName> returnList = Lists.newArrayList(players.values());
		returnList.sort(PlayerName.getComparator());
		return returnList;
	}
	
	public Set<String> getPrimaryNames(){
		return players.keySet();
	}
	
	public Map<String,PlayerName> getPlayers(){
		return players;
	}
	
	public Map<String,PlayerName> getExpandedNames(){
		Map<String,PlayerName> returnMap = Maps.newHashMap();
		for(PlayerName player:players.values()) {
			for(String name:player.getAllNames()) {
				returnMap.put(name, player);
			}
		}
		return returnMap;
	}
	
	
	public static class PlayerName implements Comparable<PlayerName>,Comparator<PlayerName>{
		private String primaryName;
		private int rank;
		private Set<String> aliases;
		
		public PlayerName(String primaryName, int rank) {
			this.primaryName = primaryName;
			this.rank = rank;
			aliases = Sets.newHashSet();
		}
		
		public void addAlias(String alias) {
			aliases.add(alias);
		}
		
		public String getPrimaryName() {
			return primaryName;
		}
		
		public int getRank() {
			return rank;
		}
		
		public Set<String> getAliases(){
			return aliases;
		}
		
		public Set<String> getAllNames(){
			Set<String> returnSet = Sets.newHashSet();
			returnSet.add(primaryName);
			returnSet.addAll(aliases);
			return returnSet;
		}

		@Override
		public int compareTo(PlayerName arg0) {
			
			if(arg0==null) {
				return -1;
			}
			PlayerName otherPlayer = (PlayerName)arg0;
			return rank - otherPlayer.getRank();
		}
		
		
		public static Comparator<PlayerName> getComparator(){
			return new PlayerName("",0);
		}
		
		@Override
		public int compare(PlayerName arg0, PlayerName arg1) {
			return arg0.compareTo(arg1);
		}
		
		public String toString() {
			StringBuilder builder = new StringBuilder(primaryName);
			for(String alias:aliases) {
				builder.append("::"+alias);
			}
			return builder.toString();
		}
	}
	
	
}
