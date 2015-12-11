package plm.universe;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.xnap.commons.i18n.I18n;

import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.core.model.lesson.ExecutionProgress;
import plm.core.ui.PlmHtmlEditorKit;
import plm.core.utils.FileUtils;

public abstract class World {
	private boolean isDelayed = false; // whether we display interactively or not
	private boolean isAnswer = false;
	private boolean isError = false;
	private int delay = 100; // delay between two instruction executions of an entity.

	protected List<Entity> entities = new ArrayList<Entity>();

	private List<List<Operation>> steps = new ArrayList<List<Operation>>();

	private String name;
	private Game game;
	
	public World(Game game, String name) {
		this.name = name;
		this.game = game;
	}

	public World(World w2) {
		this(w2.game, w2.getName());
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
	public String getDebugInfo() {
		return "";
	}

	/**
	 * Reset the content of a world to be the same than the one passed as
	 * argument
	 * 
	 * @param initialWorld
	 */
	public void reset(World initialWorld) {
		steps = new ArrayList<List<Operation>>();
		entities = new ArrayList<Entity>();
		for (Entity oldEntity : initialWorld.entities) {
			try {
				Entity newEntity = oldEntity.getClass().newInstance();
				newEntity.copy(oldEntity);
				addEntity(newEntity);
			} catch (Exception e) {
				throw new RuntimeException("Cannot copy entity of class "+ oldEntity.getClass().getName(), e);
			}
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
	/** set the value of the UI delay which will be used on doDelay() 
	 * 
	 * Default value: 100ms */
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
	public void setAnswerWorld() {
		isAnswer = true;
	}
	public boolean isAnswerWorld() {
		return isAnswer;
	}
	public void setErrorWorld() {
		isError = true;
	}
	public boolean isErrorWorld() {
		return isError;
	}

	public void addEntity(Entity b) {
		if (b.getWorld() != this)
			b.setWorld(this);
		entities.add(b);
		notifyEntityUpdateListeners();
	}
	public void removeEntity(Entity b) {
		if (!entities.remove(b)) 
			getGame().getLogger().log("Ignoring a request to remove an unknown entity");
		notifyEntityUpdateListeners();		
	}

	public void emptyEntities() {
		entities = new ArrayList<Entity>();
		notifyEntityUpdateListeners();
	}

	public void setEntities(List<Entity> l) {
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
	
	public void runEntities(List<Thread> runnerVect, final ExecutionProgress progress) {
		final ProgrammingLanguage pl = getGame().getProgrammingLanguage();
		if (game.isDebugEnabled()) {
			game.getLogger().log("World:runEntities");
			game.getLogger().log("Programming language: "+pl);
		}
		
		for (final Entity b : entities) {
			Thread runner = new Thread(new Runnable() {
				public void run() {
					game.statusArgAdd(getName());
					pl.runEntity(b, progress, getGame().i18n);
					game.statusArgRemove(getName());
				}
			});

			Thread.UncaughtExceptionHandler h = new Thread.UncaughtExceptionHandler() {
			    public void uncaughtException(Thread th, Throwable ex) {
			        
			    	if(ex instanceof ThreadDeath) {
			    		String msg = "You interrupted the execution, did you fall into an infinite loop ?\n"
			    				+ "Your program must stop by itself to successfully pass the exercise.\n";
				        progress.setExecutionError(getGame().i18n.tr(msg));
				        progress.outcome = ExecutionProgress.outcomeKind.FAIL;
			    	}
			    }
			};
			
			// So that we can still stop it from the AWT Thread, even if an infinite loop occurs
			runner.setPriority(Thread.MIN_PRIORITY);
			runner.setUncaughtExceptionHandler(h);
			runner.start();
			runnerVect.add(runner);
		}
	}

	/* who's interested in every details of the world changes */
	private ArrayList<IWorldView> worldUpdatesListeners = new ArrayList<IWorldView>();

	public ArrayList<IWorldView> getWorldUpdatesListeners() {
		return worldUpdatesListeners;
	}
	
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
		if (worldUpdatesListeners.isEmpty())
			return;
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
	/** Returns whether this universe implements world I/O */
	public boolean haveIO() { 
		return false; 
	}
	public World readFromFile(String path) throws IOException, BrokenWorldFileException {
		throw new RuntimeException("This universe does not implement world I/O");
	}

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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((entities == null) ? 0 : entities.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/** Checks whether the receiver world wins the exercise
	 * 
	 * https://www.youtube.com/watch?v=9QS0q3mGPGg
	 * 
	 * @param standard a correction world that should be used to compare the receiver state
	 */
	public boolean winning(World standard) {
		return this.equals(standard);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if ( !(obj instanceof World))
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

	String about = null;

	public String getAPI(Locale humanLang, ProgrammingLanguage progLang) {
		// TODO: Buffer and share APIs among instances
		String filename = getClass().getCanonicalName().replace('.', File.separatorChar);
		String api = "File "+filename+".html not found.";
		StringBuffer sb = null;
		try {
			sb = FileUtils.readContentAsText(filename, humanLang, "html", true);
			api = PlmHtmlEditorKit.filterHTML(sb.toString(), false, progLang);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return api;
	}

	public String getAbout() {
		if (about == null) {
			String filename = getClass().getCanonicalName().replace('.', File.separatorChar);
			StringBuffer sb = null;
			try {
				sb = FileUtils.readContentAsText(filename, getGame().getLocale(), "html", true);
			} catch (IOException ex) {
				about = "File "+filename+".html not found.";
				return about;
			}
			/* read it */
			about = sb.toString();
		}
		return PlmHtmlEditorKit.filterHTML(about, game.isDebugEnabled(), getGame().getProgrammingLanguage());
	}
	
	/**
	 * Set about to null in order to allows it to be reloaded in the right language
	 */
	public void resetAbout() {
		this.about = null ;
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
	 * @throws ScriptException 
	 */
	public abstract void setupBindings(ProgrammingLanguage lang,ScriptEngine engine) throws ScriptException;

	/** Returns a textual representation of the differences from the receiver world to the one in parameter*/
	public String diffTo(World world, I18n i18n, ProgrammingLanguage progLang) {
		return diffTo(world, i18n);
	}

	public abstract String diffTo(World world, I18n i18n);
	
	public Game getGame() {
		return game;
	}

	public List<List<Operation>> getSteps() {
		return steps;
	}

	public void addStep(List<Operation> operations) {
		synchronized(steps) {
			steps.add(operations);
		}
	}
}
