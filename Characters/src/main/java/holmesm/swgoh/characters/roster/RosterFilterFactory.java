package holmesm.swgoh.characters.roster;

import java.util.Set;

import com.google.common.collect.Sets;

import holmesm.swgoh.characters.Character;
import lombok.AllArgsConstructor;

public class RosterFilterFactory {

	public static RosterFilter newStarFilter(int starCount) {
		return new StarFilter(starCount);
	}
	
	public static RosterFilter gpFilter(int gp) {
		return new GPFilter(gp);
	}
	
	public static RosterFilter newTagFilter(String[] tags) {
		return new TagFilter(Sets.newHashSet(tags));
	}
	
	
	@AllArgsConstructor
	public static class StarFilter implements RosterFilter{

		private int starCount;
		
		@Override
		public boolean meetsFilter(Character character) {
			return character.getStarCount()	>= starCount;
		}
		
	}
	@AllArgsConstructor
	public static class GPFilter implements RosterFilter{

		private int minGP;
		
		@Override
		public boolean meetsFilter(Character character) {
			return character.getGp() >= minGP;
		}
		
	}
	@AllArgsConstructor
	public static class TagFilter implements RosterFilter{

		private Set<String> tags;
		
		@Override
		public boolean meetsFilter(Character character) {
			return character.getTags().containsAll(tags);
		}
		
	}
	@AllArgsConstructor
	public static class GearFilter implements RosterFilter{

		private int gearLevel;
		
		@Override
		public boolean meetsFilter(Character character) {
			return character.getGearLevel() >= gearLevel;
		}
		
	}
	
}
