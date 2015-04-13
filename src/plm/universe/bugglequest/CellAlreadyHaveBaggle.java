package plm.universe.bugglequest;

import org.xnap.commons.i18n.I18n;

public class CellAlreadyHaveBaggle extends BuggleWorldCellOperation{

	public CellAlreadyHaveBaggle(BuggleWorldCell cell, I18n i18n) {
		super("cellAlreadyHaveBaggle", cell);
		setMsg(i18n.tr("Tried to drop a baggle but there is already one in ({0}, {1})...", cell.getX(), cell.getY()));
	}
}