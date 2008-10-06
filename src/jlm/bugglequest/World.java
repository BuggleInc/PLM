package jlm.bugglequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Vector;

public class World {
	private ArrayList<AbstractBuggle> buggles = new ArrayList<AbstractBuggle>();

	private WorldCell[][] world;

	private int sizeX;
	private int sizeY;

	/* who's interested in every details of the world changes */
	private ArrayList<IWorldView> worldUpdatesListeners = new ArrayList<IWorldView>();

	/* who's only interested in buggle creation and destructions */ 
	private ArrayList<IWorldView> bugglesUpdateListeners = new ArrayList<IWorldView>();

	private int delay = 0; // delay between two instruction executions of a buggle.

	private String name;
	
	public World(String name, int x, int y) {
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
	public World(World world2) {
		this.name = world2.name;
		sizeX = world2.getWidth();
		sizeY = world2.getHeight();
		this.world = new WorldCell[sizeX][sizeY];
		for (int i = 0; i < sizeX; i++)
			for (int j = 0; j < sizeY; j++) {
				WorldCell c = world2.getCell(i, j);
				world[i][j] = new WorldCell(c);
				c.setWorld(this);
			}

		for (AbstractBuggle b : world2.buggles) {
			AbstractBuggle br = new Buggle(b);
			br.setWorld(this);
			buggles.add(br);			
		}
		this.delay = world2.delay;

	}
	/**
	 * Reset the content of a world to be the same than the one passed as argument
	 * does not affect the name of the initial world.
	 * @param initialWorld
	 */
	public void reset(World initialWorld) {
		for (int i = 0; i < sizeX; i++)
			for (int j = 0; j < sizeY; j++) {
				WorldCell c = initialWorld.getCell(i, j);
				//world[i][j] = new BuggleWorldCell(this, i, j, c.getColor(), c.hasLeftWall(), c.hasTopWall());
				world[i][j] = new WorldCell(c);
			}

		buggles = new ArrayList<AbstractBuggle>();
		for (AbstractBuggle b : initialWorld.buggles) {
			AbstractBuggle br = new Buggle(b);
			br.setWorld(this);
			buggles.add(br);
		}

		this.delay = initialWorld.delay;
		
		notifyBugglesUpdateListeners();
		notifyWorldUpdatesListeners();
	}

	public String getName() {
		return this.name;
	}
	
	public void addBuggle(AbstractBuggle b) {
		buggles.add(b);
		notifyBugglesUpdateListeners();
	}

	public void emptyBuggles(){
		buggles = new ArrayList<AbstractBuggle>();
		notifyBugglesUpdateListeners();
	}

	public void setBuggles(ArrayList<AbstractBuggle> l) {
		buggles = l;
		notifyBugglesUpdateListeners();
	}

	public int bugglesCount() {
		return buggles.size();
	}
	public AbstractBuggle getBuggle(int i) {
		return buggles.get(i);
	}	

	public void runBuggles(Vector<Thread> runnerVect) {
		//Logger.log("World:runBuggles","");
		for (final AbstractBuggle b : buggles) {
			if (runnerVect != null) {
				Thread runner = new Thread(new Runnable(){
					public void run() {
						b.run();
					}
				});
				
				// in order to be able to stop it from the AWT Thread in case of an infinite loop
				runner.setPriority(Thread.MIN_PRIORITY);
				
				runner.start();
				runnerVect.add(runner);
			} else {
				// everything in current thread
				b.run();
			}
		}
	}

	public Iterator<AbstractBuggle> buggles() {
		return buggles.iterator();
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

	public int getDelay() {
		return this.delay;
	}

	public void setDelay(int d) {
		this.delay = d;
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

	public void addWorldUpdatesListener(IWorldView v) {
		this.worldUpdatesListeners.add(v);
	}

	public void removeWorldUpdatesListener(IWorldView v) {
		this.worldUpdatesListeners.remove(v);
	}

	public void notifyWorldUpdatesListeners() {
		for (IWorldView v : this.worldUpdatesListeners) {
			v.worldHasMoved();
		}
	}
	
	public void addBugglesUpdateListener(IWorldView v) {
		this.bugglesUpdateListeners.add(v);
	}

	public void removeBugglesUpdateListener(IWorldView v) {
		this.bugglesUpdateListeners.remove(v);
	}

	public void notifyBugglesUpdateListeners() {
		for (IWorldView v : this.bugglesUpdateListeners) {
			v.worldHasChanged();
		}
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((buggles == null) ? 0 : buggles.hashCode());
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
		final World other = (World) obj;
		if (sizeX != other.sizeX)
			return false;
		if (sizeY != other.sizeY)
			return false;
		if (buggles == null) {
			if (other.buggles != null) 
				return false;
		} else if (!buggles.equals(other.buggles)) {
			return false;
		}
		for (int x=0; x<getWidth(); x++) 
			for (int y=0; y<getHeight(); y++) 
				if (!getCell(x, y).equals(other.getCell(x, y)))
					return false;

		return true;
	}
}
