package jlm.universe;






public abstract class GridWorld extends World {

	protected GridWorldCell[][] cells;
	protected int sizeX;
	protected int sizeY;
	protected boolean visibleGrid=true;

	public GridWorld(String name, int x, int y) {
		super(name);
		create(x, y);
	}

	public GridWorld(GridWorld world2) {
		super(world2);
		sizeX = world2.getWidth();
		sizeY = world2.getHeight();
		visibleGrid = world2.visibleGrid;
		this.cells = new GridWorldCell[sizeX][sizeY];
		for (int i = 0; i < sizeX; i++)
			for (int j = 0; j < sizeY; j++) {
				cells[i][j] = world2.getCell(i, j).copy(this);
			}
	}

	public void create(int width, int height) {
		this.sizeX = width;
		this.sizeY = height;
		this.cells = new GridWorldCell[sizeX][sizeY];
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
	public boolean getVisibleGrid() {
		return visibleGrid;
	}
	public void setVisibleGrid(boolean s) {
		visibleGrid=s;
	}
}
