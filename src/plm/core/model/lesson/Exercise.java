package plm.core.model.lesson;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import plm.core.lang.ProgrammingLanguage;
import plm.core.model.ToJSON;
import plm.core.model.session.SourceFile;
import plm.core.ui.PlmHtmlEditorKit;
import plm.universe.World;

public abstract class Exercise implements ToJSON {
	public static enum WorldKind {INITIAL, CURRENT, ANSWER, ERROR}
	public static enum StudentOrCorrection {STUDENT, CORRECTION, ERROR}

	private String id;
	private String name;
	private int nbError;
	private UserSettings settings;

	private Map<String, Map<String, String>> tips = new HashMap<String, Map<String, String>>();
	protected String tabName = getClass().getSimpleName();/* Name of the tab in editor -- must be a valid java identifier */

	public String getBaseName() {
		return getClass().getCanonicalName();
	}

	public String nameOfCorrectionEntity() { // This will be redefined by TurtleArt to reduce the amount of code
		return getBaseName() + "Entity";
	}

	public String nameOfCommonError(int i) {
		return getBaseName() + "CommonErr" + i;
	}

	public String getTabName() {
		return tabName;
	}

	protected Map<ProgrammingLanguage, List<SourceFile>> sourceFiles= new HashMap<ProgrammingLanguage, List<SourceFile>>();
	private Map<ProgrammingLanguage, SourceFile> defaultSourceFiles = new HashMap<ProgrammingLanguage, SourceFile>();

	private Map<String, String> missions = new HashMap<String, String>();

	protected Vector<World> currentWorld; /* the one displayed */
	protected Vector<World> initialWorld; /* the one used to reset the previous on each run */
	protected Vector<World> answerWorld;  /* the one current should look like to pass the test */
	protected Vector<Vector<World>> commonErrors = new Vector<Vector<World>>();

	public Exercise(String id, String name) {
		this.setId(id);
		this.setName(name);
	}

	public Exercise(Exercise exo) {
		this(exo.getId(), exo.getName());

		int nbWorlds = exo.getWorldCount();
		initWorlds(nbWorlds);
		for(int i=0; i<nbWorlds; i++) {
			World baseInitialWorld = exo.getWorld(WorldKind.INITIAL, i);
			World baseCurrentWorld = exo.getWorld(WorldKind.CURRENT, i);
			World baseAnswerWorld = exo.getWorld(WorldKind.ANSWER, i);
			initialWorld.add(baseInitialWorld);
			currentWorld.add(baseCurrentWorld);
			answerWorld.add(baseAnswerWorld);
		}
		for(ProgrammingLanguage progLang : exo.getProgLanguages()) {
			SourceFile sourceFile = exo.getDefaultSourceFile(progLang).clone();
			addDefaultSourceFile(progLang, sourceFile);
		}
		for(String humanLang : exo.getHumanLanguages()) {
			String mission = exo.getDefaultMission(humanLang);
			addMission(humanLang, mission);
		}
	}

	public Exercise(JSONObject json) {
		this((String) json.get("id"), (String) json.get("name"));

		initialWorld = new Vector<World>();
		JSONArray jsonInitialWorlds = (JSONArray) json.get("initialWorlds");
		for(int i=0; i<jsonInitialWorlds.size(); i++) {
			JSONObject jsonWorld = (JSONObject) jsonInitialWorlds.get(i);
			String type = (String) jsonWorld.get("type");
			try {
				World w = (World) Class.forName(type).getDeclaredConstructor(JSONObject.class).newInstance(jsonWorld);
				initialWorld.add(w);
			} catch (InstantiationException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException
					| ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

		currentWorld = new Vector<World>();
		for(World w : initialWorld) {
			currentWorld.addElement(w.copy());
		}

		answerWorld = new Vector<World>();
		JSONArray jsonAnswerWorlds = (JSONArray) json.get("answerWorlds");
		for(int i=0; i<jsonAnswerWorlds.size(); i++) {
			JSONObject jsonWorld = (JSONObject) jsonAnswerWorlds.get(i);
			String type = (String) jsonWorld.get("type");
			try {
				World w = (World) Class.forName(type).getDeclaredConstructor(JSONObject.class).newInstance(jsonWorld);
				answerWorld.add(w);
			} catch (InstantiationException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException
					| ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

		JSONObject jsonSourceFiles = (JSONObject) json.get("defaultSourceFiles");
		for(Object key : jsonSourceFiles.keySet()) {
			String progLangName = (String) key;
			JSONObject jsonSourceFile = (JSONObject) jsonSourceFiles.get(progLangName);
			
			ProgrammingLanguage progLang = ProgrammingLanguage.getProgrammingLanguage(progLangName);
			SourceFile sourceFile = new SourceFile(jsonSourceFile);
			
			defaultSourceFiles.put(progLang, sourceFile);
		}
	}

	public void initWorlds(int size) {
		currentWorld = new Vector<World>(size);
		initialWorld = new Vector<World>(size);
		answerWorld  = new Vector<World>(size);
	}
	
	public void setupWorlds(World[] w, int size) {
		initWorlds(w.length);
		Vector<World> errorWorld = new Vector<World>(w.length);
		for (int i=0; i<w.length; i++) {
			if (w[i] == null) 
				throw new RuntimeException("Broken exercise "+getId()+": world "+i+" is null!");
			currentWorld.add( w[i].copy() );
			initialWorld.add( w[i].copy() );
			answerWorld.add( w[i].copy() );
		}
		for(int j = 0 ; j < size ; j++) { //size : nombre de fichiers d'erreur
			errorWorld = new Vector<World>(w.length);
			for (int i=0; i<w.length; i++) {
				if (w[i] == null) 
					throw new RuntimeException("Broken exercise "+getId()+": world "+i+" is null!");
				errorWorld.add(w[i].copy());
			}
			commonErrors.add(errorWorld);
		}
	}

	/** Reset the current worlds to the state of the initial worlds */
	public void reset() {
		//lastResult = new ExecutionProgress(getGame().getProgrammingLanguage());

		for (int i=0; i<initialWorld.size(); i++) 
			currentWorld.get(i).reset(initialWorld.get(i));
	}

	/** get the list of source files for a given language, or create it if not existent yet */
	public List<SourceFile> getSourceFilesList(ProgrammingLanguage lang) {
		List<SourceFile> res = sourceFiles.get(lang);
		if (res == null) {
			res = new ArrayList<SourceFile>();
			sourceFiles.put(lang, res);
		}
		return res;
	}
	public int getSourceFileCount(ProgrammingLanguage lang) {
		return getSourceFilesList(lang).size();
	}	
	public SourceFile getSourceFile(ProgrammingLanguage lang, int i) {
		if(i<getSourceFileCount(lang)) {
			return getSourceFilesList(lang).get(i);
		}
		return null;
	}

	public Vector<World> getWorlds(WorldKind kind) {
		switch (kind) {
		case INITIAL: return initialWorld;
		case CURRENT: return currentWorld;
		case ANSWER:  return answerWorld;
		case ERROR:   if(nbError != -1) return commonErrors.get(nbError);
		default: throw new RuntimeException("Unhandled kind of world: "+kind);
		}
	}

	public int getWorldCount() {
		return this.initialWorld.size();
	}

	/** Returns the current world number index 
	 * @see #getAnswerOfWorld(int)
	 */
	public World getWorld(int index) {// FIXME: rename to getCurrentWorld or KILLME
		return this.currentWorld.get(index);
	}

	public World getWorld(WorldKind worldKind, int index) {
		return getWorlds(worldKind).get(index);
	}

	public int indexOfWorld(World w) {
		int index = 0;
		do {
			if (this.currentWorld.get(index) == w)
				return index;
			index++;
		} while (index < this.currentWorld.size());

		throw new RuntimeException("World not found (please report this bug)");
	}

	public World getAnswerOfWorld(int index) { // FIXME: rename or KILLME
		return this.answerWorld.get(index);
	}

	public String toString() {
		return getName();
	}

	public int getNbError() {
		return nbError;
	}

	public void setNbError(int nbError) {
		this.nbError = nbError;
	}

	public Set<ProgrammingLanguage> getProgLanguages() {
		return defaultSourceFiles.keySet();
	}

	public boolean isProgLangSupported(ProgrammingLanguage progLang) {
		return defaultSourceFiles.containsKey(progLang);
	}

	public void addDefaultSourceFile(ProgrammingLanguage progLang, SourceFile sourceFile) {
		defaultSourceFiles.put(progLang, sourceFile);
	}

	public SourceFile getDefaultSourceFile(ProgrammingLanguage progLang) {
		if(isProgLangSupported(progLang)) {
			return defaultSourceFiles.get(progLang).clone();
		}
		return null;
	}

	public Set<String> getHumanLanguages() {
		return missions.keySet();
	}

	public void addMission(String humanLang, String mission) {
		missions.put(humanLang, mission);
	}

	private String getDefaultMission(String humanLang) {
		return missions.get(humanLang);
	}

	public String getMission() {
		return getMission(settings.getHumanLang(), settings.getProgLang());
	}

	public String getMission(Locale locale, ProgrammingLanguage progLang) {
		String humanLang = locale.getLanguage();
		String mission = missions.get("en");
		if(missions.containsKey(humanLang)) {
			mission = missions.get(humanLang);
		}
		return PlmHtmlEditorKit.filterHTML(mission, false, progLang);
	}

	public String getWorldAPI() {
		if(initialWorld.size() == 0) {
			return "World is missing...";
		}
		return initialWorld.get(0).getAPI();
	}

	@SuppressWarnings("unchecked")
	public JSONObject toJSON() {
		JSONObject json =  new JSONObject();

		String entityType = "";

		JSONArray jsonInitialWorlds = new JSONArray();
		for(World world : initialWorld) {
			JSONObject jsonInitialWorld = world.toJSON();
			if(entityType.equals("")) {
				JSONArray entities = (JSONArray) jsonInitialWorld.get("entities");
				JSONObject entity = (JSONObject) entities.get(0);
				entityType = (String) entity.get("type");
			}
			jsonInitialWorlds.add(jsonInitialWorld);
		}

		JSONArray jsonAnswerWorlds = new JSONArray();
		for(World world : answerWorld) {
			JSONObject jsonAnswerWorld = world.toJSON();

			// Need to fix the type of the entities
			JSONArray entities = (JSONArray) jsonAnswerWorld.get("entities");
			for(int i=0; i<entities.size(); i++) {
				JSONObject entity = (JSONObject) entities.get(i);
				entity.put("type", entityType);
			}
			jsonAnswerWorlds.add(jsonAnswerWorld);
		}

		JSONObject jsonSourceFiles = new JSONObject();
		for(ProgrammingLanguage progLang : defaultSourceFiles.keySet()) {
			SourceFile sourceFile = defaultSourceFiles.get(progLang);
			jsonSourceFiles.put(progLang.getLang(), sourceFile.toJSON());
		}

		json.put("id", getId());
		json.put("name", getName());
		json.put("initialWorlds", jsonInitialWorlds);
		json.put("answerWorlds", jsonAnswerWorlds);
		json.put("defaultSourceFiles", jsonSourceFiles);
		return json;
	}

	public UserSettings getSettings() {
		return settings;
	}

	public void setSettings(UserSettings settings) {
		this.settings = settings;
		for(World w : getWorlds(WorldKind.INITIAL)) {
			w.setSettings(settings);
		}
		for(World w : getWorlds(WorldKind.ANSWER)) {
			w.setSettings(settings);
		}
		for(World w : getWorlds(WorldKind.CURRENT)) {
			w.setSettings(settings);
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addTips(String humanLang, String id, String content) {
		Map<String, String> localizedTips;
		if(tips.containsKey(humanLang)) {
			localizedTips = tips.get(humanLang);
		}
		else {
			localizedTips = new HashMap<String, String>();
			tips.put(humanLang, localizedTips);
		}
		localizedTips.put(id, content);
	}
}