package jlm.universe;




public abstract class GridWorld extends World {

	protected GridWorldCell[][] cells;
	protected int sizeX;
	protected int sizeY;

	public GridWorld(String name) {
		super(name);
	}

	public GridWorld(GridWorld world2) {
		super(world2);
	}

	public GridWorldCell getCell(int x, int y) {
		return this.cells[x][y];
	}

	public void setCell(GridWorldCell c, int x, int y) {
		this.cells[x][y] = c;
		notifyWorldUpdatesListeners();
	}

	public int getWidth() {
		return this.sizeX;
	}

	public int getHeight() {
		return this.sizeY;
	}
}
