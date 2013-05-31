package jlm.core.model;

import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Locale;

import jlm.core.model.lesson.ExecutionProgress;
import jlm.core.model.lesson.Exercise;
import jlm.core.model.lesson.Exercise.WorldKind;
import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lecture;
import jlm.core.model.lesson.Lesson;
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
	static private String[] lessons = new String[] { "lessons.welcome" };
	
	@BeforeClass
	static public void setUpClass() {
	}
	
	/* Generate the list of parameters we want to run our test on */
	@Parameters
	static public Collection<Object[]> data() {
		LinkedList<Object[]> result = new LinkedList<Object[]>();
		
		FileUtils.setLocale(new Locale("en"));
		Game g = Game.getInstance();
		
		g.switchDebug();

		/* Compute the answers with the java entities */
		Game.getInstance().setProgramingLanguage(Game.JAVA);
		for (String lessonName : lessons) { 
			System.err.println("Loading lesson "+lessonName);
			g.switchLesson(lessonName);
			System.err.println("Lesson "+lessonName+" loaded");
			
			for (Lecture l : g.getCurrentLesson().exercises()) 
				if (l instanceof Exercise)
					result.add(new Object[] {Game.getInstance().getCurrentLesson(), l});
		}
		
		return result;
	}

	
	private ExerciseTemplated exo;
	public ExoTest(Lesson l, ExerciseTemplated e) {
		this.exo = e;

		Game.getInstance().setCurrentLesson(l);
		Game.getInstance().setCurrentExercise(exo);
		for (int worldRank=0; worldRank<exo.getWorldCount(); worldRank++) 
			exo.getWorlds(WorldKind.INITIAL).get(worldRank).setDelay(0);
	}
	
	/** Resets current world, populate it with the correction entity, and rerun it */
	private void testCorrectionEntity() {
		Game.getInstance().setCurrentExercise(exo);
		exo.lastResult = new ExecutionProgress();
		
		exo.reset();
		exo.mutateCorrection(WorldKind.CURRENT);
		
		if (exo instanceof BatExercise)
			for (BatTest t : ((BatWorld)exo.getWorld(0)).tests) 
				t.objectiveTest = false; // we want to set the result for real, not the expected
		
		for (World w : exo.getWorlds(WorldKind.CURRENT)) 
			for (Entity ent: w.getEntities())  
				ent.runIt(exo.lastResult);

		exo.check();
		
		if (exo.lastResult.compilationError != null)
			fail(exo.getClass().getSimpleName()+": compilation error: "+exo.lastResult.compilationError);
		
		if (exo.lastResult.totalTests == 0 
				|| exo.lastResult.totalTests != exo.lastResult.passedTests 
				|| !exo.lastResult.details.equals("")) 
			fail(exo.getClass().getSimpleName()+": failed exercise ("+
				exo.lastResult.passedTests+"/"+exo.lastResult.totalTests+" passed): '"+exo.lastResult.details+"'");

	}
	
	@Test
	public void testJavaEntity() {
		Game.getInstance().setProgramingLanguage(Game.JAVA);
		testCorrectionEntity();
	}
	
	@Test
	public void testPythonEntity() {
		if (!exo.getProgLanguages().contains(Game.PYTHON)) {
			System.out.println("Exercise "+exo.getClass().getName()+" does not support python");
			return;
		}
		
		Game.getInstance().setProgramingLanguage(Game.PYTHON);
		testCorrectionEntity();
	}
}
