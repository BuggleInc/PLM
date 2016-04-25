package plm.universe.bugglequest.operations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ChangeBuggleCarryBaggle extends BuggleOperation {
	private boolean oldCarryBaggle;
	private boolean newCarryBaggle;

	@JsonCreator
	public ChangeBuggleCarryBaggle(@JsonProperty("buggleID")String buggleID, @JsonProperty("oldCarryBaggle")boolean oldCarryBaggle, @JsonProperty("newCarryBaggle")boolean newCarryBaggle) {
		super("changeBuggleCarryBaggle", buggleID);
		this.oldCarryBaggle = oldCarryBaggle;
		this.newCarryBaggle = newCarryBaggle;
	}

	public boolean getOldCarryBaggle() {
		return oldCarryBaggle;
	}

	public boolean getNewCarryBaggle() {
		return newCarryBaggle;
	}

	public void setOldCarryBaggle(boolean oldCarryBaggle) {
		this.oldCarryBaggle = oldCarryBaggle;
	}

	public void setNewCarryBaggle(boolean newCarryBaggle) {
		this.newCarryBaggle = newCarryBaggle;
	}
}