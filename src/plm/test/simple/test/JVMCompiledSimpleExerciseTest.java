package plm.test.simple.test;

import org.junit.Test;

import plm.core.PLMCompilerException;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.lesson.Exercise.StudentOrCorrection;

public abstract class JVMCompiledSimpleExerciseTest extends SimpleExerciseTest {
	
	public JVMCompiledSimpleExerciseTest(ProgrammingLanguage pl) {
		super(pl);
	}

	@Test (expected = PLMCompilerException.class)
	public void testSyntaxErrorRisingCodeShouldNotCompil() throws PLMCompilerException {
		exo.getSourceFile(pl, 0).setBody(generateSyntaxErrorCode());
		exo.compileAll(null, StudentOrCorrection.STUDENT);
	}
	
	@Test (expected = PLMCompilerException.class)
	public void testVariableErrorRisingCodeShouldNotCompil() throws PLMCompilerException {
		exo.getSourceFile(pl, 0).setBody(generateVariableErrorCode());
		exo.compileAll(null, StudentOrCorrection.STUDENT);
	}
}
