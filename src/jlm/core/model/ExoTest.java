package jlm.core.model;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Locale;

import jlm.core.model.lesson.Exercise;
import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lecture;
import jlm.core.model.lesson.Lesson;
import jlm.core.model.lesson.Exercise.WorldKind;
import jlm.universe.Entity;
import jlm.universe.World;
import jlm.universe.bat.BatExercise;

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
			exo.getInitialWorldList().get(worldRank).setDelay(0);
	}
	
	/** Resets current world, populate it with the correction entity, and rerun it */
	private void testCorrectionEntity() {
		Game.getInstance().setCurrentExercise(exo);
		
		exo.reset();
		if (!(exo instanceof BatExercise))
			exo.mutateCorrection(WorldKind.CURRENT);
		for (World w : exo.getCurrentWorldList()) 
			for (Entity ent: w.getEntities()) 
				ent.runIt();
		
		for (int worldRank=0; worldRank<exo.getWorldCount(); worldRank++) {
			World current = exo.getCurrentWorldList().get(worldRank);
			World answer  = exo.getAnswerWorldList().get(worldRank);
			
			if (!current.equals(answer))
				fail(exo.getClass().getSimpleName()+":world["+worldRank+"] differs in "+
						Game.getProgrammingLanguage()+":\n"+answer.diffTo(current));
		}		
	}
	
	@Test
	public void testJavaEntity() {
		Game.getInstance().setProgramingLanguage(Game.JAVA);
		testCorrectionEntity();
	}
	
	@Test
	public void testPythonEntity() {
		if (!exo.getProgLanguages().contains(Game.PYTHON)) 
			fail("Exercise "+exo.getClass().getName()+" does not support python");
		
		Game.getInstance().setProgramingLanguage(Game.PYTHON);
		testCorrectionEntity();
	}
}
