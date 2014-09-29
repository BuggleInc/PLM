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
