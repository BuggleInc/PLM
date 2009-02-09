package jlm.universe;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jlm.core.Game;
import jlm.ui.WorldView;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public abstract class World {
	@Attribute
	private int delay = 0; // delay between two instruction executions of an
	// entity.

	@ElementList
	private ArrayList<Entity> entities = new ArrayList<Entity>();

	@Attribute
	private String name;

	public World(String name) {
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
	 * Reset the content of a world to be the same than the one passed as
	 * argument
	 * 
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

	public void emptyEntities() {
		entities = new ArrayList<Entity>();
		notifyEntityUpdateListeners();
	}

	public void setEntities(ArrayList<Entity> l) {
		entities = l;
		notifyEntityUpdateListeners();
	}

	public int getEntityCount() {
		return entities.size();
	}

	public Entity getEntity(int i) {
		return entities.get(i);
	}

	public void runEntities(List<Thread> runnerVect) {
		// Logger.log("World:runEntities","");
		for (final Entity b : entities) {
			if (runnerVect != null) {
				Thread runner = new Thread(new Runnable() {
					public void run() {
						Game.getInstance().statusArgAdd(getName());
						b.run();
						Game.getInstance().statusArgRemove(getName());
					}
				});

				// in order to be able to stop it from the AWT Thread in case of
				// an infinite loop
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
		synchronized (this.worldUpdatesListeners) {
			this.worldUpdatesListeners.add(v);
		}
	}

	public void removeWorldUpdatesListener(IWorldView v) {
		synchronized (this.worldUpdatesListeners) {
			this.worldUpdatesListeners.remove(v);
		}
	}

	public void notifyWorldUpdatesListeners() {
		synchronized (this.worldUpdatesListeners) {
			for (IWorldView v : this.worldUpdatesListeners) {
				v.worldHasMoved();
			}
		}
	}

	public void addEntityUpdateListener(IWorldView v) {
		synchronized (this.entitiesUpdateListeners) {
			this.entitiesUpdateListeners.add(v);
		}
	}

	public void removeEntityUpdateListener(IWorldView v) {
		synchronized (this.entitiesUpdateListeners) {
			this.entitiesUpdateListeners.remove(v);
		}
	}

	public void notifyEntityUpdateListeners() {
		synchronized (this.entitiesUpdateListeners) {
			for (IWorldView v : this.entitiesUpdateListeners) {
				v.worldHasChanged();
			}
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
			if (bw != null)
				bw.close();
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
			if (br != null)
				br.close();
		}

	}

	/* Find my UI */
	public abstract WorldView getView();

	public abstract EntityControlPanel getEntityControlPanel();
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((entities == null) ? 0 : entities.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		World other = (World) obj;
		if (entities == null) {
			if (other.entities != null)
				return false;
		} else if (!entities.equals(other.entities))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	final static String HTMLMissionHeader = "<head>\n" + "  <meta content=\"text/html; charset=UTF-8\" />\n"
			+ "  <style>\n"
			+ "    body { font-family: tahoma, \"Times New Roman\", serif; font-size:10px; margin:10px; }\n"
			+ "    code { background:#EEEEEE; }\n" + "    pre { background: #EEEEEE;\n" + "          margin: 5px;\n"
			+ "          padding: 6px;\n" + "          border: 1px inset;\n" + "          width: 640px;\n"
			+ "          overflow: auto;\n" + "          text-align: left;\n"
			+ "          font-family: \"Courrier New\", \"Courrier\", monospace; }\n"
			+ "   .comment { background:#EEEEEE;\n" + "              font-family: \"Times New Roman\", serif;\n"
			+ "              color:#00AA00;\n" + "              font-style: italic; }\n" + "  </style>\n" + "</head>\n";
	String about = null;

	public String getAbout() {
		if (about == null) {
			String filename = getClass().getCanonicalName().replace('.', File.separatorChar) + ".html";

			String newLine = System.getProperty("line.separator");

			BufferedReader br = null;
			/* try to find the file */
			try {
				br = new BufferedReader(new FileReader(new File(filename)));
			} catch (FileNotFoundException e) {
				// external HTML file of this exercise not found on file system.
				// Give as resource, in case we are in a jar file
				String resourceName = "/" + getClass().getCanonicalName().replace('.', '/') + ".html";

				InputStream s = World.class.getResourceAsStream(resourceName);
				if (s == null) {
					about = "File " + filename + " and resource " + resourceName + " not found.";
					return about; /* file not found, give up */
				}

				try {
					br = new BufferedReader(new InputStreamReader(s, "UTF-8"));
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
					about = "About world file encoding is not supported on this platform (please report this bug)";
					return about;
				}
			}

			/* read it */
			try {
				StringBuffer sb = new StringBuffer();
				String s;
				s = br.readLine();
				while (s != null) {
					sb.append(s);
					sb.append(newLine);
					s = br.readLine();
				}
				about = "<html>\n" + HTMLMissionHeader + "<body>\n" + sb.toString() + "</body>\n</html>\n";
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return about;
	}
}
