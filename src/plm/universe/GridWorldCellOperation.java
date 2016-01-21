package plm.universe;

import org.json.simple.JSONObject;

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

	@Override
	@SuppressWarnings("unchecked")
	public JSONObject toJSON() {
		JSONObject json = super.toJSON();

		json.put("x", x);
		json.put("y", y);

		return json;
	}
}
