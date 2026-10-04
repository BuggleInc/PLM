package plm.test.simple.test;

import java.util.ArrayList;
import java.util.concurrent.Future;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import plm.core.PLMCompilerException;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.BrokenProgrammingLanguageException;
import plm.core.model.lesson.Exercise.StudentOrCorrection;
import plm.core.model.lesson.Exercise.WorldKind;
import plm.core.model.lesson.RunOutcome;

/**
 * Somewhat misnamed class that factorize some code between the tests of this directory.
 */
public abstract class CompiledSimpleExerciseTest extends SimpleExerciseTest {

  public CompiledSimpleExerciseTest(ProgrammingLanguage pl) throws BrokenProgrammingLanguageException { super(pl); }

  @Test public void testSolutionShouldPass() throws PLMCompilerException, InterruptedException
  {
    String executable = exo.compile(null, StudentOrCorrection.CORRECTION, pl);

    exo.runAll(WorldKind.CURRENT, new ArrayList<Future<?>>(), exo.lastResult, pl, executable);

    if (exo.lastResult.outcome != RunOutcome.kind.PASS) {
      Assertions.fail(getClass().getName().replace("Test", "Entity") + " should pass the exercise but the outcoume is " + exo.lastResult.outcome.toString());
    }
  }

  @Test public void testSolutionShouldExecuteProperly() throws PLMCompilerException, InterruptedException
  {
    String executable = exo.compile(null, StudentOrCorrection.CORRECTION, pl);

    exo.runAll(WorldKind.CURRENT, new ArrayList<Future<?>>(), exo.lastResult, pl, executable);

    if (exo.lastResult.executionError != null && !exo.lastResult.executionError.equals("")) {
      Assertions.fail(getClass().getName().replace("Test", "Entity") + " should execute properly and not throw the following error:\n" +
                      exo.lastResult.executionError);
    }
  }

  @Test public void testSyntaxErrorRisingCodeShouldNotCompil() throws PLMCompilerException
  {
    Assertions.assertThrows(PLMCompilerException.class, () -> {
      exo.getSourceFile(pl, 0).setEditorContent(generateSyntaxErrorCode(), pl);
      exo.compile(null, StudentOrCorrection.STUDENT, pl);
    });
  }

  @Test public void testVariableErrorRisingCodeShouldNotCompil() throws PLMCompilerException
  {
    Assertions.assertThrows(PLMCompilerException.class, () -> {
      exo.getSourceFile(pl, 0).setEditorContent(generateVariableErrorCode(), pl);
      exo.compile(null, StudentOrCorrection.STUDENT, pl);
    });
  }
}
