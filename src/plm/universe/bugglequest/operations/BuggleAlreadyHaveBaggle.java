package plm.universe.bugglequest.operations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BuggleAlreadyHaveBaggle extends BuggleOperation {
	
	@JsonCreator
	public BuggleAlreadyHaveBaggle(@JsonProperty("buggleID")String buggleID) {
		super("buggleAlreadyHaveBaggle", buggleID);
	}
}