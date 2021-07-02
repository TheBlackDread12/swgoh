package holmesm.swgoh.lambda;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PlayerIdentifier {
	private String allyCode;

	
	public PlayerIdentifier() {
		
	}
	
	public PlayerIdentifier(String allyCode) {
		this.allyCode = allyCode;
	}
}
