package plm.universe.bugglequest.operations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ChangeCellHasBaggle extends BuggleWorldCellOperation{

	private boolean oldHasBaggle;
	private boolean newHasBaggle;

	@JsonCreator
	public ChangeCellHasBaggle(@JsonProperty("x")int x, @JsonProperty("y")int y, @JsonProperty("oldHasBaggle")boolean oldHasBaggle, @JsonProperty("newHasBaggle")boolean newHasBaggle) {
		super("changeCellHasBaggle", x, y);
		this.oldHasBaggle = oldHasBaggle;
		this.newHasBaggle = newHasBaggle;
	}

	public boolean getOldHasBaggle() {
		return oldHasBaggle;
	}

	public boolean getNewHasBaggle() {
		return newHasBaggle;
	}
}
