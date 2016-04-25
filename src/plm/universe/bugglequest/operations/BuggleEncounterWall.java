package plm.universe.bugglequest.operations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BuggleEncounterWall extends BuggleOperation {
	
	@JsonCreator
	public BuggleEncounterWall(@JsonProperty("buggleID")String buggleID) {
		super("buggleEncounterWall", buggleID);
	}

}
