package plm.universe.bugglequest.operations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ChangeCellHasContent extends BuggleWorldCellOperation{

	private boolean oldHasContent;
	private boolean newHasContent;

	@JsonCreator
	public ChangeCellHasContent(@JsonProperty("x")int x, @JsonProperty("y")int y, @JsonProperty("oldHasContent")boolean oldHasContent, @JsonProperty("newHasContent")boolean newHasContent) {
		super("changeCellHasContent", x, y);
		this.oldHasContent = oldHasContent;
		this.newHasContent = newHasContent;
	}

	public boolean getOldHasContent() {
		return oldHasContent;
	}

	public boolean getNewHasContent() {
		return newHasContent;
	}
}