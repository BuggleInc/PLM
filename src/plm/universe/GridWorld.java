package plm.universe;

import plm.core.model.Game;

public abstract class GridWorld extends World {

	protected GridWorldCell[][] cells;
	protected int sizeX;
	protected int sizeY;
	protected boolean visibleGrid=true;

	public GridWorld(Game game, String name, int x, int y) {
		super(game, name);
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

	protected void create(int width, int height) {
		this.sizeX = width;
		this.sizeY = height;
		this.cells = new GridWorldCell[sizeX][sizeY];
		for (int i = 0; i < sizeX; i++)
			for (int j = 0; j < sizeY; j++)
				setCell(newCell(i, j), i, j) ;
	}
	protected abstract GridWorldCell newCell(int x, int y);
	
	public void setWidth(int width) {
		GridWorldCell[][] oldCells = cells;
		this.cells = new GridWorldCell[width][sizeY];
		for (int i = 0; i< Math.min(width, sizeX); i++) 
			for (int j = 0; j < sizeY; j++)
				cells[i][j] = oldCells[i][j];
		
		if (width>sizeX) // need to increase the table size
			for (int i = sizeX; i< width; i++) 
				for (int j = 0; j < sizeY; j++)
					cells[i][j] = newCell(i, j);
			
		sizeX = width;
	}

	public void setHeight(int height) {
		GridWorldCell[][] oldCells = cells;
		this.cells = new GridWorldCell[sizeX][height];
		for (int i = 0; i< sizeX; i++)  {
			for (int j = 0; j < Math.min(height, sizeY); j++)
				cells[i][j] = oldCells[i][j];
			if (height>sizeY) // need to increase the table size
				for (int j = sizeY; j < height; j++)
					cells[i][j] = newCell(i, j);
		}
		
		sizeY = height;
	}

	public GridWorldCell[][] getCells() {
		return cells;
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
