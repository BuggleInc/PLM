package plm.universe;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(use = Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "type")
public abstract class GridWorldCell {

	protected int x;
	protected int y;

	protected GridWorld world;

	public GridWorldCell(GridWorld w, int x, int y) {
		world = w;
		this.x = x;
		this.y = y;
	}

	public abstract GridWorldCell copy(GridWorld world);

	public GridWorld getWorld() {
		return this.world;
	}

	public void setWorld(GridWorld w) {
		this.world = w;
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

	public abstract boolean isDefaultCell();
}
