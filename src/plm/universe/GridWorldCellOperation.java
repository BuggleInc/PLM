package plm.universe;

public abstract class GridWorldCellOperation extends Operation {

	private GridWorldCell cell;
	private int x;
	private int y;
	
	public GridWorldCellOperation(String name, GridWorldCell cell) {
		super(name);
		this.cell = cell;
		this.x = cell.x;
		this.y = cell.y;
	}
	
	public GridWorldCell getCell() {
		return cell;
	}
}
