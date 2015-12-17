package plm.universe.lightbot;

import java.util.Arrays;
import java.util.Iterator;

import javax.script.ScriptEngine;

import org.xnap.commons.i18n.I18n;

import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.universe.Entity;
import plm.universe.GridWorld;
import plm.universe.World;

public class LightBotWorld extends plm.universe.GridWorld implements Iterable<LightBotWorldCell> {

	public LightBotWorld(Game game, String name, int x, int y) {
		super(game, name,x,y);
		setDelay(200);
	}
	protected LightBotWorldCell newCell(int x, int y) {
		return new LightBotWorldCell(this, x, y);
	}

	/**
	 * Create a new world being almost a copy of the first one.
	 * 
	 * @param world2
	 */
	public LightBotWorld(LightBotWorld world2) {
		super(world2);
	}

	/**
	 * Reset the content of a world to be the same than the one passed as
	 * argument. Does not affect the name of the initial world.
	 */
	@Override
	public void reset(World iw) {
		GridWorld initialWorld = (GridWorld) iw;
		for (int i = 0; i < sizeX; i++)
			for (int j = 0; j < sizeY; j++) {
				LightBotWorldCell c = (LightBotWorldCell) initialWorld.getCell(i, j);
				cells[i][j] = new LightBotWorldCell(c,this);
			}

		super.reset(initialWorld);
	}

	public void rotateRight() {	
		LightBotWorldCell[][] newWorld = new LightBotWorldCell[this.sizeY][this.sizeX];
		for (int y=0; y<this.sizeY; y++)
			for (int x=0; x<this.sizeX; x++) {
				LightBotWorldCell cell = (LightBotWorldCell) this.cells[x][y];
				cell.setX(this.sizeX-(y+1));
				cell.setY(x);
				newWorld[this.sizeX-(y+1)][x] = cell;
			}
		this.cells = newWorld;
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
				LightBotWorldCell cell = (LightBotWorldCell) this.cells[x][y];
				cell.setX(y);
				cell.setY(this.sizeX-(x+1));
				newWorld[y][this.sizeX-(x+1)] = cell;
			}
		this.cells = newWorld;
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
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + sizeX;
		result = PRIME * result + sizeY;
		result = PRIME * result + Arrays.hashCode(cells);
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
		final GridWorld other = (GridWorld) obj;
		if (getWidth() != other.getWidth())
			return false;
		if (getHeight() != other.getHeight())
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
			LightBotWorldCell res = (LightBotWorldCell) LightBotWorld.this.getCell(x, y);
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
		((LightBotWorldCell) getCell(x, y)).setHeight(h);
	}

	public void addLight(int x, int y) {
		((LightBotWorldCell) getCell(x, y)).addLight();
	}

	public void removeLight(int x, int y) {
		((LightBotWorldCell) getCell(x, y)).removeLight();
	}

	public void switchLight(int x, int y) {
		((LightBotWorldCell) getCell(x, y)).lightSwitch();
	}

	@Override
	public Iterator<LightBotWorldCell> iterator() {
		return new CellIterator();
	}
	@Override
	public void setupBindings(ProgrammingLanguage lang, ScriptEngine e) {
		throw new RuntimeException("No binding of LightbotWorld for "+lang);
	}
	@Override
	public String diffTo(World other, I18n i18n) {
		return "null";
	}
}
