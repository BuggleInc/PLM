package plm.test.integration;

import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import plm.core.lang.ProgrammingLanguage;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Exercise.WorldKind;

@RunWith(Parameterized.class)
public class ExoTest {


	@BeforeClass
	static public void setUpClass() {
	}
	
	/* Generate the list of parameters we want to run our test on */
	@Parameters
	static public Collection<Object[]> exercises() {
		List<Object[]> result = new LinkedList<Object[]>();
		/*
		String userUUID = UUID.randomUUID().toString();
		g = new Game(userUUID, mock(LogHandler.class), new Locale("en"), Game.JAVA.getLang(), false);
		g.getProgressSpyListeners().clear(); // disable all progress spies (git, etc)
		g.setBatchExecution();
		
		Set<Exercise> alreadySeenExercises = new HashSet<Exercise>();  
		for (String lessonName : Game.lessonsName) { 
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
		*/
		return result;
	}

	private Exercise exo;

	public ExoTest(Exercise e) {
		this.exo = e;
	
		// disable delay on world execution
		for (int worldRank=0; worldRank < exo.getWorldCount(); worldRank++) {
			exo.setNbError(-1);
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
		testCorrectionEntityExists(ProgrammingLanguage.getProgrammingLanguage("Java"));
	}
	
	@Test(timeout=30000) // The compiler sometimes takes time to kick in 
	public void testScalaEntityExists() {
		if (!exo.getProgLanguages().contains(ProgrammingLanguage.getProgrammingLanguage("Scala"))) 
			fail("Exercise "+exo.getId()+" does not support scala");
		testCorrectionEntityExists(ProgrammingLanguage.getProgrammingLanguage("Scala"));
	}
	
//	@Test(timeout=30000) // The compiler sometimes takes time to kick in 
	public void testCEntityExists() {
		if (!exo.getProgLanguages().contains(ProgrammingLanguage.getProgrammingLanguage("C"))) 
			fail("Exercise "+exo.getId()+" does not support C");
		testCorrectionEntityExists(ProgrammingLanguage.getProgrammingLanguage("C"));
	}
	
	@Test(timeout=30000) // the well known python's "performance"...
	public void testPythonEntityExists() {
		if (!exo.getProgLanguages().contains(ProgrammingLanguage.getProgrammingLanguage("Python"))) 
			fail("Exercise "+exo.getId()+" does not support python");
		testCorrectionEntityExists(ProgrammingLanguage.getProgrammingLanguage("Python"));
	}
	
	@Test(timeout=10000)
	public void testJavaEntity() {
		testCorrectionEntity(ProgrammingLanguage.getProgrammingLanguage("Java"));
	}
	
	@Test(timeout=30000) // The compiler sometimes takes time to kick in 
	public void testScalaEntity() {
		if (!exo.getProgLanguages().contains(ProgrammingLanguage.getProgrammingLanguage("Scala"))) 
			fail("Exercise "+exo.getId()+" does not support scala");
		testCorrectionEntity(ProgrammingLanguage.getProgrammingLanguage("Scala"));
	}
	
//	@Test(timeout=30000) // The compiler sometimes takes time to kick in 
	public void testCEntity() {
		if (!exo.getProgLanguages().contains(ProgrammingLanguage.getProgrammingLanguage("C"))) 
			fail("Exercise "+exo.getId()+" does not support C");
		testCorrectionEntity(ProgrammingLanguage.getProgrammingLanguage("C"));
	}
	
	@Test(timeout=30000) // the well known python's "performance"...
	public void testPythonEntity() {
		if (!exo.getProgLanguages().contains(ProgrammingLanguage.getProgrammingLanguage("Python"))) 
			fail("Exercise "+exo.getId()+" does not support python");
		testCorrectionEntity(ProgrammingLanguage.getProgrammingLanguage("Python"));
	}
}
