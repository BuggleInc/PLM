package plm.universe;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedDeque;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import plm.core.lang.ProgrammingLanguage;
import plm.core.log.Logger;
import plm.core.model.lesson.ExecutionProgress;
import plm.core.model.lesson.UserSettings;
import plm.core.ui.PlmHtmlEditorKit;
import plm.core.utils.FileUtils;

@JsonTypeInfo(use = Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@jsonId")
public abstract class World  {
	private UserSettings settings;

	protected List<Entity> entities = new ArrayList<Entity>();

	private ConcurrentLinkedDeque<List<SVGOperation>> steps = new ConcurrentLinkedDeque<List<SVGOperation>>();

	//private ConcurrentLinkedDeque<List<SVGOperation>> SVGOperations = new ConcurrentLinkedDeque<List<SVGOperation>>();

	protected final FileUtils fileUtils;
	private String name;


	public World(FileUtils fileUtils, String name) {
		this.fileUtils = fileUtils;
		this.name = name;
	}

	public World(World w2) {
		this(w2.fileUtils, w2.getName());
		// TODO: Implement clone() in all Operation's subclasses
		/*
		for(List<Operation> operations : steps) {
			List<Operation> clone = new ArrayList<Operation>();
			for(Operation operation : operations) {
				clone.add(operation.clone());
			}
			steps.addFirst(clone);
		}
		*/
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
	@JsonIgnore
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
		steps = new ConcurrentLinkedDeque<List<SVGOperation>>();
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
		this.parameters = (initialWorld.parameters!=null?initialWorld.parameters.clone():null);
	}

	public String getName() {
		return this.name;
	}

	public void setName(String n) {
		name = n;
	}

	public void addEntity(Entity b) {
		if (b.getWorld() != this)
			b.setWorld(this);
		entities.add(b);
	}
	public void removeEntity(Entity b) {
		if (!entities.remove(b)) 
			Logger.debug("Ignoring a request to remove an unknown entity");
	}

	/** Run all entities of the world, until their natural end (or somebody from outside kill them on timeout) */
	@SuppressWarnings("deprecation")
	public CompletableFuture<Void> runEntities(final ProgrammingLanguage progLang,
			final ExecutionProgress progress, final Locale locale, long timeoutMilli) {
		
		return CompletableFuture.runAsync(() -> {
			List<EntityRunner> allRunners = new ArrayList<EntityRunner>();  
			long startTime = System.currentTimeMillis();

			// Start all entities, each in one thread
			for (final Entity entity : getEntities()) {
				EntityRunner runner = new EntityRunner(entity, progress, progLang, locale);

				// So that we can still stop it from the maestro Thread, even if an infinite loop occurs
				runner.setPriority(Thread.MIN_PRIORITY);
				runner.start();
				allRunners.add(runner);
			}

			// Run all entities, step after step. 
			// Kill everyone as soon as the timeout occurs  
			List<EntityRunner> trash = new ArrayList<EntityRunner>();  
			boolean timeout = false;
			while (!timeout && !allRunners.isEmpty()) {
				for (EntityRunner runner : allRunners) {
					runner.entity.allowOneStep();

					long now = System.currentTimeMillis();
					long elapsed = now-startTime;
					if (runner.entity.waitStepEnd(timeoutMilli - elapsed) == false) {
						timeout = true;
						Logger.error("TIMEOUT after "+(System.currentTimeMillis()-startTime));
						break; // Don't run the other entities once the timeout fires
					}
					if (!runner.isExecuting())
						trash.add(runner);
				}

				/*Here, generate a frame of the world*/
				//Logger.info(this.draw().get(0).getOperation());
				addStep(this.draw());

				for (EntityRunner dead : trash)
					allRunners.remove(dead);
				if (timeout) {
					for (EntityRunner runner : allRunners) 
						runner.stop();
					progress.setTimeoutError();
				}
			}
			//Logger.info("Done with exercise "+getName());
		});
	}

	protected abstract List<SVGOperation> draw();

	public void emptyEntities() {
		entities = new ArrayList<Entity>();
	}

	public void setEntities(List<Entity> l) {
		entities = l;
	}

	@JsonIgnore
	public int getEntityCount() {
		return entities.size();
	}

	public Entity getEntity(int i) {
		return entities.get(i);
	}

	public List<Entity> getEntities() {
		return entities;
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

	@JsonIgnore
	public String getAPI() {
		// TODO: Buffer and share APIs among instances
		String filename = getClass().getCanonicalName().replace('.', File.separatorChar);
		String api = "File "+filename+".html not found.";
		StringBuffer sb = null;
		try {
			sb = fileUtils.readContentAsText(filename, getLocale(), "html", true);
			api = PlmHtmlEditorKit.filterHTML(sb.toString(), false, getProgLang());
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
				sb = fileUtils.readContentAsText(filename, getLocale(), "html", true);
			} catch (IOException ex) {
				about = "File "+filename+".html not found.";
				return about;
			}
			/* read it */
			about = sb.toString();
		}
		return PlmHtmlEditorKit.filterHTML(about, false, getProgLang());
	}
	
	/**
	 * Set about to null in order to allows it to be reloaded in the right language
	 */
	public void resetAbout() {
		this.about = null ;
	}

	@JsonTypeInfo(use = Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "type")
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

	/** Returns the script except that must be injected within the environment before running user code
	 * 
	 * It should pass all order to the java entity, which were injected independently  
	 * @throws ScriptException 
	 */
	public abstract void setupBindings(ProgrammingLanguage lang,ScriptEngine engine) throws ScriptException;

	/** Returns a textual representation of the differences from the receiver world to the one in parameter*/
	public abstract String diffTo(World world);

	public ConcurrentLinkedDeque<List<SVGOperation>> getSteps() {
		return steps;
	}

	public void setSteps(ConcurrentLinkedDeque<List<SVGOperation>> steps) {
		this.steps = steps;
	}

	public void addStep(List<SVGOperation> operations) {
		//Logger.info("addStep : ");
		if(operations != null && steps != null) {
			steps.add(operations);
		} else {
			Logger.error("Steps ou Opertions NULL");
			Logger.info(this.getName());
		}
	}

	@JsonIgnore
	public UserSettings getSettings() {
		return settings;
	}

	public void setSettings(UserSettings settings) {
		this.settings = settings;
	}
	
	@JsonIgnore
	public Locale getLocale() {
		if(settings != null) {
			return settings.getHumanLang();
		}
		return Locale.getDefault();
	}
	
	@JsonIgnore
	public ProgrammingLanguage getProgLang() {
		return settings.getProgLang();
	}
}
