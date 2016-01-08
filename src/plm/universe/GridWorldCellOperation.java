package plm.universe;

import org.json.simple.JSONObject;

public abstract class GridWorldCellOperation extends Operation {

	private GridWorldCell cell;
	
	public GridWorldCellOperation(String name, GridWorldCell cell) {
		super(name);
		this.cell = cell;
	}
	
	public GridWorldCell getCell() {
		return cell;
	}

	@Override
	@SuppressWarnings("unchecked")
	public JSONObject toJSON() {
		JSONObject json = super.toJSON();

		json.put("cell", cell.toJSON());

		return json;
	}
}
