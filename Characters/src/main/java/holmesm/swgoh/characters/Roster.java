package holmesm.swgoh.characters;

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.google.common.collect.Sets;

import holmesm.swgoh.characters.roster.RosterFilter;

public class Roster {
	Map<String,Character> characters;
	
	public void applyFilter(RosterFilter filter) {
		Set<String> toRemove = Sets.newHashSet();
		for(Entry<String,Character> character:characters.entrySet()) {
			if(!filter.meetsFilter(character.getValue())) {
				toRemove.add(character.getKey());
			}
		}
		for(String id:toRemove) {
			characters.remove(id);
		}
	}
}
