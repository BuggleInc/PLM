package plm.universe.bugglequest.operations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class NoBaggleUnderBuggle extends BuggleOperation {
	
	@JsonCreator
	public NoBaggleUnderBuggle(@JsonProperty("buggleID")String buggleID) {
		super("noBaggleUnderBuggle", buggleID);
	}
}