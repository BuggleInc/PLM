package plm.test.simple.test;

import static org.junit.Assert.fail;

import org.junit.Test;

import plm.core.PLMCompilerException;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.lesson.Exercise.StudentOrCorrection;

public abstract class ScriptingSimpleExerciseTest extends SimpleExerciseTest {
	public ScriptingSimpleExerciseTest(ProgrammingLanguage pl) {
		super(pl);
	}

	@Test
	public void testBadCodeShouldCompil() {
		exo.getSourceFile(pl, 0).setBody(generateCompilationExceptionRisingCode());
		try {
			exo.compileAll(null, StudentOrCorrection.STUDENT);
		} catch (PLMCompilerException e) {
			e.printStackTrace();
		}
		if(exo.lastResult.compilationError!=null && !exo.lastResult.compilationError.equals("")) {
			fail(getClass().getName().replace("Test", "Entity") +" compilation should do nothing and not throw the following error:\n"+exo.lastResult.compilationError);
		}
	}
}
