package plm.universe.bugglequest.operations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CellAlreadyHaveBaggle extends BuggleWorldCellOperation{

	@JsonCreator
	public CellAlreadyHaveBaggle(@JsonProperty("x")int x, @JsonProperty("y")int y) {
		super("cellAlreadyHaveBaggle", x, y);
	}
}