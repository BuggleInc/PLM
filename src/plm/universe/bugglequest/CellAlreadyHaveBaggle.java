package plm.universe.bugglequest;

public class CellAlreadyHaveBaggle extends BuggleWorldCellOperation{

	public CellAlreadyHaveBaggle(BuggleWorldCell cell) {
		super("cellAlreadyHaveBaggle", cell);
		setMsg(getI18n().tr("Tried to drop a baggle but there is already one in ({0}, {1})...", cell.getX(), cell.getY()));
	}
}