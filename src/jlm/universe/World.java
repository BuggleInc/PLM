package jlm.universe;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import jlm.ui.WorldView;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;


@Root
public abstract class World {
	@Attribute
	private int delay = 0; // delay between two instruction executions of an entity.

	@ElementList
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	
	@Attribute
	private String name;

	public World(String name){
		this.name = name;
	}

	public World(World w2) {
		this(w2.getName());
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
	public String getName() {
		return this.name;
	}
	protected void setName(String n) {
		name = n;
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
	/* IO related */
	public abstract void readFromFile(BufferedReader br) throws IOException;
	public abstract void writeToFile(BufferedWriter f) throws IOException;
	public void writeToFile(File outputFile) throws IOException {
		BufferedWriter bw = null;
		FileWriter fw = null;
		try {
			fw = new FileWriter(outputFile);
			bw = new BufferedWriter(fw);
			this.writeToFile(bw);
		} catch (IOException e) {
			throw e;
		} finally {
			if (bw != null)  bw.close();
		}
	}
	public void readFromFile(File inputFile) throws IOException {
		BufferedReader br = null;
		FileReader fr = null;
		try {
			fr = new FileReader(inputFile);
			br = new BufferedReader(fr);
			readFromFile(br);
		} catch (IOException e) {
			throw e;
		} finally {
			if (br != null) br.close();
		}

	}

	/* Find my UI */
	public abstract WorldView getView();
	public abstract EntityControlPanel getEntityControlPanel();

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
