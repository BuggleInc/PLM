package jlm.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Properties;

import jlm.event.GameListener;
import jlm.event.GameStateListener;
import jlm.lesson.Exercise;
import jlm.lesson.Lesson;
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
	private ArrayList<Lesson> lessons = new ArrayList<Lesson>();
	private Lesson currentLesson;
	private ArrayList<GameListener> listeners = new ArrayList<GameListener>();
	private World selectedWorld;
	private World answerOfSelectedWorld;
	private Entity selectedEntity;
	private List<Thread> lessonRunners = new ArrayList<Thread>();
	private List<Thread> demoRunners = new ArrayList<Thread>();
	private boolean sequential = false;
	private ArrayList<GameStateListener> gameStateListeners = new ArrayList<GameStateListener>();

	private LogWriter outputWriter;

	private ISessionKit sessionKit = new ZipSessionKit(this);

	public static Game getInstance() {
		if (Game.instance == null)
			Game.instance = new Game();
		return Game.instance;
	}

	private Game() {
		Game.loadProperties();
		initLessons();
		loadSession();
	}

	public void initLessons() {
		String lessons = getProperty("jlm.lessons", "");
		if (lessons.equals("")) {
			System.err.println("Error: the property file does not contain any lesson to start. Default to lessons.welcome");
			lessons = "lessons.welcome";
		}
		String[] lessonNames = lessons.split(",");
		for (String name : lessonNames) {
			System.err.println("Load lesson "+name);
			Lesson lesson = null;
			try {
				lesson = (Lesson) Class.forName(name + ".Main").newInstance();
				addLesson(lesson);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	public void addLesson(Lesson lesson) {
		this.lessons.add(lesson);
		fireLessonsChanged();
	}

	public List<Lesson> getLessons() {
		return this.lessons;
	}

	public Lesson getCurrentLesson() {
		if (this.currentLesson == null && this.lessons.size() > 0) {
			setCurrentLesson(this.lessons.get(0));
		}
		return this.currentLesson;
	}

	public void setCurrentLesson(Lesson lesson) {
		if (isAccessible(lesson)) {
			this.currentLesson = lesson;
			fireCurrentLessonChanged();
			setCurrentExercise(this.currentLesson.getCurrentExercise());
		}
	}

	// only to avoid that exercise views register as listener of a lesson
	public void setCurrentExercise(Exercise exo) {
		if (exo.getLesson().isAccessible(exo)) {
			this.currentLesson.setCurrentExercise(exo);
			exo.reset();
			fireCurrentExerciseChanged();
			setSelectedWorld(this.currentLesson.getCurrentExercise().getWorld(0));
		}
	}

	public boolean isAccessible(Lesson lesson) {
		if (sequential) {
			int index = this.lessons.indexOf(lesson);
			if (index == 0)
				return true;
			if (index > 0)
				return this.lessons.get(index - 1).isSuccessfullyCompleted();
			return false;
		} else {
			return true;
		}
	}

	public World getSelectedWorld() {
		if (this.selectedWorld == null) {
			setSelectedWorld(this.getCurrentLesson().getCurrentExercise().getWorld(0));
		}
		return this.selectedWorld;
	}

	public void setSelectedWorld(World world) {
		if (this.selectedWorld != null)
			this.selectedWorld.removeEntityUpdateListener(this);
		this.selectedWorld = world;
		this.selectedWorld.addEntityUpdateListener(this);

		Exercise currentExercise = getCurrentLesson().getCurrentExercise();
		int index = currentExercise.indexOfWorld(this.selectedWorld);
		this.answerOfSelectedWorld = currentExercise.getAnswerOfWorld(index);
		this.selectedEntity = this.selectedWorld.getEntity(0);
		fireSelectedWorldHasChanged();
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

	public void startExerciseExecution() {
		LessonRunner runner = new LessonRunner(Game.getInstance(), this.lessonRunners);
		// lessonsRunner.add(runner);
		runner.start();
	}

	public void stopExerciseExecution() {
		while (this.lessonRunners.size() > 0) {
			Thread t = lessonRunners.remove(lessonRunners.size() - 1);
			t.stop(); // harmful but who cares ?
		}
		/*
		while (this.demoRunners.size() > 0) {
			Thread t = demoRunners.remove(demoRunners.size() - 1);
			t.stop(); // harmful but who cares ?
			// FIXME: stop demo execution but do not restore answer world, so it breaks everything !!!
		}
		*/
		setState(GameState.EXECUTION_ENDED);
	}

	public void startExerciseDemoExecution() {
		DemoRunner runner = new DemoRunner(Game.getInstance(), this.demoRunners);
		runner.start();
	}

	public void reset() {
		getCurrentLesson().getCurrentExercise().reset();
		fireCurrentExerciseChanged();
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
		// FIXME: this method is not called when pressing APPLE+Q on OSX
		saveSession();
		storeProperties();
		System.exit(0);
	}

	public void clearSession() {
		this.sessionKit.cleanUp();
		for (Lesson l : this.lessons)
			for (Exercise ex : l.exercises())
				ex.failed();
		fireLessonsChanged();
		fireCurrentExerciseChanged();
	}

	public void loadSession() {
		this.setState(GameState.LOADING);
		this.sessionKit.load();
		this.setState(GameState.LOADING_DONE);
	}

	public void saveSession() {
		this.setState(GameState.SAVING);
		this.sessionKit.store();
		this.setState(GameState.SAVING_DONE);
	}

	public ISessionKit getSessionKit() {
		return this.sessionKit;
	}

	public static void loadProperties() {
		InputStream is = null;
		try {
			is = Game.class.getClassLoader().getResourceAsStream("resources/jlm.configuration.properties");
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

					break;
				}
			}
		}
	}

	public static void storeProperties() {
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
				Logger.log("Game:storeProperties", "cannot store local properties, not path provided");
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

	protected void fireLessonsChanged() {
		for (GameListener v : this.listeners) {
			v.lessonsChanged();
		}
	}

	protected void fireCurrentExerciseChanged() {
		for (GameListener v : this.listeners) {
			v.currentExerciseHasChanged();
		}
	}

	protected void fireSelectedWorldHasChanged() {
		for (GameListener v : this.listeners) {
			v.selectedWorldHasChanged();
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

	@Override
	public void worldHasChanged() {
		setSelectedEntity(this.selectedWorld.getEntity(0));
		fireSelectedWorldWasUpdated();
	}

	@Override
	public void worldHasMoved() {
		// don't really care that something moved within the current world
	}

}
