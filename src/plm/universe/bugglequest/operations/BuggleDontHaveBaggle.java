package plm.universe.bugglequest.operations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BuggleDontHaveBaggle extends BuggleOperation {
	
	@JsonCreator
	public BuggleDontHaveBaggle(@JsonProperty("buggleID")String buggleID) {
		super("buggleDontHaveBaggle", buggleID);
	}
}
