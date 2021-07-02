package holmesm.swgoh.characters.roster;

import holmesm.swgoh.characters.Character;
import lombok.Builder;
import lombok.Getter;

public interface RosterFilter {

	public boolean meetsFilter(Character character);
}



