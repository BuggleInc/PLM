package plm.universe.bugglequest;

import org.xnap.commons.i18n.I18n;

public class ChangeCellContent extends BuggleWorldCellOperation{

	private String oldContent;
	private String newContent;

	public ChangeCellContent(BuggleWorldCell cell, String oldContent, String newContent, I18n i18n) {
		super("changeCellContent", cell);
		this.oldContent = oldContent;
		this.newContent = newContent;
		
		String msg = i18n.tr("Cell ({0}, {1}) has new content: \"{2}\"", cell.getX(), cell.getY(), newContent);
		setMsg(msg);
	}

	public String getOldContent() {
		return oldContent;
	}

	public String getNewContent() {
		return newContent;
	}
}