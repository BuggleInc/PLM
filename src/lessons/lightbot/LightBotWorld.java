package lessons.lightbot;

import java.util.Arrays;
import java.util.Iterator;

import jlm.universe.Entity;
import jlm.universe.EntityControlPanel;
import jlm.universe.World;

public class LightBotWorld extends jlm.universe.World implements Iterable<LightBotWorldCell> {

	private LightBotWorldCell[][] world;

	private int sizeX;
	private int sizeY; 
	
	public LightBotWorld(String name, int x, int y) {
		super(name);
		create(x, y);
		setDelay(200);
	}

	public void create(int width, int height) {
		this.sizeX = width;
		this.sizeY = height;
		this.world = new LightBotWorldCell[sizeX][sizeY];
		for (int i = 0; i < sizeX; i++)
			for (int j = 0; j < sizeY; j++)
				world[i][j] = new LightBotWorldCell(this, i, j);
	}

	/**
	 * Create a new world being almost a copy of the first one.
	 * 
	 * @param world2
	 */
	public LightBotWorld(LightBotWorld world2) {
		super(world2);
		setName(world2.getName());
		sizeX = world2.getWidth();
		sizeY = world2.getHeight();
		this.world = new LightBotWorldCell[sizeX][sizeY];
		for (int i = 0; i < sizeX; i++)
			for (int j = 0; j < sizeY; j++) {
				world[i][j] = new LightBotWorldCell(world2.getCell(i, j));
				world[i][j].setWorld(this);
			}
	}

	@Override
	public World copy() {
		return new LightBotWorld(this);
	}

	/**
	 * Reset the content of a world to be the same than the one passed as
	 * argument does not affect the name of the initial world.
	 * 
	 * @param initialWorld
	 */
	@Override
	public void reset(World iw) {
		LightBotWorld initialWorld = (LightBotWorld) iw;
		for (int i = 0; i < sizeX; i++)
			for (int j = 0; j < sizeY; j++) {
				LightBotWorldCell c = initialWorld.getCell(i, j);
				world[i][j] = new LightBotWorldCell(c);
			}

		super.reset(initialWorld);
	}

	public LightBotWorldCell getCell(int x, int y) {
		return this.world[x][y];
	}

	public void setCell(LightBotWorldCell c, int x, int y) {
		this.world[x][y] = c;
		notifyWorldUpdatesListeners();
	}

	public int getWidth() {
		return this.sizeX;
	}

	public int getHeight() {
		return this.sizeY;
	}

	public void rotateRight() {	
		LightBotWorldCell[][] newWorld = new LightBotWorldCell[this.sizeY][this.sizeX];
		for (int y=0; y<this.sizeY; y++)
			for (int x=0; x<this.sizeX; x++) {
				LightBotWorldCell cell = this.world[x][y];
				cell.setX(this.sizeX-(y+1));
				cell.setY(x);
				newWorld[this.sizeX-(y+1)][x] = cell;
			}
		this.world = newWorld;
		int oldSizeX = this.sizeX;
		this.sizeX = this.sizeY;
		this.sizeY = oldSizeX;
		
		for (Entity entity : this.entities) {
			LightBotEntity bot = (LightBotEntity) entity;
			int x = bot.getX();
			int y = bot.getY();
			
			bot.setX(this.sizeX-(y+1));
			bot.setY(x);
			bot.right();
		}
		
		notifyWorldUpdatesListeners();
	}
	
	public void rotateLeft() {
		LightBotWorldCell[][] newWorld = new LightBotWorldCell[this.sizeY][this.sizeX];
		for (int y=0; y<this.sizeY; y++)
			for (int x=0; x<this.sizeX; x++) {
				LightBotWorldCell cell = this.world[x][y];
				cell.setX(y);
				cell.setY(this.sizeX-(x+1));
				newWorld[y][this.sizeX-(x+1)] = cell;
			}
		this.world = newWorld;
		int oldSizeX = this.sizeX;
		this.sizeX = this.sizeY;
		this.sizeY = oldSizeX;
		
		for (Entity entity : this.entities) {
			LightBotEntity bot = (LightBotEntity) entity;
			int x = bot.getX();
			int y = bot.getY();
			
			bot.setX(y);
			bot.setY(this.sizeX-(x+1));
			bot.left();
		}
		
		notifyWorldUpdatesListeners();		
	}
	
	
	
	@Override
	public LightBotWorldViewIsometric[] getView() {
		LightBotWorldViewIsometric[] res = new LightBotWorldViewIsometric[1];
		res[0] = new LightBotWorldViewIsometric(this);
		return res;
	}

	@Override
	public EntityControlPanel getEntityControlPanel() {
		return new EntityControlPanel() {
			private static final long serialVersionUID = 1L;
			@Override
			public void setEnabledControl(boolean enabled) {
			}
		};
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + sizeX;
		result = PRIME * result + sizeY;
		result = PRIME * result + Arrays.hashCode(world);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final LightBotWorld other = (LightBotWorld) obj;
		if (sizeX != other.sizeX)
			return false;
		if (sizeY != other.sizeY)
			return false;
		for (int x = 0; x < getWidth(); x++)
			for (int y = 0; y < getHeight(); y++)
				if (!getCell(x, y).equals(other.getCell(x, y)))
					return false;

		return super.equals(obj);
	}

	public class CellIterator implements Iterator<LightBotWorldCell> {
		private int x = 0;
		private int y = 0;

		@Override
		public boolean hasNext() {
			return x <= LightBotWorld.this.sizeX - 1 && y <= LightBotWorld.this.sizeY - 1;
		}

		@Override
		public LightBotWorldCell next() {
			LightBotWorldCell res = LightBotWorld.this.getCell(x, y);
			if (x >= LightBotWorld.this.sizeX - 1) {
				x = 0;
				y++;
			} else {
				x++;
			}
			return res;
		}

		@Override
		public void remove() {
			throw new RuntimeException("Method not implemented (and not implementable");
		}

	}

	public void setHeight(int x, int y, int h) {
		getCell(x, y).setHeight(h);
	}

	public void addLight(int x, int y) {
		getCell(x, y).addLight();
	}

	public void removeLight(int x, int y) {
		getCell(x, y).removeLight();
	}

	public void switchLight(int x, int y) {
		getCell(x, y).lightSwitch();
	}

	@Override
	public Iterator<LightBotWorldCell> iterator() {
		return new CellIterator();
	}

}
