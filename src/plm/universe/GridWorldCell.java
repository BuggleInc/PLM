package plm.universe;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(use = Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "type")
public abstract class GridWorldCell {

	@JsonIgnore
	protected GridWorld world;
	protected int x;
	protected int y;

	public GridWorldCell(GridWorld w, int x, int y) {
		world = w;
		this.x = x;
		this.y = y;
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

	public abstract boolean isDefaultCell();
}
