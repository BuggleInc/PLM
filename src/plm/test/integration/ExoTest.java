package plm.test.integration;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import plm.core.PLMCompilerException;
import plm.core.lang.ProgrammingLanguage;
import plm.core.log.LogHandler;
import plm.core.log.Logger;
import plm.core.model.Game;
import plm.core.model.lesson.ExecutionProgress;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Exercise.StudentOrCorrection;
import plm.core.model.lesson.Exercise.WorldKind;
import plm.core.model.lesson.Lecture;
import plm.core.model.lesson.Lesson;
import plm.universe.Entity;
import plm.universe.World;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

@RunWith(Parameterized.class)
public class ExoTest {

	static private Game g;

	@BeforeClass
	static public void setUpClass() {
	}
	
	/* Generate the list of parameters we want to run our test on */
	@Parameters
	static public Collection<Object[]> exercises() {
		List<Object[]> result = new LinkedList<Object[]>();
		String userUUID = UUID.randomUUID().toString();
		g = new Game(userUUID, mock(LogHandler.class), new Locale("en"), Game.JAVA.getLang(), false);
		g.getProgressSpyListeners().clear(); // disable all progress spies (git, etc)
		g.removeSessionKit();
		g.setBatchExecution();
		
		Set<Lecture> alreadySeenExercises = new HashSet<Lecture>();  
		for (String lessonName : Game.lessonsName) { 
			if(g.switchLesson(lessonName, true)==null) {
				System.err.println("Warning, I tried to load "+lessonName+" but something went wrong... Please fix it before running this test again.");
				System.exit(1);
			}
			Logger.log("Lesson "+lessonName+" loaded ("+g.getCurrentLesson().getExerciseCount()+" exercises)");
			if (g.getCurrentLesson().getExerciseCount() == 0) {
				System.err.println("Cannot find any exercise in "+lessonName+". Something's wrong here");
				System.exit(1);
			}
			for (Lecture l : g.getCurrentLesson().exercises()) 
				if (l instanceof Exercise) {
					result.add(new Object[] { g.getCurrentLesson(), l });
					if (alreadySeenExercises.contains(l)) {
						System.err.println("Warning, I tried to add the exercise "+l.getName()+" twice. Something's wrong here");
						System.exit(1);
					}
					alreadySeenExercises.add(l);
				}
		}
		Logger.log("There is currently "+result.size()+" exercises in our database. Yes sir.");
		Logger.log("");
		
		//g.switchDebug();		
		g.setLocale(new Locale("en"));
		
		return result;
	}

	private Exercise exo;

	public ExoTest(Lesson l, Exercise e) {
		this.exo = e;
		g.setCurrentLesson(l);
		g.setCurrentExercise(exo);
	
		// disable delay on world execution
		for (int worldRank=0; worldRank < exo.getWorldCount(); worldRank++) {
			exo.setNbError(-1);
			exo.getWorlds(WorldKind.INITIAL).get(worldRank).setDelay(0);
		}
	}
	
	/** Try to run the solution, fail if it's missing **/
	private void testCorrectionEntityExists(ProgrammingLanguage lang) {
		// FIXME: Re-implement me
	}
	
	/** Resets current world, populate it with the correction entity, and rerun it */
	private void testCorrectionEntity(ProgrammingLanguage lang) {
		// FIXME: Re-implement me
	}
	
	@Test(timeout=10000)
	public void testJavaEntityExists() {
		testCorrectionEntityExists(Game.JAVA);
	}
	
	@Test(timeout=30000) // The compiler sometimes takes time to kick in 
	public void testScalaEntityExists() {
		if (!exo.getProgLanguages().contains(Game.SCALA)) 
			fail("Exercise "+exo.getId()+" does not support scala");
		testCorrectionEntityExists(Game.SCALA);
	}
	
//	@Test(timeout=30000) // The compiler sometimes takes time to kick in 
	public void testCEntityExists() {
		if (!exo.getProgLanguages().contains(Game.C)) 
			fail("Exercise "+exo.getId()+" does not support C");
		testCorrectionEntityExists(Game.C);
	}
	
	@Test(timeout=30000) // the well known python's "performance"...
	public void testPythonEntityExists() {
		if (!exo.getProgLanguages().contains(Game.PYTHON)) 
			fail("Exercise "+exo.getId()+" does not support python");
		testCorrectionEntityExists(Game.PYTHON);
	}
	
	@Test(timeout=10000)
	public void testJavaEntity() {
		testCorrectionEntity(Game.JAVA);
	}
	
	@Test(timeout=30000) // The compiler sometimes takes time to kick in 
	public void testScalaEntity() {
		if (!exo.getProgLanguages().contains(Game.SCALA)) 
			fail("Exercise "+exo.getId()+" does not support scala");
		testCorrectionEntity(Game.SCALA);
	}
	
//	@Test(timeout=30000) // The compiler sometimes takes time to kick in 
	public void testCEntity() {
		if (!exo.getProgLanguages().contains(Game.C)) 
			fail("Exercise "+exo.getId()+" does not support C");
		testCorrectionEntity(Game.C);
	}
	
	@Test(timeout=30000) // the well known python's "performance"...
	public void testPythonEntity() {
		if (!exo.getProgLanguages().contains(Game.PYTHON)) 
			fail("Exercise "+exo.getId()+" does not support python");
		testCorrectionEntity(Game.PYTHON);
	}
}
