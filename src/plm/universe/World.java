package plm.universe;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentLinkedDeque;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import plm.core.lang.ProgrammingLanguage;
import plm.core.log.Logger;
import plm.core.model.ToJSON;
import plm.core.model.lesson.UserSettings;
import plm.core.ui.PlmHtmlEditorKit;
import plm.core.utils.FileUtils;

public abstract class World implements ToJSON {
	private boolean isDelayed = false; // whether we display interactively or not
	private boolean isAnswer = false;
	private boolean isError = false;
	private int delay = 100; // delay between two instruction executions of an entity.
	private UserSettings settings;

	protected List<Entity> entities = new ArrayList<Entity>();

	private ConcurrentLinkedDeque<List<Operation>> steps = new ConcurrentLinkedDeque<List<Operation>>();

	private String name;
	
	public World(String name) {
		this.name = name;
	}

	public World(JSONObject json) {
		this.name = (String) json.get("name");

		JSONArray jsonEntities = (JSONArray) json.get("entities");
		for(int i=0; i<jsonEntities.size(); i++) {
			JSONObject jsonEntity = (JSONObject) jsonEntities.get(i);
			String type = (String) jsonEntity.get("type");
			try {
				Entity e = (Entity) Class.forName(type).getDeclaredConstructor(JSONObject.class).newInstance(jsonEntity);
				entities.add(e);
			} catch (InstantiationException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException
					| ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
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
		steps = new ConcurrentLinkedDeque<List<Operation>>();
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
	}
	public void removeEntity(Entity b) {
		if (!entities.remove(b)) 
			Logger.log("Ignoring a request to remove an unknown entity");
	}

	public void emptyEntities() {
		entities = new ArrayList<Entity>();
	}

	public void setEntities(List<Entity> l) {
		entities = l;
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

	public String getAPI() {
		// TODO: Buffer and share APIs among instances
		String filename = getClass().getCanonicalName().replace('.', File.separatorChar);
		String api = "File "+filename+".html not found.";
		StringBuffer sb = null;
		try {
			sb = FileUtils.readContentAsText(filename, settings.getHumanLang(), "html", true);
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
				sb = FileUtils.readContentAsText(filename, settings.getHumanLang(), "html", true);
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

	public ConcurrentLinkedDeque<List<Operation>> getSteps() {
		return steps;
	}

	public void addStep(List<Operation> operations) {
		steps.add(operations);
	}

	@SuppressWarnings("unchecked")
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();

		JSONArray jsonEntities = new JSONArray();
		for(Entity entity : entities) {
			jsonEntities.add(entity.toJSON());
		}

		json.put("type", getJSONType());
		json.put("name", name);
		json.put("entities", jsonEntities);

		return json;
	}

	public UserSettings getSettings() {
		return settings;
	}

	public void setSettings(UserSettings settings) {
		this.settings = settings;
	}
	
	public Locale getLocale() {
		if(settings != null) {
			return settings.getHumanLang();
		}
		return Locale.getDefault();
	}
	
	public ProgrammingLanguage getProgLang() {
		return settings.getProgLang();
	}
}
