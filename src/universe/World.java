package universe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import org.simpleframework.xml.*;

@Root
public abstract class World {
	@Attribute
	private int delay = 0; // delay between two instruction executions of an entity.
	
	@ElementList
	private ArrayList<Entity> entities = new ArrayList<Entity>();

	public World(){}
	
	public World(World w2) {
		for (Entity e : w2.entities) {
			Entity e2 = e.copy();
			e2.setWorld(this);
			entities.add(e2);			
		}
		this.delay = w2.delay;
	}

	public abstract World copy();

	/**
	 * Reset the content of a world to be the same than the one passed as argument
	 * @param initialWorld
	 */
	public void reset(World initialWorld) {
		entities = new ArrayList<Entity>();
		for (Entity b : initialWorld.entities) {
			Entity br = b.copy();
			br.setWorld(this);
			entities.add(br);
		}
		this.delay = initialWorld.delay;
		notifyEntityUpdateListeners();
		notifyWorldUpdatesListeners();
	}			
	
	public int getDelay() {
		return this.delay;
	}

	public void setDelay(int d) {
		this.delay = d;
	}
	
	public void addEntity(Entity b) {
		entities.add(b);
		notifyEntityUpdateListeners();
	}

	public void emptyEntities(){
		entities = new ArrayList<Entity>();
		notifyEntityUpdateListeners();
	}

	public void setEntities(ArrayList<Entity> l) {
		entities = l;
		notifyEntityUpdateListeners();
	}

	public int entityCount() {
		return entities.size();
	}
	public Entity getEntity(int i) {
		return entities.get(i);
	}	

	public void runEntities(Vector<Thread> runnerVect) {
		//Logger.log("World:runEntities","");
		for (final Entity b : entities) {
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

	public Iterator<Entity> entities() {
		return entities.iterator();
	}	

	
	/* who's interested in every details of the world changes */
	private ArrayList<IWorldView> worldUpdatesListeners = new ArrayList<IWorldView>();

	/* who's only interested in entities creation and destructions */ 
	private ArrayList<IWorldView> entitiesUpdateListeners = new ArrayList<IWorldView>();

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
	
	public void addEntityUpdateListener(IWorldView v) {
		this.entitiesUpdateListeners.add(v);
	}

	public void removeEntityUpdateListener(IWorldView v) {
		this.entitiesUpdateListeners.remove(v);
	}

	public void notifyEntityUpdateListeners() {
		for (IWorldView v : this.entitiesUpdateListeners) {
			v.worldHasChanged();
		}
	}
	
	/* Find my UI */
	public abstract jlm.ui.WorldView getView();
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final World other = (World) obj;

		if (entities == null) {
			if (other.entities != null) 
				return false;
		} else if (!entities.equals(other.entities)) {
			return false;
		}

		return true;
	}
}
