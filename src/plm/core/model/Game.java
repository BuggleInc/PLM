package plm.core.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.swing.JOptionPane;

import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import plm.core.GameListener;
import plm.core.GameStateListener;
import plm.core.HumanLangChangesListener;
import plm.core.ProgLangChangesListener;
import plm.core.StatusStateListener;
import plm.core.lang.LangC;
import plm.core.lang.LangJava;
import plm.core.lang.LangLightbot;
import plm.core.lang.LangPython;
import plm.core.lang.LangRuby;
import plm.core.lang.LangScala;
import plm.core.lang.LangBlockly;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.lesson.ExecutionProgress;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Exercise.WorldKind;
import plm.core.model.lesson.Lecture;
import plm.core.model.lesson.Lesson;
import plm.core.model.lesson.Lesson.LoadingOutcome;
import plm.core.model.session.SessionDB;
import plm.core.model.session.SourceFile;
import plm.core.model.session.SourceFileRevertable;
import plm.core.model.tracking.HeartBeatSpy;
import plm.core.model.tracking.LocalFileSpy;
import plm.core.model.tracking.ProgressSpyListener;
import plm.core.model.tracking.ServerSpyAppEngine;
import plm.universe.Entity;
import plm.universe.IWorldView;
import plm.universe.World;

/*
 *  core model which contains all known exercises.
 */
public class Game implements IWorldView {
	/** Current state of the engine: whether we are running the student code, a demo, saving to files, or whatever. 
	 *  Helps deciding which interface buttons are enabled/disabled for example.
	 */
	public enum GameState {
		IDLE, 
		SAVING, SAVING_DONE,
		LOADING, LOADING_DONE,
		COMPILATION_STARTED, COMPILATION_ENDED, 
		EXECUTION_STARTED, EXECUTION_ENDED,
		DEMO_STARTED, DEMO_ENDED,
	}

	private GameState state = GameState.IDLE;

	private final static String LOCAL_PROPERTIES_FILENAME = "plm.properties";

	private static Properties defaultGameProperties = new Properties();
	private static Properties localGameProperties = new Properties();
	private static File localGamePropertiesLoadedFile;

	private Properties localProperties = new Properties();
	
	private Map<String, Lesson> lessons = new HashMap<String, Lesson>();
	private Map<String, Lesson> loadedLessons = new HashMap<String, Lesson>();
	private Lesson currentLesson;
	private Course currentCourse;
	private Lecture lastExercise;

	public static final String [][] humanLangs = { {"English","en"}, {"Français","fr"}, {"Italiano","it"}, {"Português brasileiro", "pt_BR"}, {"中文", "zh"} };
	public static final String [] lessonsName = new String[] { // WARNING, keep ChooseLessonDialog.lessons synchronized
		"lessons.welcome", "lessons.turmites", "lessons.maze", "lessons.turtleart",
		"lessons.sort.basic", "lessons.sort.dutchflag", "lessons.sort.baseball", "lessons.sort.pancake", 
		"lessons.recursion.cons", "lessons.recursion.lego", "lessons.recursion.hanoi",
		"lessons.lightbot", "lessons.bat.string1", "lessons.lander"
	};
	public static final ProgrammingLanguage JAVA =       new LangJava(false);
	public static final ProgrammingLanguage PYTHON =     new LangPython(false);
	public static final ProgrammingLanguage SCALA =      new LangScala(false);
	public static final ProgrammingLanguage C =          new LangC(false);
	//public static final ProgrammingLanguage JAVASCRIPT = new ProgrammingLanguage("JavaScript","js",ResourcesCache.getIcon("img/lang_javascript.png"));
	public static final ProgrammingLanguage RUBY =       new LangRuby(false);
	public static final ProgrammingLanguage LIGHTBOT =   new LangLightbot(false);
	public static final ProgrammingLanguage BLOCKLY =      new LangBlockly(false);

	public static final ProgrammingLanguage[] programmingLanguages = new ProgrammingLanguage[] {
		JAVA, PYTHON, SCALA, RUBY, LIGHTBOT, BLOCKLY // TODO: re-add C & JAVASCRIPT to this list once they work at least a bit
	};
	private ProgrammingLanguage programmingLanguage = JAVA;
	private boolean canScala = false;
	private boolean canPython = false;
	private boolean canBlockly = false;
	private boolean canC = false;
	
	/* TODO: document these values elsewhere */
	public static final String PROP_OUTPUT_CAPTURE = "output.capture"; // Whether to redirect stdout and stderr to the graphical console. Defaults to true
	public static final String PROP_ANSWER_CACHE = "answers.cache"; // Whether to use the cache of answers worlds on disk, defaults to true. 
	// Turning to false will slow down the startup process, but avoid out of date files

	public static final String PROP_PROGRESS_APPENGINE = "plm.progress.appengine"; // Whether the progresses should be posted to the appengine (default: false)
	public static final String PROP_APPENGINE_URL = "plm.appengine.url"; // Where to find the appengine. This is related to the teacher console, that should be rewritten at some point.

	public static final String PROP_PROGRAMING_LANGUAGE = "plm.programingLanguage";

	public static final String PROP_FONT_SIZE = "plm.display.fontsize"; // the CSS property of the font size

	private List<GameListener> listeners = new ArrayList<GameListener>();
	private World selectedWorld;
	private World answerOfSelectedWorld;
	private World initialOfSelectedWorld;
	private Entity selectedEntity;
	private List<Thread> demoRunners = new ArrayList<Thread>();
	private static List<Thread> initRunners = new ArrayList<Thread>();

	private HeartBeatSpy heartBeatSpy;

	private ArrayList<GameStateListener> gameStateListeners = new ArrayList<GameStateListener>();

	public SessionDB studentWork;

	public LogHandler logger;

	private static boolean ongoingInitialization = false;
	
	private boolean trackUser;
	
	private Locale locale;
	public I18n i18n;
	public Game(String userUUID, LogHandler logger, Locale locale, String defaultProgrammingLanguage, boolean trackUser) {
		this(userUUID, logger, locale, defaultProgrammingLanguage, trackUser, new Properties());
	}
	
	public Game(String userUUID, LogHandler logger, Locale locale, String defaultProgrammingLanguage, boolean trackUser, Properties localProperties) {
		this.localProperties = localProperties;
		this.logger = logger;
		this.locale = locale;
		this.trackUser = trackUser;
		
		i18n = I18nFactory.getI18n(getClass(),"org.plm.i18n.Messages", locale, I18nFactory.FALLBACK);
		loadProperties();

		canScala = true;
		canPython = true;
		canBlockly = true;
		
		if (!defaultProgrammingLanguage.equalsIgnoreCase(Game.JAVA.getLang()) &&
				!defaultProgrammingLanguage.equalsIgnoreCase(Game.PYTHON.getLang()) &&
				!defaultProgrammingLanguage.equalsIgnoreCase(Game.SCALA.getLang()) && 
				!defaultProgrammingLanguage.equalsIgnoreCase(Game.C.getLang()) && 
				!defaultProgrammingLanguage.equalsIgnoreCase(Game.BLOCKLY.getLang())) 
			System.err.println(i18n.tr("Warning, the default programming language is neither ''Java'' nor ''python'' or ''Scala'' or ''C'' or ''Blockly'' but {0}.\n"+
					"   This language will be used to setup the worlds, possibly leading to severe issues for the exercises that don''t expect it.\n" +
					"   It is safer to change the current language, and restart the PLM before proceeding.\n"+
					"   Alternatively, the property {1} can be changed in your configuration file ({2}/plm.properties)",defaultProgrammingLanguage,PROP_PROGRAMING_LANGUAGE, getSavingLocation()));

		if (defaultProgrammingLanguage.equalsIgnoreCase(Game.SCALA.getLang()) && !canScala) {
			System.err.println(i18n.tr("The default programming language is Scala, but your scala installation is not usable. Switching to Java instead.\n"));
			setProgramingLanguage(JAVA);
		} else if (defaultProgrammingLanguage.equalsIgnoreCase(Game.PYTHON.getLang()) && !canPython) {
			System.err.println(i18n.tr("The default programming language is python, but your python installation is not usable. Switching to Java instead.\n"));
			setProgramingLanguage(JAVA);
		} else if (defaultProgrammingLanguage.equalsIgnoreCase(Game.C.getLang()) && !canC) {
			System.err.println(i18n.tr("The default programming language is C, but your C installation is not usable. Switching to Java instead.\n"));
			setProgramingLanguage(JAVA);
		} else if (defaultProgrammingLanguage.equalsIgnoreCase(Game.BLOCKLY.getLang()) && !canBlockly) {
			System.err.println(i18n.tr("The default programming language is Blockly, but your Blockly installation is not usable. Switching to Java instead.\n"));
			setProgramingLanguage(JAVA);
		} else {
			for (ProgrammingLanguage pl : Game.getProgrammingLanguages()) 
				if (pl.getLang().equals(defaultProgrammingLanguage)) {
					setProgramingLanguage(pl);
					break;
				}
		}

		studentWork = new SessionDB(this);

		addProgressSpyListener(new LocalFileSpy(this, SAVE_DIR));

		initLessons();

		if (getProperty(PROP_PROGRESS_APPENGINE, "false",true).equalsIgnoreCase("true"))
			addProgressSpyListener(new ServerSpyAppEngine(this));
		if (! Game.getProperty(Game.PROP_APPENGINE_URL).equals("")) { // FIXME: there is no way real proper way to disable the CourseEngine !!!
			currentCourse = new CourseAppEngine(logger);
		}

		loadSession();
	}

	private void initLessons() {
		for(String lessonName: lessonsName) {
			addLesson(lessonName);			
		}
	}

	public void addLesson(String lessonName) {
		Lesson lesson = null;
		try {
			// This is where we assume here that each lesson contains a Main object, instantiating the Lesson class.
			// We manually build a call to the constructor of this object, and fire it
			// This creates such an object, which is in charge of creating the whole lesson (including exercises) from its constructor
			try {
				lesson = (Lesson) Class.forName(lessonName + ".Main").getDeclaredConstructor(Game.class).newInstance(this);
			} catch (IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
			addHumanLangListener(lesson);
			lessons.put(lessonName, lesson); // cache the newly created object
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(i18n.tr("Cannot load lesson {0}: class Main not found.",lessonName));
		}
	}

	/** Change the current lesson.
	 * 
	 * Also, initialize the newly used lesson on need. It must already be in the classpath 
	 * (use @loadLesson() if you want to load a lesson located in an external jar file)
	 *  
	 * @param lessonName package name of the lesson to load 
	 */

	public Lesson switchLesson(String lessonName, boolean failOnError) {
		if(state==GameState.EXECUTION_STARTED || state == GameState.DEMO_STARTED) {
			stopExerciseExecution();
		}
		this.setState(GameState.LOADING);
		// Try caching the lesson to avoid the possibly long loading time during which we compute the solution of each exercise  
		Lesson lesson = lessons.get(lessonName);
		Lesson lessonLoaded = loadedLessons.get(lessonName);
		statusArgAdd(lessonName);
		if (lessonLoaded == null) { // we have to load it
			lesson.loadLesson();
			loadedLessons.put(lessonName, lesson);
		}
		// Prevent obvious error messages
		try {
			waitInitThreads();
		} catch (InterruptedException e) {
			System.err.println("Interrupted while loading the lesson "+lesson.getName());
			e.printStackTrace();
		}
		// If a problem arose while setting up the lesson, don't switch 
		if(lesson.getLoadingOutcomeState() == LoadingOutcome.FAIL) {
			JOptionPane.showMessageDialog(null, 
					i18n.tr("The lesson {0} encountered an issue while loading its exercises, please report the issue and choose another lesson.",lesson.getName()) ,
					i18n.tr("Broken lesson"), JOptionPane.ERROR_MESSAGE); 
			return null;
		}

		setCurrentLesson(lesson);
		this.setState(GameState.LOADING_DONE);
		return lesson;
	}

	public void switchExercise(String exerciseID) {
		this.selectedWorld = null; // Remove the previous selectedWorld
		this.currentLesson.setCurrentExercise(exerciseID);
	}

	private Set<String> usedJARs = new HashSet<String>(); // cache used in loadLessonFromJAR()
	/** Load a new lesson from an external JAR file.
	 *  
	 * This will only work if the system classloader is an URLClassLoader. 
	 * If your JVM gave you something else (and if you failed to change it from the command line or whatever), this will fail with an exception. 
	 * 
	 * @param path Path to the JAR file
	 * @throws LessonLoadingException if the JAR file is inexistent or invalid (or if the system classloader does not accept URLs)
	 */
	public void loadLessonFromJAR(File jar) throws LessonLoadingException {

		if (!jar.exists())
			throw new LessonLoadingException("File "+jar.getName()+" does not exist");


		// Check if the JAR has already been added. If not, load it in the classloader.
		if (!usedJARs.contains(jar.getAbsolutePath())) {	
			URLClassLoader sysloader = (URLClassLoader)ClassLoader.getSystemClassLoader();
			Class<URLClassLoader> sysclass = URLClassLoader.class;

			URL urlOfJar;
			try {
				urlOfJar = jar.toURI().toURL();
			} catch (IOException e1) {
				throw new LessonLoadingException("Error while reading the jarfile "+jar.getName(),e1);
			}

			try {
				// Hell yeah. That method is usually private, but I can change it that easily.
				// Some people even call this bullshit "security"...

				Method method = sysclass.getDeclaredMethod("addURL",new Class[]{URL.class});
				method.setAccessible(true);
				method.invoke(sysloader,new Object[]{ urlOfJar });
			} catch (Throwable t) {
				throw new LessonLoadingException("Internal Error: The system classloader refused to load the URL of this lesson file. You may want to change the JVM classloader from the command line.",t);
			}

			usedJARs.add(jar.getAbsolutePath());
		}

		// Load the JAR manifest file to retrieve the lesson's package name in it
		Manifest manifest;
		try {
			manifest = new JarFile(jar).getManifest();
		} catch (Exception e) {
			throw new LessonLoadingException("Invalid lesson file (Manifest not found): "+jar.getName(), e);
		}

		String lessonPackage = manifest.getMainAttributes().getValue("LessonPackage");
		if (lessonPackage == null)
			throw new LessonLoadingException("Invalid lesson file (Attribute 'LessonPackage' not found in Manifest): "+jar.getName());

		// We are ready to launch this lesson
		switchLesson("lessons." + lessonPackage,false);
	}//end method



	public static void addInitThread(Thread t) {
		initRunners.add(t);
	}
	public static void waitInitThreads() throws InterruptedException {
		for (Thread t:initRunners) 
			t.join();
	}

	public Collection<Lesson> getLessons() {
		return this.lessons.values();
	}

	public Collection<Lesson> getLoadedLessons() {
		return this.loadedLessons.values();
	}

	public Lesson getCurrentLesson() {
		if (this.currentLesson == null && this.loadedLessons.size() > 0) {
			setCurrentLesson(loadedLessons.get(lessonsName[0]));
		}
		return this.currentLesson;
	}

	public void setCurrentLesson(Lesson lesson) {
		// Don't set the currentLesson from here, or the lastExercise saving mechanism will not work
		setCurrentExercise(lesson.getCurrentExercise());
	}

	public Lecture getLastExercise() {
		return this.lastExercise;
	}

	// only to avoid that exercise views register as listener of a lesson
	public void setCurrentExercise(Lecture lect) {
		// No need to stop the execution if no lesson is currently selected
		if(currentLesson != null) {
			// If already executing a program, stop it
			if(state==GameState.EXECUTION_STARTED || state == GameState.DEMO_STARTED) {
				stopExerciseExecution();
			}
			removeHumanLangListener(currentLesson.getCurrentExercise());
			removeHumanLangListener(currentLesson);
		}
		try {
			saveSession(); // don't loose user changes
			this.lastExercise = (currentLesson==null ? null : currentLesson.getCurrentExercise()); // save the last viewed exercise before switching7

			if (this.currentLesson != lect.getLesson()) {
				this.currentLesson = lect.getLesson();
				addHumanLangListener(currentLesson);
			}

			/* if the user changes the exercise, you can assume that he wants to test another challenge */
			if (isCreativeEnabled())
				switchCreative();

			this.currentLesson.setCurrentExercise(lect);
			fireCurrentExerciseChanged(lect);
			if (lect instanceof Exercise) {
				Exercise exo = (Exercise) lect;
				exo.reset();
				setSelectedWorld(exo.getWorld(0));

				ProgrammingLanguage fallback = null;
				for (ProgrammingLanguage l:exo.getProgLanguages()) {
					if (l.equals(programmingLanguage))
						return; /* The exo accepts the language we currently have */
					if (fallback == null)
						fallback = l;
				}
				/* Use the first (programming) language advertised by the exercise java as a fallback */
				if (getProgrammingLanguage() != Game.LIGHTBOT && fallback != Game.LIGHTBOT)
					getLogger().log(
							i18n.tr("Exercise {0} does not support language {1}. Fallback to {2} instead. "
									+ "Please consider contributing to this project by adapting this exercise to this language.",
									lect.getName(),getProgrammingLanguage(),fallback.getLang()));
				setProgramingLanguage(fallback);

			}
		} catch (UserAbortException e) { 
			getLogger().log(i18n.tr("Operation cancelled by the user"));
		}
	}

	public World getSelectedWorld() {
		if (this.currentLesson == null)
			return null;
		Lecture lecture = this.currentLesson.getCurrentExercise();
		if (lecture instanceof Exercise) {
			if (this.selectedWorld == null) {
				setSelectedWorld(((Exercise) lecture).getWorld(0));
			}
			return this.selectedWorld;
		} else {
			return null;
		}
	}
	public World[] getSelectedWorlds() {
		World[] res = new World[3];
		res[0] = selectedWorld;
		res[1] = answerOfSelectedWorld;
		res[2] = initialOfSelectedWorld;
		return res;
	}

	public void setSelectedWorld(World world) {
		Lecture lect = getCurrentLesson().getCurrentExercise();
		if (lect instanceof Exercise) {
			Exercise exo = (Exercise) lect;
			if (this.selectedWorld != null)
				this.selectedWorld.removeEntityUpdateListener(this);
			this.selectedWorld = world;
			this.selectedWorld.addEntityUpdateListener(this);

			int index = exo.indexOfWorld(this.selectedWorld);
			this.answerOfSelectedWorld = exo.getAnswerOfWorld(index);
			exo.setNbError(-1);
			this.initialOfSelectedWorld = exo.getWorlds(WorldKind.INITIAL).get(index);
			if (this.selectedWorld.getEntityCount()>0) {
				this.selectedEntity = this.selectedWorld.getEntity(0);
			}
			fireSelectedWorldHasChanged(world);
		} else {
			throw new RuntimeException(i18n.tr("The lecture {0} has no world that I can select",lect));
		}
	}

	public World getAnswerOfSelectedWorld() {
		return this.answerOfSelectedWorld;
	}

	public void setSelectedEntity(Entity b) {
		this.selectedEntity = b;
		fireSelectedEntityHasChanged();
	}

	public Entity getSelectedEntity() {
		return this.selectedEntity;
	}

	/* Actions of the toolbar buttons */
	private boolean stepMode = false;
	private LessonRunner runner;
	public void startExerciseExecution() {
		runner = new LessonRunner(this);
		runner.start();
	}
	public void stopExerciseExecution() {
		if (stepModeEnabled()) 
			disableStepMode();

		// Only forcefully stop the threads if they run the user code (not the correction)
		if(state == GameState.EXECUTION_STARTED) {
			runner.stopAll();
		}			

		// "Stop" the demo threads too, but asking them to not wait for the UI
		// We cannot kill them as they are computing the exercise's correction.
		Lecture lecture = this.currentLesson.getCurrentExercise();
		if (lecture instanceof Exercise)
			((Exercise) lecture).setNbError(-1);
			for (World w : ((Exercise) lecture).getWorlds(WorldKind.ANSWER))
				w.doneDelay();
	}
	public void startExerciseDemoExecution() {
		DemoRunner runner = new DemoRunner(this, this.demoRunners);
		runner.start();
	}

	public void startExerciseStepExecution() {
		stepMode = true;
		startExerciseExecution();
	}

	public void enableStepMode() {
		this.stepMode = true;
	}
	public void disableStepMode() {
		this.stepMode = false;
	}

	public boolean stepModeEnabled() {
		return this.stepMode;
	}

	public void allowOneStep() {
		Lecture lecture = this.currentLesson.getCurrentExercise();
		if (lecture instanceof Exercise)
			((Exercise) lecture).setNbError(-1);
			for (World w: ((Exercise) lecture).getWorlds(WorldKind.CURRENT))
				for (Entity e : w.getEntities())
					e.allowOneStep();
	}

	/**  Reset the current exercise (see {@link Exercise.reset()} */
	public void reset() {
		Lecture lecture = this.currentLesson.getCurrentExercise();
		if (lecture instanceof Exercise) {
			((Exercise) lecture).reset();
			fireCurrentExerciseChanged(lecture);
		}
	}

	public void setState(GameState status) {
		this.state = status;
		fireStateChanged(status);
	}

	public GameState getState() {
		return this.state;
	}

	public void quit() {
		try {
			// FIXME: this method is not called when pressing APPLE+Q on OSX
			
			// Should kill all threads before quitting this instance
			if(state==GameState.EXECUTION_STARTED || state == GameState.DEMO_STARTED) {
				stopExerciseExecution();
			}
			
			saveSession();

			// report user leave on the server
			for(ProgressSpyListener spyListener: progressSpyListeners){
				spyListener.leave();
			}
			// stop the heartbeat report to PLMServer
			if(heartBeatSpy != null)
				heartBeatSpy.die();

			storeProperties();
		} catch (UserAbortException e) {
			// Ok, user decided to not quit (to get a chance to export the session)
			getLogger().log("Exit aborted");
		}
	}

	public void clearSession() {
		for (Lesson l : this.lessons.values())
			for (Lecture lect : l.exercises())
				if (lect instanceof Exercise)
					for (ProgrammingLanguage lang:((Exercise) lect).getProgLanguages()) 
						studentWork.setPassed(lect, lang, false);

		fireCurrentExerciseChanged(currentLesson.getCurrentExercise());
	}

	public void loadSession() {
		this.setState(GameState.LOADING);
		this.setState(GameState.LOADING_DONE);
	}

	public void saveSession() throws UserAbortException {
		this.setState(GameState.SAVING);
		this.setState(GameState.SAVING_DONE);
		storeProperties();
	}
	
	public void removeSessionKit() {
		getLogger().log("Disabling the session kit on disk.");
	}

	// FIXME: Should not be static
	public static void loadProperties() {
		InputStream is = null;
		try {
			is = Game.class.getClassLoader().getResourceAsStream("resources/plm.configuration.properties");
			if (is==null) // try to find the file in the Debian package
				is = Game.class.getClassLoader().getResourceAsStream("/etc/plm.configuration.properties");
			Game.defaultGameProperties.load(is);
		} catch (InvalidPropertiesFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			// resources/plm.configuration.properties not found. Try plm.configuration.properties afterward
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

		File localPropertiesFile = new File(SAVE_DIR + File.separator + Game.LOCAL_PROPERTIES_FILENAME);
		if (localPropertiesFile.exists()) {
			FileInputStream fi = null;
			try {
				fi = new FileInputStream(localPropertiesFile);
				Game.localGameProperties.load(fi);
				Game.localGamePropertiesLoadedFile = localPropertiesFile;
			} catch (InvalidPropertiesFormatException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (fi != null)
					try {
						fi.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
			//getLogger().log(String.format("Loading properties [%s]", localPropertiesFile));
		}
	}

	public static void storeProperties() {
		Game.localGamePropertiesLoadedFile = new File(SAVE_DIR + File.separator + Game.LOCAL_PROPERTIES_FILENAME);
		FileOutputStream fo;
		try {
			fo = new FileOutputStream(Game.localGamePropertiesLoadedFile);
			Game.localGameProperties.store(fo, "Java Learning Machine properties");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void setProperty(String key, String value) {
		Game.localGameProperties.setProperty(key, value);
	}

	public static String getProperty(String key) {
		return Game.getProperty(key, "", false);
	}

	/** 
	 * Gets the value from either the local properties set (in ~/.plm) or the global one (in the jar file).
	 * If the value is not defined in either of them, use the default value. If so and if the save parameter is true, this is saved back to the local properties file. 
	 */
	public static String getProperty(String key, String defaultValue, boolean save) {
		if (Game.localGameProperties.containsKey(key)) {
			return Game.localGameProperties.getProperty(key);
		} else {
			String res = Game.defaultGameProperties.getProperty(key, defaultValue);
			if (save)
				setProperty(key, res);
			return res;
		}
	}

	public void addGameListener(GameListener l) {
		this.listeners.add(l);
	}

	public void removeGameListener(GameListener l) {
		this.listeners.remove(l);
	}

	protected void fireCurrentExerciseChanged(Lecture lect) {
		if (stepModeEnabled())
			disableStepMode();

		for (GameListener v : this.listeners) {
			v.currentExerciseHasChanged(lect);
		}

		if(lect instanceof Exercise){
			for(ProgressSpyListener p: this.progressSpyListeners){
				p.switched((Exercise)lect);
			}
		}
	}

	protected void fireSelectedWorldHasChanged(World w) {
		for (GameListener v : this.listeners) {
			v.selectedWorldHasChanged(w);
		}
	}

	protected void fireSelectedWorldWasUpdated() {
		for (GameListener v : this.listeners) {
			v.selectedWorldWasUpdated();
		}
	}

	protected void fireSelectedEntityHasChanged() {
		for (GameListener v : this.listeners) {
			v.selectedEntityHasChanged();
		}
	}

	public void addGameStateListener(GameStateListener l) {
		this.gameStateListeners.add(l);
	}

	public void removeGameStateListener(GameStateListener l) {
		this.gameStateListeners.remove(l);
	}

	protected void fireStateChanged(GameState status) {
		for (GameStateListener l : this.gameStateListeners) 
			l.stateChanged(status);
	}

	private ArrayList<ProgressSpyListener> progressSpyListeners = new ArrayList<ProgressSpyListener>();
	public void addProgressSpyListener(ProgressSpyListener l) {
		this.progressSpyListeners.add(l);
	}
	public void removeProgressSpyListener(ProgressSpyListener l) {
		this.progressSpyListeners.remove(l);
	}
	public void fireProgressSpy(Exercise exo) {
		for (ProgressSpyListener l : this.progressSpyListeners) {
			l.executed(exo);
		}
	}

	public void fireCallForHelpSpy(String studentInput) {
		for (ProgressSpyListener l : this.progressSpyListeners) {
			l.callForHelp(studentInput);
		}
	}

	public void fireCancelCallForHelpSpy() {
		for (ProgressSpyListener l : this.progressSpyListeners) {
			l.cancelCallForHelp();
		}
	}

	public void fireReadTipSpy(String id, String mission) {
		for (ProgressSpyListener l : this.progressSpyListeners) {
			l.readTip(id, mission);
		}
	}

	@Override
	public void worldHasChanged() {
		if (selectedWorld.getEntityCount()>0)
			setSelectedEntity(this.selectedWorld.getEntity(0));
		fireSelectedWorldWasUpdated();
	}

	@Override
	public void worldHasMoved() {
		// don't really care that something moved within the current world
	}

	/* Status bar label changing logic */
	ArrayList<StatusStateListener> statusStateListeners = new ArrayList<StatusStateListener>();
	public void addStatusStateListener(StatusStateListener l) {
		this.statusStateListeners.add(l);
	}
	public void removeStatusStateListener(StatusStateListener l) {
		this.statusStateListeners.remove(l);
	}
	ArrayList<String> statusArgs = new ArrayList<String>();
	String stateTxt = "";
	public void statusRootSet(String txt) {
		stateTxt = txt;
	}
	public void statusArgAdd(String txt) {
		synchronized (statusArgs) {
			statusArgs.add(txt);
			statusChanged();
		}
	}
	public void statusArgRemove(String txt) {
		synchronized (statusArgs) {
			statusArgs.remove(txt);
			statusChanged();
		}
	}
	public void statusArgEmpty(){
		synchronized (statusArgs) {
			statusArgs.clear();
			statusChanged();
		}
	}
	private void statusChanged() {
		StringBuffer sb = new StringBuffer(stateTxt);
		boolean first = true;
		for (String s:statusArgs) {
			if (first)
				first = false;
			else
				sb.append(", ");
			sb.append(s);
		}

		String msg = first ? "" : sb.toString(); // remove everything if no argument at all 
		for (StatusStateListener l : this.statusStateListeners) 
			l.stateChanged(msg);
	}
	public void setLocale(Locale lang) {
		this.locale = lang;
		i18n = I18nFactory.getI18n(getClass(),"org.plm.i18n.Messages", lang, I18nFactory.FALLBACK);
		fireHumanLangChange(lang);
	}

	public Locale getLocale(){
		return locale;
	}

	public void setProgrammingLanguage(String newLanguage) {
		for (ProgrammingLanguage pl : Game.getProgrammingLanguages()) {
			if (pl.getLang().equalsIgnoreCase(newLanguage)) {
				setProgramingLanguage(pl);
				break;
			}
		}
	}

	public void setProgramingLanguage(ProgrammingLanguage newLanguage) {
		if (programmingLanguage.equals(newLanguage))
			return;

		if (isValidProgLanguage(newLanguage)) {
			this.programmingLanguage = newLanguage;
			if(getCurrentLesson() != null)
				((Exercise)getCurrentLesson().getCurrentExercise()).lastResult = new ExecutionProgress(newLanguage);
			fireProgLangChange(newLanguage);
			if (newLanguage.equals(Game.JAVA) || newLanguage.equals(Game.PYTHON) || newLanguage.equals(Game.SCALA) || newLanguage.equals(Game.C) || newLanguage.equals(Game.BLOCKLY)) // Only save it if it's stable enough
				setProperty(PROP_PROGRAMING_LANGUAGE, newLanguage.getLang());
			return;
		}
		throw new RuntimeException("Ignoring request to switch the programming language to the unknown "+newLanguage);
	}

	public ProgrammingLanguage getProgrammingLanguage() {
		if (ongoingInitialization) /* break an initialization loop -- the crude way (FIXME) */
			return JAVA;
		else
			return programmingLanguage;
	}
	public static ProgrammingLanguage[] getProgrammingLanguages(){
		return programmingLanguages;
	}

	public boolean isValidProgLanguage(ProgrammingLanguage newL) {
		for (ProgrammingLanguage pl : programmingLanguages)
			if (pl.equals(newL))
				return true;
		return false;
	}
	private List<ProgLangChangesListener> progLangListeners = new Vector<ProgLangChangesListener>();
	public void addProgLangListener(ProgLangChangesListener l) {
		progLangListeners.add(0, l);
	}
	public void addProgLangListener(ProgLangChangesListener l, boolean last) {
		if(last) {
			progLangListeners.add(l);
		}
		else {
			addProgLangListener(l);
		}
	}
	public void fireProgLangChange(ProgrammingLanguage newLang) {
		for (ProgLangChangesListener l : progLangListeners)
			l.currentProgrammingLanguageHasChanged(newLang);
	}
	public void removeProgLangListener(ProgLangChangesListener l) {
		this.progLangListeners.remove(l);
	}

	private List<HumanLangChangesListener> humanLangListeners = new Vector<HumanLangChangesListener>();
	public void addHumanLangListener(HumanLangChangesListener l) {
		humanLangListeners.add(0, l);
	}
	public void addHumanLangListener(HumanLangChangesListener l, boolean last) {
		if(last) {
			humanLangListeners.add(l);
		}
		else {
			addHumanLangListener(l);
		}
	}
	public void fireHumanLangChange(Locale newLang) {
		for (HumanLangChangesListener l : humanLangListeners)
			l.currentHumanLanguageHasChanged(newLang);
	}
	public void removeHumanLangListener(HumanLangChangesListener l) {
		this.humanLangListeners.remove(l);
	}

	private boolean doDebug = false;
	public void switchDebug() {
		doDebug = !doDebug;
		if (doDebug) {
			Lesson l = getCurrentLesson();
			getLogger().log("Saving location: "+SAVE_DIR.getAbsolutePath());
			getLogger().log("Lesson: "+(l==null?"None loaded yet":l.getName()));
			getLogger().log("Exercise: "+(l==null?"None loaded yet":l.getCurrentExercise().getName()));
			if(l!=null) {
				((Exercise) l.getCurrentExercise()).setNbError(-1);
				for (World w:((Exercise)l.getCurrentExercise()).getWorlds(WorldKind.ANSWER)) {
					String s = w.getDebugInfo();
					if (s != "") 
						getLogger().log("World: "+s);
				}
			}
			getLogger().log("PLM version: "+Game.getProperty("plm.major.version","internal",false)+" ("+Game.getProperty("plm.major.version","internal",false)+"."+Game.getProperty("plm.minor.version","",false)+")");
			getLogger().log("Java version: "+System.getProperty("java.version")+" (VM: "+ System.getProperty("java.vm.name")+" "+ System.getProperty("java.vm.version")+")");
			getLogger().log("System: " +System.getProperty("os.name")+" (version: "+System.getProperty("os.version")+"; arch: "+ System.getProperty("os.arch")+")");
			for (ScriptEngineFactory sef : new ScriptEngineManager().getEngineFactories()) {
				StringBuilder sb = new StringBuilder(sef.toString())
				.append("  Engine: ")
				.append(sef.getEngineName())
				.append(" ")
				.append(sef.getEngineVersion());
				getLogger().log(sb.toString());
				sb = new StringBuilder("  Language: ")
				.append(sef.getLanguageName())
				.append(" ")
				.append(sef.getLanguageVersion());
				getLogger().log(sb.toString());
				sb = new StringBuilder("  Names: ")
				.append(sef.getNames());
				getLogger().log(sb.toString());
			}
		}
	}
	public boolean isDebugEnabled() {
		return doDebug;
	}
	private boolean doBatch = false;
	public void setBatchExecution() {
		doBatch = true;
	}
	public boolean isBatchExecution() {
		return doBatch;
	}

	private boolean doCreative = false;		
	public void switchCreative() {
		doCreative =  !doCreative;
	}
	public boolean isCreativeEnabled() {
		return doCreative;
	}

	/*
	 * Getter and Setter for the course ID for the current session.
	 * This ID will be used by the ServerSpy, to associate this
	 * PLM student with a course started by a teacher on the server
	 */
	public String getCourseID() {
		if (this.currentCourse == null)
			return "";
		else
			return currentCourse.getCourseId();
	}

	public String getCoursePassword(){
		if(this.currentCourse == null)
			return "";
		else
			return currentCourse.getPassword();
	}

	public void setCourseID(String courseID) {
		this.currentCourse.setCourseId(courseID);
	}

	public Course getCurrentCourse() {
		return currentCourse;
	}

	public void setCurrentCourse(Course currentCourse) {
		this.currentCourse = currentCourse;
	}

	public HeartBeatSpy getHeartBeatSpy(){ return this.heartBeatSpy; }

	public void setHeartBeatSpy(HeartBeatSpy heartBeatSpy){ this.heartBeatSpy = heartBeatSpy; }

	public ArrayList<ProgressSpyListener> getProgressSpyListeners(){ return this.progressSpyListeners; }

	/* Mechanism to find where to save our data */

	private static String HOME_DIR = System.getProperty("user.home");

	/* These names are tested one after the other, searching for one that exist or that we can create */
	static String[] rootDirNames = new String[] { 
		HOME_DIR + File.separator + ".plm", /* preferred, default directory name */
		HOME_DIR + File.separator + "_plm", /* Some paranoid administrator refuse directories which name starts with a dot */
		HOME_DIR + File.separator + "plm",  /* one day, hidden directories with make trouble... */
		"h:"     + File.separator + "_plm", /* windows-preferred directory name */
		"h:"     + File.separator + "plm",
		"z:"     + File.separator + "_plm", /* windows-preferred directory name */
		"z:"     + File.separator + "plm",
	};
	private static File SAVE_DIR = initializeSaveDir();

	// FIXME: Should not be static
	private static File initializeSaveDir() {
		StringBuffer sb = new StringBuffer();
		for (String path : rootDirNames) {
			File res = new File(path);
			sb.append(path);
			sb.append(", ");
			try {
				if (res.exists()) {
					if (res.isDirectory()) {
						if (res.canWrite()) {
							return res;
						} else {
							//.getLogger().log(res.getAbsolutePath()+" is not writable.");
							continue;
						}
					} else {
						//.getLogger().log(res.getAbsolutePath()+" is not a directory.");
						continue;
					}
				}
				if (res.mkdir())
					return res;
			} catch (SecurityException e) {
				e.getLocalizedMessage();
			}
		}
		throw new RuntimeException("Impossible to find a path for PLM data. Tested "+sb.toString());
	}
	public static String getSavingLocation() {
		return SAVE_DIR.getPath();
	}

	public void revertExo() {
		Lecture lect = getCurrentLesson().getCurrentExercise();
		if (! (lect instanceof Exercise)) 
			return;

		Exercise ex = (Exercise) lect;
		for (ProgrammingLanguage lang: ex.getProgLanguages()){
			for (int i=0; i<ex.getSourceFileCount(lang); i++) {
				SourceFile sf = ex.getSourceFile(lang,i);
				if (sf instanceof SourceFileRevertable)
					((SourceFileRevertable) sf).revert();
			}
		}
		for (ProgrammingLanguage pl:Game.programmingLanguages)
			studentWork.setPassed(ex, pl, false);
		for (ProgressSpyListener l : this.progressSpyListeners) {
			l.reverted(ex);
		}
	}

	public void setUserUUID(String userUUID) {
		try {
			saveSession();
		} catch (UserAbortException e) {
			e.printStackTrace();
		}
		if(currentLesson != null) {
			removeHumanLangListener(currentLesson.getCurrentExercise());
		}
		currentLesson = null;
		lastExercise = null;
		for(Lesson lesson: lessons.values()) {
			removeHumanLangListener(lesson);
		}
		lessons.clear();		
		loadedLessons.clear();
		studentWork = new SessionDB(this);
		initLessons();

		loadSession();
	}
	
	public void signalIdle(String start, String end, String duration) {
	}
	
	public void setTrackUser(boolean trackUser) {
		this.trackUser = trackUser;
	}
	
	public boolean getTrackUser() {
		return trackUser;
	}
	
	public LogHandler getLogger() {
		return logger;
	}
	
	public String getLocalProperty(String key) {
		return localProperties.getProperty(key);
	}
	
	public void setLocalProperty(String key, String value) {
		localProperties.setProperty(key, value);
	}
}
