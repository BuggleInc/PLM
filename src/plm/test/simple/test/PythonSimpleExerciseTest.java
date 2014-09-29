package plm.test.simple.test;

import static org.junit.Assert.fail;

import org.junit.Test;

import plm.core.PLMCompilerException;
import plm.core.model.Game;
import plm.core.model.lesson.Exercise.StudentOrCorrection;
import plm.core.model.lesson.Exercise.WorldKind;
import plm.universe.Entity;
import plm.universe.World;

public class PythonSimpleExerciseTest extends ScriptingSimpleExerciseTest {

	public PythonSimpleExerciseTest() {
		super(Game.PYTHON);
	}

	@Override
	public String generateSyntaxErrorCode() {
		return "zqkdçajdé\"\"";
	}

	@Override
	public String generateVariableErrorCode() {
		return "toto++;\n";
	}

	@Override
	public String generateNullPointerErrorCode() {
		return "def run():\n  truc = None\n  print truc.toto";
	}

	@Override
	public String generateOutOfBoundsErrorCode() {
		return "def run():\n  tab = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]\n  print tab[42]";
	}

	@Test
	public void testSolutionShouldExecuteProperly() throws PLMCompilerException {
		exo.mutateEntities(WorldKind.CURRENT, StudentOrCorrection.CORRECTION);
		
		for (World w : exo.getWorlds(WorldKind.CURRENT)) {
			for (Entity ent: w.getEntities()) {
				pl.runEntity(ent,exo.lastResult);
			}
		}
		
		if(exo.lastResult.executionError!=null && !exo.lastResult.executionError.equals("")) {
			fail(getClass().getName().replace("Test", "Entity") +" should execute properly and not throw the following error:\n"+exo.lastResult.executionError);
		}
	}
}
