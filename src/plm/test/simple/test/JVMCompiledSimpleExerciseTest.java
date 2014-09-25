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
	public void testBadJVMShouldNotCompil() throws PLMCompilerException {
		exo.getSourceFile(pl, 0).setBody(generateCompilationExceptionRisingCode());
		exo.compileAll(null, StudentOrCorrection.STUDENT);
	}
}
