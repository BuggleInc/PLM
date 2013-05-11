package jlm.core.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import javax.swing.JOptionPane;

import jlm.core.GameListener;
import jlm.core.GameStateListener;
import jlm.core.ProgLangChangesListener;
import jlm.core.StatusStateListener;
import jlm.core.model.lesson.Exercise;
import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lecture;
import jlm.core.model.lesson.Lesson;
import jlm.core.model.session.ISessionKit;
import jlm.core.model.session.SessionDB;
import jlm.core.model.session.ZipSessionKit;
import jlm.core.model.tracking.HeartBeatSpy;
import jlm.core.model.tracking.IdenticaSpy;
import jlm.core.model.tracking.LocalFileSpy;
import jlm.core.model.tracking.ProgressSpyListener;
import jlm.core.model.tracking.ServerSpyAppEngine;
import jlm.core.model.tracking.TwitterSpy;
import jlm.core.ui.MainFrame;
import jlm.universe.Entity;
import jlm.universe.IWorldView;
import jlm.universe.World;

/*
 *  core model which contains all known exercises.
 */
public class Game implements IWorldView {

	private GameState state = GameState.IDLE;

	private final static String LOCAL_PROPERTIES_FILENAME = "jlm.properties";
	private final static String LOCAL_PROPERTIES_SUBDIRECTORY = ".jlm";

	private static Properties defaultGameProperties = new Properties();
	private static Properties localGameProperties = new Properties();
	private static File localGamePropertiesLoadedFile;

	private static Game instance = null;
	private Map<String, Lesson> lessons = new HashMap<String, Lesson>();
	private Lesson currentLesson;
	private Course currentCourse;

	public static final ProgrammingLanguage JAVA = new ProgrammingLanguage("Java","java");
	public static final ProgrammingLanguage JAVASCRIPT = new ProgrammingLanguage("JavaScript","js");
	public static final ProgrammingLanguage PYTHON = new ProgrammingLanguage("Python","py");
	public static final ProgrammingLanguage RUBY = new ProgrammingLanguage("Ruby","rb");
	public static final ProgrammingLanguage LIGHTBOT = new ProgrammingLanguage("lightbot","ignored");
	public static final ProgrammingLanguage[] programmingLanguages = new ProgrammingLanguage[] {
		JAVA, JAVASCRIPT, PYTHON, RUBY, LIGHTBOT
	};
	private ProgrammingLanguage programmingLanguage = JAVA;

	private List<GameListener> listeners = new ArrayList<GameListener>();
	private World selectedWorld;
	private World answerOfSelectedWorld;
	private World initialOfSelectedWorld;
	private Entity selectedEntity;
	private List<Thread> lessonRunners = new ArrayList<Thread>();
	private List<Thread> demoRunners = new ArrayList<Thread>();
	private static List<Thread> initRunners = new ArrayList<Thread>();

    private HeartBeatSpy heartBeatSpy;

	private ArrayList<GameStateListener> gameStateListeners = new ArrayList<GameStateListener>();

	private LogWriter outputWriter;

	public SessionDB studentWork = new SessionDB();
	private ISessionKit sessionKit = new ZipSessionKit(this);

	private static boolean ongoingInitialization = false;
	private static String lessonChooser = "lessons.chooser";
	
	public static Game getInstance() {
		if (Game.instance == null) {
			if (ongoingInitialization)
				throw new RuntimeException("Loop in initialization process. This is a JLM bug.");
			ongoingInitialization = true;
			Game.instance = new Game();
			ongoingInitialization = false;
		}
		return Game.instance;
	}

	private Game() {
		Game.loadProperties();
		loadSession();

		addProgressSpyListener(new IdenticaSpy());
		addProgressSpyListener(new TwitterSpy());
        addProgressSpyListener(new LocalFileSpy());
        addProgressSpyListener(new ServerSpyAppEngine());

        currentCourse = new CourseAppEngine();
	}
	
	
	/**
	 * Load the chooser, stored in jlm.core.ui.chooser
	 */
	public void loadChooser() {
		Game.instance.switchLesson(lessonChooser);
	}
	
	
	/** Change the current lesson.
	 * 
	 * Also, initialize the newly used lesson on need. It must already be in the classpath 
	 * (use @loadLesson() if you want to load a lesson located in an external jar file)
	 *  
	 * @param lessonName package name of the lesson to load 
	 */
	
	public Lesson switchLesson(String lessonName) {
		statusArgAdd("Load lesson " + lessonName);
		
		// Try caching the lesson to avoid the possibly long loading time during which we compute the solution of each exercise  
		Lesson lesson = lessons.get(lessonName);
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
				System.err.println("Cannot switch to lesson "+lessonName+": class Main not found.");
				statusArgRemove("Load lesson "+lessonName);				
				return getCurrentLesson();
			}
		}
		// Prevent an error message telling us that the JLM couldn't load our code for the chooser -- kinda obvious
		if ( !lessonName.equals(lessonChooser))
			sessionKit.loadLesson(null, lesson);
		setCurrentLesson(lesson);
		statusArgRemove("Load lesson "+lessonName);
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
		Game.getInstance().switchLesson("lessons." + lessonPackage);
    }//end method

	

	public static void addInitThread(Thread t) {
		initRunners.add(t);
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
		this.currentLesson = lesson;
		fireCurrentLessonChanged();
		setCurrentExercise(this.currentLesson.getCurrentExercise());
	}

	// only to avoid that exercise views register as listener of a lesson
	public void setCurrentExercise(Lecture lect) {
		fireCurrentExerciseChanged(lect);
		this.currentLesson.setCurrentExercise(lect);
		if (lect instanceof Exercise) {
			Exercise exo = (Exercise) lect;
			exo.reset();
			setSelectedWorld(exo.getWorld(0));

			boolean seenJava=false;
			for (ProgrammingLanguage l:exo.getProgLanguages()) {
				if (l.equals(programmingLanguage))
					return; /* The exo accepts the language we currently have */
				if (l.equals(Game.JAVA))
					seenJava = true;
			}
			/* Use java as a fallback programming language (if the exo accepts it)  */
			if (seenJava)
				setProgramingLanguage(Game.JAVA);
			/* The exo don't like our currently set language, nor Java. Let's pick its first selected language */
			setProgramingLanguage( exo.getProgLanguages().iterator().next() );
		}
		MainFrame.getInstance().currentExerciseHasChanged(lect); // make sure that the right language is selected -- yeah that's a ugly way of doing it
	}

	public World getSelectedWorld() {
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
			this.initialOfSelectedWorld = exo.getInitialWorld().get(index);
			if (this.selectedWorld.getEntityCount()>0) {
				this.selectedEntity = this.selectedWorld.getEntity(0);
			}
			fireSelectedWorldHasChanged(world);
		} else {
			throw new RuntimeException("The lecture "+lect+" has no world that I can select");
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
	public void startExerciseExecution() {
		LessonRunner runner = new LessonRunner(Game.getInstance(), this.lessonRunners);
		runner.start();
	}
	@SuppressWarnings("deprecation")
	public void stopExerciseExecution() {
		while (this.lessonRunners.size() > 0) {
			Thread t = lessonRunners.remove(lessonRunners.size() - 1);
			t.stop(); // harmful but who cares ?
		}
		Lecture lecture = this.currentLesson.getCurrentExercise();
		if (lecture instanceof Exercise)
			for (World w : ((Exercise) lecture).getAnswerWorld())
				w.doneDelay();

		setState(GameState.EXECUTION_ENDED);
	}
	public void startExerciseDemoExecution() {
		DemoRunner runner = new DemoRunner(Game.getInstance(), this.demoRunners);
		runner.start();
	}
	public void startExerciseStepExecution() {
		stepMode = true;
		LessonRunner runner = new LessonRunner(Game.getInstance(), this.lessonRunners);
		runner.start();
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
			for (World w: ((Exercise) lecture).getCurrentWorld())
				for (Entity e : w.getEntities())
					e.allowOneStep();
	}

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

	public void setOutputWriter(LogWriter writer) {
		this.outputWriter = writer;
		if (!getProperty("output.capture", "false").equals("true")) {
			Logger l = new Logger(outputWriter);
			System.setOut(l);
			System.setErr(l);
		}
	}

	public LogWriter getOutputWriter() {
		return this.outputWriter;
	}

	public void quit() {
		try {
			// FIXME: this method is not called when pressing APPLE+Q on OSX

            // report user leave on the server
            for(ProgressSpyListener spyListener: progressSpyListeners){
                spyListener.leave();
            }
            // stop the heartbeat report to JLMServer
            if(heartBeatSpy != null)
                heartBeatSpy.die();

			saveSession();
			storeProperties();
			System.exit(0);
		} catch (UserAbortException e) {
			// Ok, user decided to not quit (to get a chance to export the session)
			System.out.println("Exit aborted");
		}
	}

	public void clearSession() {
		this.sessionKit.cleanAll();
		for (Lesson l : this.lessons.values())
			for (Lecture lect : l.exercises())
				if (lect instanceof Exercise)
					for (ProgrammingLanguage lang:((Exercise) lect).getProgLanguages()) 
						Game.getInstance().studentWork.setPassed(lect.getId(), lang, false);

		fireCurrentExerciseChanged(currentLesson.getCurrentExercise());
	}

	public void loadSession() {
		this.setState(GameState.LOADING);
		this.sessionKit.loadAll();
		this.setState(GameState.LOADING_DONE);
	}

	public void saveSession() throws UserAbortException {
		this.setState(GameState.SAVING);
		this.sessionKit.storeAll();
		this.setState(GameState.SAVING_DONE);
	}

	public ISessionKit getSessionKit() {
		return this.sessionKit;
	}

	public static void loadProperties() {
		InputStream is = null;
		try {
			is = Game.class.getClassLoader().getResourceAsStream("resources/jlm.configuration.properties");
			if (is==null) // try to find the file in the debian package
				is = Game.class.getClassLoader().getResourceAsStream("/etc/jlm.configuration.properties");
			Game.defaultGameProperties.load(is);
		} catch (InvalidPropertiesFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			// resources/jlm.configuration.properties not found. Try jlm.configuration.properties afterward
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

		String value = Game.defaultGameProperties.getProperty("jlm.configuration.file.path");
		if (value != null) {
			String paths[] = value.split(",");

			for (String localPath : paths) {
				localPath = localPath.replace("$HOME$", System.getProperty("user.home"));
				File localPropertiesFile = new File(localPath + File.separator + Game.LOCAL_PROPERTIES_SUBDIRECTORY,
						Game.LOCAL_PROPERTIES_FILENAME);
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
					break;
				}
			}
		}
	}

	public static void storeProperties() throws UserAbortException {
		FileOutputStream fo = null;
		try {

			// if (Game.localGamePropertiesLoadedFile == null) {

			String value = Game.getProperty("jlm.configuration.file.path");
			if (value != null) {
				String paths[] = value.split(",");

				for (String localPath : paths) {
					localPath = localPath.replace("$HOME$", System.getProperty("user.home"));
					File localPropertiesFileParentDirectory = new File(localPath);
					File localPropertiesFileDirectory = new File(localPath, Game.LOCAL_PROPERTIES_SUBDIRECTORY);

					if (!localPropertiesFileParentDirectory.exists()) {
						continue;
					} else if (localPropertiesFileDirectory.exists() || localPropertiesFileDirectory.mkdir()) {
						Game.localGamePropertiesLoadedFile = new File(localPropertiesFileDirectory,
								Game.LOCAL_PROPERTIES_FILENAME);
						break;
					} else {
						Logger.log("Game:storeProperties", "cannot create local properties store directory ("
								+ localPropertiesFileDirectory + ")");
					}

				}
			} else {
				int choice = JOptionPane.showConfirmDialog(null,
						"No path provided in the property file (or property file not found)\n"+
						"You may want to export your session with the menu 'Session/Export session'\n" +
						"to save your work manually.\n\n" +
						"Quit without saving?",
						"Cannot save your changes. Quit without saving?",
						JOptionPane.YES_NO_OPTION,JOptionPane.ERROR_MESSAGE);
				if (choice==JOptionPane.NO_OPTION)
					throw new UserAbortException("Quit aborded by user.");
				return;
			}
			// }
			fo = new FileOutputStream(Game.localGamePropertiesLoadedFile);
			Game.localGameProperties.store(fo, "Java Learning Machine properties");
			System.out.println("Game:storeProperties: properties stored in " + localGamePropertiesLoadedFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fo != null)
				try {
					fo.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	public static String getProperty(String key) {
		return Game.getProperty(key, null);
	}

	public static String getProperty(String key, String defaultValue) {
		if (Game.localGameProperties.containsKey(key)) {
			return Game.localGameProperties.getProperty(key);
		} else {
			return Game.defaultGameProperties.getProperty(key, defaultValue);
		}
	}

	public void addGameListener(GameListener l) {
		this.listeners.add(l);
	}

	public void removeGameListener(GameListener l) {
		this.listeners.remove(l);
	}

	protected void fireCurrentLessonChanged() {
		for (GameListener v : this.listeners) {
			v.currentLessonHasChanged();
		}
	}

    protected void fireCurrentExerciseChanged(Lecture lect) {
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
		for (GameStateListener l : this.gameStateListeners) {
			l.stateChanged(status);
		}
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
		String str = stateTxt;
		boolean first = true;
		for (String s:statusArgs) {
			if (first)
				first = false;
			else
				str += ", ";
			str+= s;
		}
		for (StatusStateListener l : this.statusStateListeners) {
			l.stateChanged(str);
		}
	}
	public void setLocale(String lang) {
		FileUtils.setLocale(lang);
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



	public void setProgramingLanguage(ProgrammingLanguage newLanguage) {
		if (programmingLanguage.equals(newLanguage))
			return;

		if (isValidProgLanguage(newLanguage)) {
			//System.out.println("Switch programming language to "+newLanguage);
			this.programmingLanguage = newLanguage;
			fireProgLangChange(newLanguage);
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

	private boolean doDebug = false;
	public void switchDebug() {
		doDebug = !doDebug;
		if (doDebug) {
			System.out.println("Saving location: "+Game.getInstance().getSessionKit().getSavingLocation());
			System.out.println("Lesson: "+Game.getInstance().getCurrentLesson().getName());
			System.out.println("Exercise: "+Game.getInstance().getCurrentLesson().getCurrentExercise().getName());
			System.out.println("JLM version: "+Game.getProperty("jlm.major.version","internal")+" ("+Game.getProperty("jlm.major.version","internal")+"."+Game.getProperty("jlm.minor.version","")+")");
			System.out.println("Java version: "+System.getProperty("java.version")+" (VM version: "+ System.getProperty("java.vm.version")+")");
			System.out.println("System: " +System.getProperty("os.name")+" (version: "+System.getProperty("os.version")+"; arch: "+ System.getProperty("os.arch")+")");
		}
	}
	public boolean isDebugEnabled() {
		return doDebug;
	}

    public static String getLocalPropertiesSubdirectory() {
        return LOCAL_PROPERTIES_SUBDIRECTORY;
    }



    /*
     * Getter and Setter for the course ID for the current session.
     * This ID will be used by the ServerSpy, to associate this
     * JLM student with a course started by a teacher on the server
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
}
