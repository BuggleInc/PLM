package jlm.universe;


public class GridWorldCell {

	protected GridWorld world;
	protected int x;
	protected int y;

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
}
