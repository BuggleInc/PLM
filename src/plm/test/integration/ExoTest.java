package plm.test.integration;

import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import plm.core.PLMCompilerException;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.core.model.lesson.ExecutionProgress;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Lecture;
import plm.core.model.lesson.Lesson;
import plm.core.model.lesson.Exercise.StudentOrCorrection;
import plm.core.model.lesson.Exercise.WorldKind;
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
		"lessons.recursion.cons", "lessons.recursion", "lessons.recursion.hanoi",
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
		g.setLocale(new Locale("en"));
		
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
	
	/** Resets current world, populate it with the correction entity, and rerun it */
	private void testCorrectionEntity(ProgrammingLanguage lang) {
		Game.getInstance().setProgramingLanguage(lang);
		Game.getInstance().setCurrentExercise(exo);
		Game.getInstance().setProgramingLanguage(lang); // This stupid Game.setCurrentExercise tries to be cleaver if the current progLang is not avail in the requested exercise
		
		//System.out.flush();
		//System.err.println("Testing "+exo.getName()+" in "+lang.getLang()+" ");
		exo.lastResult = new ExecutionProgress();
		
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
		
		if (exo.lastResult.compilationError != null) 
				fail(exo.getId()+": compilation error: "+exo.lastResult.compilationError+". Compiled file:\n"+
						((exo.getSourceFileCount(lang)>0) ? (exo.getSourceFile(lang, 0).getCompilableContent(StudentOrCorrection.CORRECTION))
							                              : "none"));
		
		
		if (exo.lastResult.outcome != ExecutionProgress.outcomeKind.PASS) {
			//System.out.println(""+exo.getId()+" failed in "+Game.getProgrammingLanguage()+": "+exo.lastResult.details);
			fail(exo.getId()+": failed exercise ("+
				exo.lastResult.passedTests+"/"+exo.lastResult.totalTests+" passed): '"+exo.lastResult.executionError+"'");
		}
		//System.out.println(""+exo.getId()+" passed in "+Game.getProgrammingLanguage());
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
		
		Game.getInstance().setProgramingLanguage(Game.C);
		testCorrectionEntity(Game.C);
	}
	
	@Test(timeout=30000) // the well known python's "performance"...
	public void testPythonEntity() {
		if (!exo.getProgLanguages().contains(Game.PYTHON)) 
			fail("Exercise "+exo.getId()+" does not support python");	
		testCorrectionEntity(Game.PYTHON);
	}
}
