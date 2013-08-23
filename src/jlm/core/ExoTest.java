package jlm.core;

import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Set;

import jlm.core.model.Game;
import jlm.core.model.ProgrammingLanguage;
import jlm.core.model.lesson.ExecutionProgress;
import jlm.core.model.lesson.Exercise;
import jlm.core.model.lesson.Exercise.StudentOrCorrection;
import jlm.core.model.lesson.Exercise.WorldKind;
import jlm.core.model.lesson.Lecture;
import jlm.core.model.lesson.Lesson;
import jlm.core.utils.FileUtils;
import jlm.universe.Entity;
import jlm.universe.World;
import jlm.universe.bat.BatExercise;
import jlm.universe.bat.BatTest;
import jlm.universe.bat.BatWorld;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ExoTest {
	static private String[] lessons = new String[] { 
		"lessons.welcome", "lessons.turmites", "lessons.maze","lessons.bat.string1", 
		"lessons.sort", "lessons.sort.baseball", "lessons.sort.pancake",  
		"lessons.recursion", "lessons.recursion.hanoi",
		// "lessons.lightbot", // Well, testing this requires testing the swing directly I guess
		};

	@BeforeClass
	static public void setUpClass() {
	}
	
	/* Generate the list of parameters we want to run our test on */
	@Parameters
	static public Collection<Object[]> data() {
		LinkedList<Object[]> result = new LinkedList<Object[]>();
		
		FileUtils.setLocale(new Locale("en"));
		Game g = Game.getInstance();

		/* Compute the answers with the java entities */
		Game.getInstance().setProgramingLanguage(Game.JAVA);
		Set<Lecture> allExercises = new HashSet<Lecture>();  
		for (String lessonName : lessons) { 
			g.switchLesson(lessonName,true);
			System.out.println("Lesson "+lessonName+" loaded ("+g.getCurrentLesson().getExerciseCount()+" exercises)");
			if (g.getCurrentLesson().getExerciseCount() == 0) {
				System.err.println("Cannot find any exercise in "+lessonName+". Something's wrong here");
				System.exit(1);
			}
			for (Lecture l : g.getCurrentLesson().exercises()) 
				if (l instanceof Exercise) {
					result.add(new Object[] {Game.getInstance().getCurrentLesson(), l});
					if (allExercises.contains(l)) {
						System.err.println("Warning, I tried to add the exercise "+l.getName()+" twice. Something's wrong here");
						System.exit(1);
					}
					allExercises.add(l);
				}
		}
		System.out.println("There is currently "+result.size()+" exercises in our database. Yes sir.");
		g.switchDebug();
		g.removeSessionKit();
		
		g.setLocale(new Locale("en"));
		return result;
	}

	
	private Exercise exo;
	public ExoTest(Lesson l, Exercise e) {
		this.exo = e;

		Game.getInstance().setCurrentLesson(l);
		Game.getInstance().setCurrentExercise(exo);
		for (int worldRank=0; worldRank<exo.getWorldCount(); worldRank++) 
			exo.getWorlds(WorldKind.INITIAL).get(worldRank).setDelay(0);
	}
	
	/** Resets current world, populate it with the correction entity, and rerun it */
	private void testCorrectionEntity() {
		ProgrammingLanguage lang = Game.getProgrammingLanguage();
		Game.getInstance().setCurrentExercise(exo);
		Game.getInstance().setProgramingLanguage(lang); // This stupid Game.setCurrentExercise tries to be cleaver if the current progLang is not avail in the requested exercise
		
		System.out.flush();
		System.err.println("XXXXXXX "+exo.getName()+" in "+lang.getLang()+" XXXXXXX");
		exo.lastResult = new ExecutionProgress();
		
		try {
			exo.compileAll(null,StudentOrCorrection.CORRECTION);
			if (exo.lastResult.compilationError != null)
				fail(exo.getId()+": compilation error: "+exo.lastResult.compilationError);

			exo.reset();
			// For compiled languages, we mutate to the compiled entity. 
			// For script languages, we mutate to the correction entity.
			StudentOrCorrection what = StudentOrCorrection.CORRECTION;
			if (lang == Game.JAVA || lang == Game.SCALA)
				what = StudentOrCorrection.STUDENT;
			exo.mutateEntities(WorldKind.CURRENT, what);
			
			if (exo instanceof BatExercise)
				for (BatTest t : ((BatWorld)exo.getWorld(0)).tests) 
					t.objectiveTest = false; // we want to set the result for real, not the expected
			
			for (World w : exo.getWorlds(WorldKind.CURRENT)) 
				for (Entity ent: w.getEntities())  
					ent.runIt(exo.lastResult);
			
			exo.check();
		} catch (JLMCompilerException e) {
			// compileAll already setup the error message; we just needed to not run the entity in that case
		}
		
		if (exo.lastResult.compilationError != null) 
				fail(exo.getId()+": compilation error: "+exo.lastResult.compilationError+". Compiled file:\n"+
						((exo.getSourceFileCount(lang)>0) ? (exo.getSourceFile(lang, 0).getCompilableContent(StudentOrCorrection.CORRECTION))
							                              : "none"));
		
		
		if (exo.lastResult.totalTests == 0 
				|| exo.lastResult.totalTests != exo.lastResult.passedTests 
				|| !exo.lastResult.details.equals("")) {
			System.out.println(""+exo.getId()+" failed in "+Game.getProgrammingLanguage()+": "+exo.lastResult.details);
			fail(exo.getId()+": failed exercise ("+
				exo.lastResult.passedTests+"/"+exo.lastResult.totalTests+" passed): '"+exo.lastResult.details+"'");
		}
		System.out.println(""+exo.getId()+" passed in "+Game.getProgrammingLanguage());

	}
	
	@Test(timeout=10000)
	public void testJavaEntity() {
		Game.getInstance().setProgramingLanguage(Game.JAVA);
		testCorrectionEntity();
	}
	@Test(timeout=30000) // The compiler sometimes takes time to kick in 
	public void testScalaEntity() {
		if (!exo.getProgLanguages().contains(Game.SCALA)) 
			fail("Exercise "+exo.getId()+" does not support scala");
		
		Game.getInstance().setProgramingLanguage(Game.SCALA);
		testCorrectionEntity();
	}
	
	@Test(timeout=30000) // the well known python's "performance"...
	public void testPythonEntity() {
		if (!exo.getProgLanguages().contains(Game.PYTHON)) 
			fail("Exercise "+exo.getId()+" does not support python");
	
		Game.getInstance().setProgramingLanguage(Game.PYTHON);
		testCorrectionEntity();
	}
}
