package plm.universe;

import java.lang.reflect.InvocationTargetException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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

	public GridWorld(JSONObject json) {
		super(json);

		int x = ((Long) json.get("sizeX")).intValue();
		int y = ((Long) json.get("sizeY")).intValue();

		create(x, y);

		JSONArray jsonCells = (JSONArray) json.get("cells");
		for(int i=0; i<jsonCells.size(); i++) {
			JSONObject jsonCell = (JSONObject) jsonCells.get(i);

			String type = (String) jsonCell.get("type");
			int xCell = ((Long) jsonCell.get("x")).intValue();
			int yCell = ((Long) jsonCell.get("y")).intValue();

			try {
				cells[xCell][yCell] = (GridWorldCell) Class.forName(type).getDeclaredConstructor(JSONObject.class).newInstance(jsonCell);
			} catch (InstantiationException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException
					| ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
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

	@Override
	@SuppressWarnings("unchecked")
	public JSONObject toJSON() {
		JSONObject json = super.toJSON();

		// We just need to add the non-default cells
		JSONArray jsonCells = new JSONArray();
		for(GridWorldCell[] rowCell : cells) {
			for(GridWorldCell cell : rowCell) {
				if(!cell.isDefaultCell()) {
					jsonCells.add(cell.toJSON());
				}
			}
		}

		json.put("sizeX", sizeX);
		json.put("sizeY", sizeY);
		json.put("visibleGrid", visibleGrid);
		json.put("cells", jsonCells);

		return json;
	}
}
