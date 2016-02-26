package plm.universe.bugglequest.operations;

import plm.universe.bugglequest.BuggleWorldCell;

public class ChangeCellHasContent extends BuggleWorldCellOperation{

	private boolean oldHasContent;
	private boolean newHasContent;

	public ChangeCellHasContent(BuggleWorldCell cell, boolean oldHasContent, boolean newHasContent) {
		super("changeCellHasContent", cell);
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