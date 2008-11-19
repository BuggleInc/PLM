package jlm.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

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

	private final static File PROPERTY_FILE = new File(System.getProperty("user.home")+File.separator+".jlm", "jlm.properties");

	private static Properties gameProperties = new Properties();

	private static Game instance = null;
	private ArrayList<Lesson> lessons = new ArrayList<Lesson>();
	private Lesson currentLesson;
	private ArrayList<GameListener> listeners = new ArrayList<GameListener>();
	private World selectedWorld;
	private World answerOfSelectedWorld;
	private Entity selectedEntity;
	private Vector<Thread> lessonRunners = new Vector<Thread>();
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
		/* Add all the lessons we know */
		addLesson(new lessons.welcome.Main());
		addLesson(new lessons.maze.Main());
		addLesson(new lessons.turtles.Main());
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
		setState(GameState.EXECUTION_ENDED);
	}

	public void startExerciseDemoExecution() {
		DemoRunner runner = new DemoRunner(Game.getInstance(), new Vector<Thread>());
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
		if (!getProperties().getProperty("output.capture","false").equals("true")) {
			Logger l  = new Logger(outputWriter);
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
		if (Game.PROPERTY_FILE.exists()) {
			FileInputStream fi = null;
			try {
				fi = new FileInputStream(Game.PROPERTY_FILE);
				Game.gameProperties.load(fi);
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
		}
	}

	public static void storeProperties() {
		FileOutputStream fo = null;
		try {
			File saveDir = new File(System.getProperty("user.home")+File.separator+".jlm");
			if (! saveDir.exists()) {
				if (! saveDir.mkdir()) {
					Logger.log("Game:storeProperties", "cannot create properties store directory");
				};
			}
			fo = new FileOutputStream(Game.PROPERTY_FILE);
			Game.gameProperties.store(fo, "Java Learning Machine properties");
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

	public static Properties getProperties() {
		return Game.gameProperties;
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
