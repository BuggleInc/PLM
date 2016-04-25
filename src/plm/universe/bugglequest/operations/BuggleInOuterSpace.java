package plm.universe.bugglequest.operations;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BuggleInOuterSpace extends BuggleOperation {
		
	public BuggleInOuterSpace(@JsonProperty("buggleID")String buggleID) {
		super("buggleInOuterSpace", buggleID);
	}
}
