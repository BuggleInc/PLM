package plm.test.simple.test;

import static org.junit.Assert.fail;

import org.junit.Test;

import plm.core.PLMCompilerException;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.core.model.lesson.ExecutionProgress;
import plm.core.model.lesson.Exercise.StudentOrCorrection;
import plm.core.model.lesson.Exercise.WorldKind;
import plm.universe.Entity;
import plm.universe.World;

public abstract class ScriptingSimpleExerciseTest extends SimpleExerciseTest {
	public ScriptingSimpleExerciseTest(Game game, ProgrammingLanguage pl) {
		super(game, pl);
	}
	
	@Test
	public void testSolutionShouldExecuteProperly() throws PLMCompilerException {
		exo.setNbError(-1);
		exo.mutateEntities(WorldKind.CURRENT, StudentOrCorrection.CORRECTION);
		
		for (World w : exo.getWorlds(WorldKind.CURRENT)) {
			for (Entity ent: w.getEntities()) {
				pl.runEntity(ent,exo.lastResult, getGame().i18n);
			}
		}
		
		if(exo.lastResult.executionError!=null && !exo.lastResult.executionError.equals("")) {
			fail(getClass().getName().replace("Test", "Entity") +" should execute properly and not throw the following error:\n"+exo.lastResult.executionError);
		}
	}

	@Test
	public void testSolutionShouldPass() throws PLMCompilerException {
		exo.setNbError(-1);
		exo.mutateEntities(WorldKind.CURRENT, StudentOrCorrection.CORRECTION);
		
		for (World w : exo.getWorlds(WorldKind.CURRENT)) {
			for (Entity ent: w.getEntities()) {
				pl.runEntity(ent,exo.lastResult, getGame().i18n);
			}
		}
		
		if(exo.lastResult.outcome != ExecutionProgress.outcomeKind.PASS) {
			fail(getClass().getName().replace("Test", "Entity") +" should pass the exercise...");
		}
	}
	
	@Test
	public void testVariableErrorRisingCodeShouldCompil() throws PLMCompilerException {
		exo.getSourceFile(pl, 0).setBody(generateVariableErrorCode());
		exo.compileAll(null, StudentOrCorrection.STUDENT);
		if(exo.lastResult.compilationError!=null && !exo.lastResult.compilationError.equals("")) {
			fail(getClass().getName().replace("Test", "Entity") +" compilation should do nothing and not throw the following error:\n"+exo.lastResult.compilationError);
		}
	}
	
	@Test
	public void testSyntaxErrorRisingCodeShouldCompil() throws PLMCompilerException  {
		exo.getSourceFile(pl, 0).setBody(generateSyntaxErrorCode());
		exo.compileAll(null, StudentOrCorrection.STUDENT);
		if(exo.lastResult.compilationError!=null && !exo.lastResult.compilationError.equals("")) {
			fail(getClass().getName().replace("Test", "Entity") +" compilation should do nothing and not throw the following error:\n"+exo.lastResult.compilationError);
		}
	}
	
}
