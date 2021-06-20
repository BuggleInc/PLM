package plm.test.integration;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import plm.core.PLMCompilerException;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.DemoRunner;
import plm.core.model.Game;
import plm.core.model.lesson.ExecutionProgress;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Exercise.StudentOrCorrection;
import plm.core.model.lesson.Exercise.WorldKind;
import plm.core.model.lesson.Lecture;
import plm.core.model.lesson.Lesson;
import plm.core.utils.FileUtils;
import plm.universe.Entity;
import plm.universe.World;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

@RunWith(Parameterized.class)
public class ExoTest {

	static private String[] lessonNamesToTest = new String[] { // WARNING, keep ChooseLessonDialog.lessons synchronized
		"lessons.welcome", "lessons.turmites", "lessons.maze", "lessons.turtleart",
		"lessons.sort.basic", "lessons.sort.dutchflag", "lessons.sort.baseball", "lessons.sort.pancake", 
		"lessons.recursion.cons", "lessons.recursion.lego", "lessons.recursion.hanoi",
		// "lessons.lightbot", // Well, testing this requires testing the swing directly I guess
		"lessons.bat.string1", "lessons.lander",
		};

	@BeforeClass
	static public void setUpClass() {
	}
	
	/* Generate the list of parameters we want to run our test on */
	@Parameters
	static public Collection<Object[]> exercises() {
		List<Object[]> result = new LinkedList<Object[]>();
		
		FileUtils.setLocale(new Locale("en"));
		Game g = Game.getInstance();
		g.getProgressSpyListeners().clear(); // disable all progress spies (git, etc)
		g.removeSessionKit();
		g.setBatchExecution();

		/* Compute the answers with the java entities */
		Game.getInstance().setProgramingLanguage(Game.JAVA);
		
		Set<Lecture> alreadySeenExercises = new HashSet<Lecture>();  
		for (String lessonName : lessonNamesToTest) { 
			if(g.switchLesson(lessonName, true)==null) {
				System.err.println("Warning, I tried to load "+lessonName+" but something went wrong... Please fix it before running this test again.");
				System.exit(1);
			}
			System.out.println("Lesson "+lessonName+" loaded ("+g.getCurrentLesson().getExerciseCount()+" exercises)");
			if (g.getCurrentLesson().getExerciseCount() == 0) {
				System.err.println("Cannot find any exercise in "+lessonName+". Something's wrong here");
				System.exit(1);
			}
			for (Lecture l : g.getCurrentLesson().exercises()) 
				if (l instanceof Exercise) {
					result.add(new Object[] { Game.getInstance().getCurrentLesson(), l });
					if (alreadySeenExercises.contains(l)) {
						System.err.println("Warning, I tried to add the exercise "+l.getName()+" twice. Something's wrong here");
						System.exit(1);
					}
					alreadySeenExercises.add(l);
				}
		}
		System.out.println("There is currently "+result.size()+" exercises in our database. Yes sir.");
		System.out.println("");
		
		//g.switchDebug();		
		g.setLocale(new Locale("en")); // Test the templates in English, to catch translation issues
		
		return result;
	}

	private Exercise exo;

	public ExoTest(Lesson l, Exercise e) {
		this.exo = e;
		Game.getInstance().setCurrentLesson(l);
		Game.getInstance().setCurrentExercise(exo);
	
		// disable delay on world execution
		for (int worldRank=0; worldRank < exo.getWorldCount(); worldRank++) {
			exo.getWorlds(WorldKind.INITIAL).get(worldRank).setDelay(0);
		}
	}
	
	/** Try to run the solution, fail if it's missing **/
	private void testCorrectionEntityExists(ProgrammingLanguage lang) {
		Game.getInstance().setProgramingLanguage(lang);
		
		DemoRunner demoRunner = new DemoRunner(Game.getInstance(), new ArrayList<Thread>());
		
		exo.lastResult = new ExecutionProgress();
		try {
			demoRunner.runDemo(exo);
		}
		catch(Exception e) {
			e.printStackTrace();
			fail(exo.getId()+"'s solution failed to run...");
		}
	}
	
	/** Resets current world, populate it with the correction entity, and rerun it */
	private void testCorrectionEntity(ProgrammingLanguage lang) {
		Game.getInstance().setProgramingLanguage(lang);
		
		exo.lastResult = new ExecutionProgress();
		System.err.println("Test exo "+exo.getName()+" in "+lang);
		try {
			exo.compileAll(null, StudentOrCorrection.CORRECTION);
			if (exo.lastResult.compilationError != null && ! exo.lastResult.compilationError.equals(""))
				fail(exo.getId()+": compilation error: " + exo.lastResult.compilationError);

			exo.reset();
			// For compiled languages, we mutate to the compiled entity. 
			// For script languages, we mutate to the correction entity.
			StudentOrCorrection what = StudentOrCorrection.CORRECTION;
			if (lang == Game.JAVA || lang == Game.SCALA || lang == Game.C)
				what = StudentOrCorrection.STUDENT;
			exo.mutateEntities(WorldKind.CURRENT, what);
			
			if (exo instanceof BatExercise)
				for (BatTest t : ((BatWorld)exo.getWorld(0)).tests) 
					t.objectiveTest = false; // we want to set the result for real, not the expected
			
			for (World w : exo.getWorlds(WorldKind.CURRENT)) 
				for (Entity ent: w.getEntities())  
					lang.runEntity(ent,exo.lastResult);
			
			exo.check();
		} catch (PLMCompilerException e) {
			// compileAll already setup the error message; we just needed to not run the entity in that case
		}
		
		if (exo.lastResult.compilationError != null) {
			String msg = exo.getId()+": compilation error: "+exo.lastResult.compilationError+". Compiled file:\n"+
					       ((exo.getSourceFileCount(lang)>0) ? (exo.getSourceFile(lang, 0).getCompilableContent(StudentOrCorrection.CORRECTION))
													  : "none");
			System.err.println(msg);									  
			fail(msg);
		}
		
		
		if (exo.lastResult.outcome != ExecutionProgress.outcomeKind.PASS) {
			String msg = "Test of "+exo.getId()+" failed ("+
				exo.lastResult.passedTests+"/"+exo.lastResult.totalTests+" passed): '"+exo.lastResult.executionError+"'";
			System.err.println(msg);									  
			for (int wnum = 0; wnum < exo.getWorldCount(); wnum++)
				try {
					String name = "FailingWorld-"+exo.getId()+"-World"+wnum+".txt";
					exo.getWorld(0).writeToFile(new File(name));
					System.err.println(" World "+wnum+" dumped to "+name);
				} catch (IOException e) {
					System.err.println("Cannot write the bugged world to disk because of the following exception");
					e.printStackTrace();
				}
			fail(msg);				
		}
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
	
	// FIXME: Test this!
	//@Test(timeout=30000) // The compiler sometimes takes time to kick in 
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
