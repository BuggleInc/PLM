package jlm.universe.robozzle;

import java.util.Arrays;
import java.util.Iterator;

import jlm.core.ui.WorldView;
import jlm.universe.Entity;
import jlm.universe.GridWorld;
import jlm.universe.World;

public class RobozzleWorld extends jlm.universe.GridWorld implements Iterable<RobozzleWorldCell> {

	public RobozzleWorld(String name, int w, int h) {
		super(name,w,h);
		setDelay(200);
	}
	
	@Override
	public void create(int x, int y) {
		super.create(x,y);
		for (int i = 0; i < sizeX; i++)
			for (int j = 0; j < sizeY; j++)
				setCell(new RobozzleWorldCell(this, i, j), i, j) ;
	}
	/**
	 * Create a new world being almost a copy of the first one.
	 * 
	 * @param world2
	 */
	public RobozzleWorld(RobozzleWorld world2) {
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
				RobozzleWorldCell c = (RobozzleWorldCell) initialWorld.getCell(i, j);
				cells[i][j] = new RobozzleWorldCell(c,this);
			}

		super.reset(initialWorld);
	}

	
	
	@Override
	public WorldView[] getView() {
		return new WorldView[] { new RobozzleWorldView(this) };
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

	public class CellIterator implements Iterator<RobozzleWorldCell> {
		private int x = 0;
		private int y = 0;

		@Override
		public boolean hasNext() {
			return x <= RobozzleWorld.this.sizeX - 1 && y <= RobozzleWorld.this.sizeY - 1;
		}

		@Override
		public RobozzleWorldCell next() {
			RobozzleWorldCell res = (RobozzleWorldCell) RobozzleWorld.this.getCell(x, y);
			if (x >= RobozzleWorld.this.sizeX - 1) {
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

	public void setColor(int x, int y, char color) {
		((RobozzleWorldCell) getCell(x, y)).setColor(color);
	}
	
	public void addStar(int x, int y) {
		((RobozzleWorldCell) getCell(x, y)).addStar();
	}

	public void removeStar(int x, int y) {
		((RobozzleWorldCell) getCell(x, y)).removeStar();
	}

	@Override
	public Iterator<RobozzleWorldCell> iterator() {
		return new CellIterator();
	}

}
