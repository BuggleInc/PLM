package jlm.universe;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.swing.ImageIcon;

import jlm.core.model.Game;
import jlm.core.model.ProgrammingLanguage;
import jlm.core.model.Reader;
import jlm.core.ui.WorldView;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public abstract class World {
	@Attribute
	private boolean isDelayed = false; // whether we display interactively or not
	private int delay = 100; // delay between two instruction executions of an entity.

	@ElementList
	protected ArrayList<Entity> entities = new ArrayList<Entity>();

	@Attribute
	private String name;

	public World(String name) {
		this.name = name;
	}

	public World(World w2) {
		this(w2.getName());
		reset(w2);
	}

	public World copy() {
		World res=null;
		try {
			res = this.getClass().getConstructor(this.getClass()).newInstance(this);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return res;
	}

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
		this.isDelayed = initialWorld.isDelayed;
		this.delay = initialWorld.delay;
		this.parameters = (initialWorld.parameters!=null?initialWorld.parameters.clone():null);
		notifyEntityUpdateListeners();
		notifyWorldUpdatesListeners();
	}

	public String getName() {
		return this.name;
	}

	public void setName(String n) {
		name = n;
	}

	public boolean isDelayed() {
		return isDelayed;
	}
	/** returns the delay to apply */
	public int getDelay() {
		return this.delay;
	}
	/** set the value of the UI delay which will be used on doDelay() */
	public void setDelay(int d) {
		this.delay = d;
		notifyWorldUpdatesListeners(); // notify the speed slider model
	}
	/** set current UI delay to what was defined as max UI delay with setDelayUI() */
	public void doDelay() {
		isDelayed = true;
	}
	/** set current UI delay to 0 */
	public void doneDelay() {
		isDelayed = false;
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
	public List<Entity> getEntities() {
		return entities;
	}

	public void runEntities(List<Thread> runnerVect) {
		// Logger.log("World:runEntities","");
		for (final Entity b : entities) {
			Thread runner = new Thread(new Runnable() {
				public void run() {
					Game.getInstance().statusArgAdd(getName());
					ProgrammingLanguage progLang = Game.getProgrammingLanguage(); 
					try {
						if (progLang.equals(Game.JAVA)||progLang.equals(Game.LIGHTBOT)) {
							b.run();
						} else {
							ScriptEngineManager manager = new ScriptEngineManager();       
							ScriptEngine engine = manager.getEngineByName(progLang.getLang().toLowerCase());
							if (engine==null) 
								throw new RuntimeException("Failed to start an interpreter for "+progLang.getLang().toLowerCase());

							engine.put("entity", b);
							engine.eval(b.getWorld().getBindings(progLang));
							String script = b.getScript(progLang);
							if (script == null) {
								System.out.println("no script source for entity "+b);
								//script = Game.getInstance().getCurrentLesson().getCurrentExercise().scriptSources.get(Game.getProgrammingLanguage()).get(b)
							}
							engine.eval(script);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					Game.getInstance().statusArgRemove(getName());
				}
			});

			// in order to be able to stop it from the AWT Thread in case of
			// an infinite loop
			runner.setPriority(Thread.MIN_PRIORITY);

			runner.start();
			runnerVect.add(runner);
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
	public void readFromFile(BufferedReader br) throws IOException {}

	public void writeToFile(BufferedWriter f) throws IOException {}

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
	public WorldView[] getView() {
		return new WorldView[] { new WorldView(this) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isWorldCompatible(World world) {
				return false;
			}
			
			@Override
			public ImageIcon getIcon() {
				return null; // FIXME: not sure about this?
			}
		}};
	}
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
			String filename = getClass().getCanonicalName().replace('.', File.separatorChar);
			StringBuffer sb = Reader.fileToStringBuffer(filename, "html", true);
			if (sb==null) {
				about = "File "+filename+".html not found.";
				return about;
			}
			/* read it */
			about = "<html>\n" + HTMLMissionHeader + "<body>\n" + sb.toString() + "</body>\n</html>\n";
		}

		return about;
	}

	protected Object[] parameters = null;
	public void setParameter(Object[] parameters) {
		this.parameters = parameters;		
	}
	public Object[] getParameters() {
		return parameters;
	}
	public Object getParameter(int i){
		return parameters[i];
	}

	public void setSelectedEntity(Entity e) {
		notifyWorldUpdatesListeners();//EntityUpdateListeners();
	}
	/** Returns the script except that must be injected within the environment before running user code
	 * 
	 * It should pass all order to the java entity, which were injected independently  
	 */
	public abstract String getBindings(ProgrammingLanguage lang);

	/** Returns a textual representation of the differences from the receiver world to the one in parameter*/
	public abstract String diffTo(World world);
}
