package plm.universe;

public abstract class GridWorldCellOperation extends Operation {

	private GridWorldCell cell;
	
	public GridWorldCellOperation(String name, GridWorldCell cell) {
		super(name);
		this.cell = cell;
	}
	
	public GridWorldCell getCell() {
		return cell;
	}
}
