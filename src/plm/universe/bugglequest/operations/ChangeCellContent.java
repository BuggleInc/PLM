package plm.universe.bugglequest.operations;

import plm.universe.bugglequest.BuggleWorldCell;

public class ChangeCellContent extends BuggleWorldCellOperation{

	private String oldContent;
	private String newContent;

	public ChangeCellContent(BuggleWorldCell cell, String oldContent, String newContent) {
		super("changeCellContent", cell);
		this.oldContent = oldContent;
		this.newContent = newContent;
	}

	public String getOldContent() {
		return oldContent;
	}

	public String getNewContent() {
		return newContent;
	}
}