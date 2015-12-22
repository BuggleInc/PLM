package plm.universe;

import org.json.simple.JSONObject;

import plm.core.model.ToJSON;

public abstract class GridWorldCell implements ToJSON {

	protected GridWorld world;
	protected int x;
	protected int y;

	public GridWorldCell(GridWorld w, int x, int y) {
		world = w;
		this.x = x;
		this.y = y;
	}

	public GridWorldCell(JSONObject json) {
		x = ((Long) json.get("x")).intValue();
		y = ((Long) json.get("y")).intValue();
	}

	public abstract GridWorldCell copy(GridWorld world);

	public GridWorld getWorld() {
		return this.world;
	}

	public int getX() {
		return this.x;
	}

	public void setX(int newX) {
		this.x = newX;
	}

	public int getY() {
		return this.y;
	}

	public void setY(int newY) {
		this.y = newY;
	}

	public void setWorld(GridWorld w) {
		this.world = w;
	}

	@SuppressWarnings("unchecked")
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();

		json.put("type", getJSONType());
		json.put("x", x);
		json.put("y", y);

		return json;
	}

	public abstract boolean isDefaultCell();
}
