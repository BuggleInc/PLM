package plm.universe.bugglequest.operations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ChangeBuggleBrushDown extends BuggleOperation {
	private boolean oldBrushDown;
	private boolean newBrushDown;

	@JsonCreator
	public ChangeBuggleBrushDown(@JsonProperty("buggleID")String buggleID, @JsonProperty("oldBrushDown")boolean oldBrushDown, @JsonProperty("newBrushDown")boolean newBrushDown) {
		super("changeBuggleBrushDown", buggleID);
		this.oldBrushDown = oldBrushDown;
		this.newBrushDown = newBrushDown;
	}

	public boolean getOldBrushDown() {
		return oldBrushDown;
	}

	public boolean getNewBrushDown() {
		return newBrushDown;
	}

	public void setOldBrushDown(boolean oldBrushDown) {
		this.oldBrushDown = oldBrushDown;
	}

	public void setNewBrushDown(boolean newBrushDown) {
		this.newBrushDown = newBrushDown;
	}
}