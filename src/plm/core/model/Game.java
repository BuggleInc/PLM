package plm.core.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
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

import org.eclipse.jgit.api.errors.GitAPIException;
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
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Exercise.WorldKind;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lecture;
import plm.core.model.lesson.Lesson;
import plm.core.model.lesson.Lesson.LoadingOutcome;
import plm.core.model.session.GitSessionKit;
import plm.core.model.session.ISessionKit;
import plm.core.model.session.SessionDB;
import plm.core.model.session.SourceFile;
import plm.core.model.session.SourceFileRevertable;
import plm.core.model.tracking.GitSpy;
import plm.core.model.tracking.HeartBeatSpy;
import plm.core.model.tracking.LocalFileSpy;
import plm.core.model.tracking.ProgressSpyListener;
import plm.core.model.tracking.ServerSpyAppEngine;
import plm.core.ui.MainFrame;
import plm.core.utils.FileUtils;
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

	private static Game instance = null;
	private Map<String, Lesson> lessons = new HashMap<String, Lesson>();
	private Lesson currentLesson;
	private Course currentCourse;
	private Lecture lastExercise;

	public static final String [][] humanLangs = { {"English","en"}, {"Francais","fr"}, {"Italiano","it"}, {"Português brasileiro", "pt_BR"}, {"中文", "zh"} };

	public static final ProgrammingLanguage JAVA =       new LangJava();
	public static final ProgrammingLanguage PYTHON =     new LangPython();
	public static final ProgrammingLanguage SCALA =      new LangScala();
	public static final ProgrammingLanguage C =          new LangC();
	//public static final ProgrammingLanguage JAVASCRIPT = new ProgrammingLanguage("JavaScript","js",ResourcesCache.getIcon("img/lang_javascript.png"));
	public static final ProgrammingLanguage RUBY =       new LangRuby();
	public static final ProgrammingLanguage LIGHTBOT =   new LangLightbot();
	
	public static final ProgrammingLanguage[] programmingLanguages = new ProgrammingLanguage[] {
		JAVA, PYTHON, SCALA, RUBY, LIGHTBOT, C // TODO: re-add JAVASCRIPT to this list once it works at least a bit
	}; 
	private ProgrammingLanguage programmingLanguage = JAVA;

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

	private LogWriter outputWriter;
	private PrintStream outputOrig = System.out;
	private PrintStream errorOrig = System.err;


	public SessionDB studentWork = new SessionDB();
	//private ISessionKit sessionKit = new ZipSessionKit(this);
	private ISessionKit sessionKit;

	private Users users;

	private static boolean ongoingInitialization = false;
	public  static I18n i18n;

	public static Game getInstance() {
		if (Game.instance == null) {
			if (ongoingInitialization)
				throw new RuntimeException("Loop in initialization process. This is a PLM bug: please report it.");
			ongoingInitialization = true;
			Game.instance = new Game();
			ongoingInitialization = false;
			Game.instance.loadSession();
		}
		return Game.instance;
	}

	private Game() {
		i18n = I18nFactory.getI18n(getClass(),"org.plm.i18n.Messages",FileUtils.getLocale(), I18nFactory.FALLBACK);
		loadProperties();

		if (checkScala())
			System.err.println(i18n.tr("Scala is usable on your machine. Congratulations."));
		else
			System.err.println(i18n.tr("Please install Scala version 2.11 or higher to use it in the PLM."));
		if (checkPython())
			System.err.println(i18n.tr("Jython is usable on your machine. Congratulations."));
		else
			System.err.println(i18n.tr("Please install jython to use the python programming language in the PLM."));
		if (checkC())
			System.err.println(i18n.tr("C is usable on your machine. Congratulations."));
		else
			System.err.println(i18n.tr("Please install gcc to use the C programming language in the PLM."));

		String defaultProgrammingLanguage = Game.getProperty(PROP_PROGRAMING_LANGUAGE,Game.JAVA.getLang(),true);
		if (!defaultProgrammingLanguage.equalsIgnoreCase(Game.JAVA.getLang()) &&
				!defaultProgrammingLanguage.equalsIgnoreCase(Game.PYTHON.getLang()) &&
				!defaultProgrammingLanguage.equalsIgnoreCase(Game.SCALA.getLang()) && 
				!defaultProgrammingLanguage.equalsIgnoreCase(Game.C.getLang())) 
			System.err.println(i18n.tr("Warning, the default programming language is neither ''Java'' nor ''python'' or ''Scala'' or ''C'' but {0}.\n"+
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
		} else {
			for (ProgrammingLanguage pl : Game.getProgrammingLanguages()) 
				if (pl.getLang().equals(defaultProgrammingLanguage)) {
					setProgramingLanguage(pl);
					break;
				}
		}

		users = new Users(SAVE_DIR);
		users.getCurrentUser();

		addProgressSpyListener(new LocalFileSpy(SAVE_DIR));
		sessionKit = new GitSessionKit(this);

		try {
			addProgressSpyListener(new GitSpy(SAVE_DIR, users));
		} catch (IOException | GitAPIException e) {
			System.err.println(Game.i18n.tr("You found a bug in the PLM. Please report it with all possible details (including the stacktrace below"));
			e.printStackTrace();
		}


		if (getProperty(PROP_PROGRESS_APPENGINE, "false",true).equalsIgnoreCase("true"))
			addProgressSpyListener(new ServerSpyAppEngine());
		
		if (! Game.getProperty(Game.PROP_APPENGINE_URL).equals("")) { // FIXME: there is no way real proper way to disable the CourseEngine !!!
	        currentCourse = new CourseAppEngine();
		}
	}

	boolean canScala = false;
	String scalaError = "";

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private boolean checkScala() {
		String[] resources = new String[] {"/scala/tools/nsc/Interpreter", "/scala/Unit",        "/scala/reflect/io/AbstractFile"};
		String[] hints     = new String[] {"scala-compiler.jar",           "scala-library.jar",  "scala-reflect.jar"};
		for (int i=0;i<resources.length;i++) {
			scalaError = canResolve(resources[i],hints[i]);
			if (!scalaError.isEmpty()) {
				System.err.println(scalaError);
				return canScala;
			}
		}

		String version = null;
		try {
			Class props = Class.forName("scala.util.Properties");
			Method meth = props.getMethod("versionString", new Class[] {});
			version = (String) meth.invoke(props);
		} catch (Exception e) {
			scalaError = i18n.tr("Error {0} while retrieving the Scala version: {1}", e.getClass().getName() ,e.getLocalizedMessage());
			System.err.println( scalaError );
			return canScala;
		}

		if (version.contains("version 2.10") || version.contains("version 2.11")) {
			canScala = true;
			return canScala;
		} else {
			scalaError = i18n.tr("Scala is too ancient. Found {0} while I need 2.10 or higher.",version);
			System.err.println(scalaError);
			return canScala;
		}
	}

	public boolean canPython = false;
	String pythonError = "";
	private boolean checkPython() {
		String[] resources = new String[] {
				"/org/python/jsr223/PyScriptEngineFactory", "/org/jruby/ext/posix/util/Platform","/org/antlr/runtime/CharStream",
				"/org/objectweb/asm/Opcodes"
		};
		String[] hints     = new String[] {"jython.jar", "jruby.jar","antlr3-runtime.jar",
		"asm3.jar"};
		for (int i=0;i<resources.length;i++) {
			pythonError = canResolve(resources[i],hints[i]);
			if (!pythonError.isEmpty()) {
				System.err.println(pythonError);
				return canPython;
			}
		}

		ScriptEngineManager manager = new ScriptEngineManager();       
		if (manager.getEngineByName("python") == null) {
			pythonError = i18n.tr("Cannot retrieve the python ScriptEngine. Are jython.jar and its dependencies in the classpath?");
		}

		canPython = true;
		return true;
	}


	public boolean canC = false;
	String CError = "";
	private boolean checkC(){
		Runtime runtime = Runtime.getRuntime();
		try {
			runtime.exec("gcc --version");
			canC=true;
		} catch (IOException e) {
			e.printStackTrace();
			canC=false;
		}
		return canC;
	}


	private String canResolve(String resource, String hint) {
		try {
			URL path = getClass().getResource(resource+".class");
			if (path != null)
				return ""; // Cool, found it.

			path = ClassLoader.getSystemResource(resource+".class");
			if (path != null)
				return ""; // Cool, found it.

			resource = resource.replaceAll("/", ".");
			resource = resource.substring(1);
			Class.forName(resource).newInstance();
			return ""; // That's cool if I manage to create one such object

		} catch (ClassNotFoundException ce) {
			return i18n.tr("Resource {0} not found in the classpath.\nIs {1} in your classpath?",resource,hint);
		} catch (Exception e) {
			return i18n.tr("{0} received while searching for resource {1}: {2}",e.getClass().getName(),resource,e.getLocalizedMessage());
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
		statusArgAdd(lessonName);

		if (lesson == null) { // we have to load it 
			try {
				// This is where we assume here that each lesson contains a Main object, instantiating the Lesson class.
				// We manually build a call to the constructor of this object, and fire it
				// This creates such an object, which is in charge of creating the whole lesson (including exercises) from its constructor
				lesson = (Lesson) Class.forName(lessonName + ".Main").newInstance();

				lessons.put(lessonName, lesson); // cache the newly created object
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				if (failOnError)
					throw new RuntimeException(Game.i18n.tr("Cannot switch to lesson {0}: class Main not found.",lessonName));
				System.err.println(Game.i18n.tr("Cannot switch to lesson {0}: class Main not found.",lessonName));
				statusArgRemove(Game.i18n.tr("Load lesson {0}",lessonName));				
				return getCurrentLesson();
			}
		}
		// Prevent obvious error messages
		if (sessionKit != null)
			sessionKit.loadLesson(SAVE_DIR, lesson);
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
		Game.getInstance().switchLesson("lessons." + lessonPackage,false);
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

	public Lesson getCurrentLesson() {
		if (this.currentLesson == null && this.lessons.size() > 0) {
			setCurrentLesson(this.lessons.get(0));
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
		}
		try {
			saveSession(); // don't loose user changes
			this.lastExercise = (currentLesson==null ? null : currentLesson.getCurrentExercise()); // save the last viewed exercise before switching7
			
			if (this.currentLesson != lect.getLesson())
				this.currentLesson = lect.getLesson();
			
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
					System.out.println(
							Game.i18n.tr("Exercise {0} does not support language {1}. Fallback to {2} instead. "
									+ "Please consider contributing to this project by adapting this exercise to this language.",
									lect.getName(),getProgrammingLanguage(),fallback.getLang()));
				setProgramingLanguage(fallback);

			}
			MainFrame.getInstance().currentExerciseHasChanged(lect); // make sure that the right language is selected -- yeah that's a ugly way of doing it
		} catch (UserAbortException e) { 
			System.out.println(i18n.tr("Operation cancelled by the user"));
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
			this.initialOfSelectedWorld = exo.getWorlds(WorldKind.INITIAL).get(index);
			if (this.selectedWorld.getEntityCount()>0) {
				this.selectedEntity = this.selectedWorld.getEntity(0);
			}
			fireSelectedWorldHasChanged(world);
		} else {
			throw new RuntimeException(Game.i18n.tr("The lecture {0} has no world that I can select",lect));
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
		runner = new LessonRunner(Game.getInstance());
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
			for (World w : ((Exercise) lecture).getWorlds(WorldKind.ANSWER))
				w.doneDelay();

		setState(GameState.EXECUTION_ENDED);
	}
	public void startExerciseDemoExecution() {
		DemoRunner runner = new DemoRunner(Game.getInstance(), this.demoRunners);
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

	public void setCaptureOutput(boolean isCaptured) {
		if (isCaptured && getProperty(PROP_OUTPUT_CAPTURE, "true",true).equals("true")) {
			Logger l = new Logger(outputWriter);
			System.setOut(l);
			System.setErr(l);
		} else if (System.out.equals(this.outputOrig)) {
			System.setOut(this.outputOrig);
			System.setErr(this.errorOrig);
		}
	}
	public void setOutputWriter(LogWriter writer) {
		this.outputWriter = writer;
	}

	public LogWriter getOutputWriter() {
		return this.outputWriter;
	}

	public void quit() {
		try {
			// FIXME: this method is not called when pressing APPLE+Q on OSX

			saveSession();
			
			// report user leave on the server
			for(ProgressSpyListener spyListener: progressSpyListeners){
				spyListener.leave();
			}
			// stop the heartbeat report to PLMServer
			if(heartBeatSpy != null)
				heartBeatSpy.die();

			storeProperties();
			System.exit(0);
		} catch (UserAbortException e) {
			// Ok, user decided to not quit (to get a chance to export the session)
			System.out.println("Exit aborted");
		}
	}

	public void clearSession() {
		if (sessionKit == null)
			return;
		this.sessionKit.cleanAll(SAVE_DIR);
		for (Lesson l : this.lessons.values())
			for (Lecture lect : l.exercises())
				if (lect instanceof Exercise)
					for (ProgrammingLanguage lang:((Exercise) lect).getProgLanguages()) 
						Game.getInstance().studentWork.setPassed(lect, lang, false);

		fireCurrentExerciseChanged(currentLesson.getCurrentExercise());
	}

	public void loadSession() {
		if (sessionKit == null)
			return;
		this.setState(GameState.LOADING);
		this.sessionKit.loadAll(SAVE_DIR);
		this.setState(GameState.LOADING_DONE);
	}

	public void saveSession() throws UserAbortException {
		if (sessionKit == null)
			return;
		this.setState(GameState.SAVING);
		this.sessionKit.storeAll(SAVE_DIR);
		this.setState(GameState.SAVING_DONE);
		storeProperties();
	}

	public ISessionKit getSessionKit() {
		return this.sessionKit;
	}
	public void removeSessionKit() {
		sessionKit = null;
		System.out.println("Disabling the session kit on disk.");
	}

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
			System.out.println(String.format("Loading properties [%s]", localPropertiesFile));
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
		FileUtils.setLocale(lang);
		i18n = I18nFactory.getI18n(getClass(),"org.plm.i18n.Messages",getLocale(), I18nFactory.FALLBACK);
		fireHumanLangChange(lang);

		/* FIXME: convert all this to a humanLanguage listener */
		if (  Game.getInstance() != null && Game.getInstance().getCurrentLesson() != null ) {
			Game.getInstance().getCurrentLesson().resetAboutLoaded();
			Lecture lect = Game.getInstance().getCurrentLesson().getCurrentExercise();
			if ( lect instanceof Exercise )
				((Exercise) lect).getWorlds(WorldKind.CURRENT).get(0).resetAbout();
		}

		for (Lesson lesson : lessons.values()) {
			for (Lecture lect:lesson.exercises()) {
				if (lect instanceof ExerciseTemplated) {
					lect.loadHTMLMission();
				}
			}
		}
		setCurrentLesson(getCurrentLesson());
		currentLesson.setCurrentExercise(currentLesson.getCurrentExercise());
	}
	public Locale getLocale(){
		return FileUtils.getLocale();
	}



	public void setProgramingLanguage(ProgrammingLanguage newLanguage) {
		if (programmingLanguage.equals(newLanguage))
			return;

		if (isValidProgLanguage(newLanguage)) {
			//System.out.println("Switch programming language to "+newLanguage);
			if (newLanguage.equals(Game.SCALA) && !canScala) {
				JOptionPane.showMessageDialog(null, i18n.tr("Please install Scala version 2.10 or higher to use it in the PLM.\n\n")+scalaError ,
						i18n.tr("Scala is missing"), JOptionPane.ERROR_MESSAGE); 
				return;
			}
			if (newLanguage.equals(Game.PYTHON) && !canPython) {
				JOptionPane.showMessageDialog(null, i18n.tr("Please install jython and its dependencies to use the python programming language in the PLM.\n\n")+pythonError,
						i18n.tr("Python is missing"), JOptionPane.ERROR_MESSAGE); 
				return;
			}
			if (newLanguage.equals(Game.C) && !canC) {
				JOptionPane.showMessageDialog(null, i18n.tr("Please install C and its dependencies to use the C programming language in the PLM.\n\n")+CError,
						i18n.tr("C is missing"), JOptionPane.ERROR_MESSAGE); 
				return;
			}
			if (newLanguage.equals(Game.C) && !doBatch) {
				int res = JOptionPane.showConfirmDialog(null, 
						i18n.tr(  "The C langage is currently very experimental in the PLM.\n"
			                    + "If you go for C, you may not be able to complete some exercises that\n"
			                    + "are still in progress in C, although some other parts are already okay.\n\n"
			                    + "Do you want to proceed anyway?"),
						i18n.tr("C is still experimental"), JOptionPane.OK_CANCEL_OPTION);
				if (res != JOptionPane.OK_OPTION)
					return;
			}
			this.programmingLanguage = newLanguage;
			fireProgLangChange(newLanguage);
			if (newLanguage.equals(Game.JAVA) || newLanguage.equals(Game.PYTHON) || newLanguage.equals(Game.SCALA) || newLanguage.equals(Game.C)) // Only save it if it's stable enough
				setProperty(PROP_PROGRAMING_LANGUAGE, newLanguage.getLang());
			return;
		}
		throw new RuntimeException("Ignoring request to switch the programming language to the unknown "+newLanguage);
	}

	public static ProgrammingLanguage getProgrammingLanguage() {
		if (ongoingInitialization) /* break an initialization loop -- the crude way (FIXME) */
			return JAVA;
		else
			return getInstance().programmingLanguage;
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
		progLangListeners.add(l);
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
		humanLangListeners.add(l);
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
			Lesson l = Game.getInstance().getCurrentLesson();
			System.out.println("Saving location: "+SAVE_DIR.getAbsolutePath());
			System.out.println("Lesson: "+(l==null?"None loaded yet":l.getName()));
			System.out.println("Exercise: "+(l==null?"None loaded yet":l.getCurrentExercise().getName()));
			if(l!=null) {
				for (World w:((Exercise)l.getCurrentExercise()).getWorlds(WorldKind.ANSWER)) {
					String s = w.getDebugInfo();
					if (s != "") 
						System.out.println("World: "+s);
				}
			}
			System.out.println("PLM version: "+Game.getProperty("plm.major.version","internal",false)+" ("+Game.getProperty("plm.major.version","internal",false)+"."+Game.getProperty("plm.minor.version","",false)+")");
			System.out.println("Java version: "+System.getProperty("java.version")+" (VM: "+ System.getProperty("java.vm.name")+" "+ System.getProperty("java.vm.version")+")");
			System.out.println("System: " +System.getProperty("os.name")+" (version: "+System.getProperty("os.version")+"; arch: "+ System.getProperty("os.arch")+")");
			for (ScriptEngineFactory sef : new ScriptEngineManager().getEngineFactories()) {
				System.out.println(sef);
				System.out.append("  Engine: ")
				.append(sef.getEngineName())
				.append(" ")
				.println(sef.getEngineVersion());
				System.out.append("  Language: ")
				.append(sef.getLanguageName())
				.append(" ")
				.println(sef.getLanguageVersion());
				System.out.append("  Names: ")
				.println(sef.getNames());
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

	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
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
							System.out.println(res.getAbsolutePath()+" is not writable.");
							continue;
						}
					} else {
						System.out.println(res.getAbsolutePath()+" is not a directory.");
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
		for (ProgrammingLanguage lang: ex.getProgLanguages())
			for (int i=0; i<ex.getSourceFileCount(lang); i++) {
				SourceFile sf = ex.getSourceFile(lang,i);
				if (sf instanceof SourceFileRevertable)
					((SourceFileRevertable) sf).revert();
			}
		for (ProgrammingLanguage pl:Game.programmingLanguages)
			Game.getInstance().studentWork.setPassed(ex, pl, false);
		for (ProgressSpyListener l : this.progressSpyListeners) {
			l.reverted(ex);
		}
	}
}
