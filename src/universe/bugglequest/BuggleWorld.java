package universe.bugglequest;

import java.util.Arrays;

import universe.World;
import universe.bugglequest.ui.BuggleWorldView;


public class BuggleWorld extends universe.World {
 
	private WorldCell[][] world;

	private int sizeX;
	private int sizeY;

	private String name;
	
	public BuggleWorld(String name, int x, int y) {
		this.name = name;
		create(x, y);
	}

	public void create(int width, int height) {
		this.sizeX = width;
		this.sizeY = height;
		this.world = new WorldCell[sizeX][sizeY];
		for (int i = 0; i < sizeX; i++)
			for (int j = 0; j < sizeY; j++)
				world[i][j] = new WorldCell(this,i,j);		
	}

	/** 
		 * Create a new world being almost a copy of the first one. Beware, all the buggles of the copy are changed to BuggleRaw. 
	 * @param world2
	 */
	public BuggleWorld(BuggleWorld world2) {
		super(world2);
		this.name = world2.name;
		sizeX = world2.getWidth();
		sizeY = world2.getHeight();
		this.world = new WorldCell[sizeX][sizeY];
		for (int i = 0; i < sizeX; i++)
			for (int j = 0; j < sizeY; j++) {
				world[i][j] = new WorldCell(world2.getCell(i, j));
				world[i][j].setWorld(this);
			}
	}
	
	public World copy(){
		return new BuggleWorld(this);
	}
	/**
	 * Reset the content of a world to be the same than the one passed as argument
	 * does not affect the name of the initial world.
	 * @param initialWorld
	 */
	@Override
	public void reset(World iw) {
		BuggleWorld initialWorld = (BuggleWorld)iw;
		for (int i = 0; i < sizeX; i++)
			for (int j = 0; j < sizeY; j++) {
				WorldCell c = initialWorld.getCell(i, j);
				//world[i][j] = new BuggleWorldCell(this, i, j, c.getColor(), c.hasLeftWall(), c.hasTopWall());
				world[i][j] = new WorldCell(c);
			}

		
		super.reset(initialWorld);
	}

	public String getName() {
		return this.name;
	}
	

	public WorldCell getCell(int x, int y) {
		return this.world[x][y];
	}

	public void setCell(WorldCell c, int x, int y) {
		this.world[x][y] = c;
		notifyWorldUpdatesListeners();
	}

	public int getWidth() {
		return this.sizeX;
	}

	public int getHeight() {
		return this.sizeY;
	}

	public BuggleWorldView getView() {
		return new BuggleWorldView(this);
	}

	
	@Override
	public String toString() {
		return super.toString(); 
	/*	
		// cell
		String res = "";
		for (int j = 0; j < sizeY + 2; j++)
			res += "-";
		res += "\n";
		for (int i = 0; i < sizeX; i++) {
			res += "|";
			for (int j = 0; j < sizeY; j++)
				res += world[i][j].toString();
			res += "|\n";
		}
		for (int j = 0; j < sizeY + 2; j++)
			res += "-";
		res += "\n";

		// buggles
		//res += buggles.toString();

		// buggles
	
		Iterator<AbstractBuggle> it;
		for (it = buggles(); it.hasNext();) {
			AbstractBuggle b = it.next();
			res += "Buggle: "+b.toString()+"\n";
		}
	
		
		return res;
	*/	
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		//result = PRIME * result + ((entities == null) ? 0 : entities.hashCode());
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
		final BuggleWorld other = (BuggleWorld) obj;
		if (sizeX != other.sizeX)
			return false;
		if (sizeY != other.sizeY)
			return false;
		for (int x=0; x<getWidth(); x++) 
			for (int y=0; y<getHeight(); y++) 
				if (!getCell(x, y).equals(other.getCell(x, y)))
					return false;

		return super.equals(obj);
	}
}
