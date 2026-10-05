package plm.test.simple.test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.concurrent.Future;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import plm.core.PLMCompilerException;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.BrokenProgrammingLanguageException;
import plm.core.model.Game;
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

  /** Debugging keeps the lines of the generated source, so it is switched off where the lines of the editor are checked. */
  protected static void setDebug(boolean enabled)
  {
    if (Game.getInstance().isDebugEnabled() != enabled)
      Game.getInstance().switchDebug();
  }

  /** How a stack trace designates a line of the entity. */
  protected String locationOfLine(int line) { return "(Entity." + pl.getExt() + ":" + line + ")"; }

  /** The frames of a stack trace are reported at their line in the editor, not in the generated source, whatever the length of the template before it. */
  @Test public void testStackTraceLineMatchesEditor() throws PLMCompilerException, InterruptedException
  {
    PrintStream realErr           = System.err;
    ByteArrayOutputStream capture = new ByteArrayOutputStream();
    setDebug(false);
    System.setErr(new PrintStream(capture, true));
    try {
      exo.getSourceFile(pl, 0).setEditorContent(generateExceptionRaisingCode(), pl);
      String executable = exo.compile(null, StudentOrCorrection.STUDENT, pl);
      exo.runAll(WorldKind.CURRENT, new ArrayList<Future<?>>(), exo.lastResult, pl, executable);
    } finally {
      System.setErr(realErr);
      setDebug(true);
    }
    Assertions.assertTrue(capture.toString().contains(locationOfLine(2)), capture.toString());
  }

  /** Runs the given editor content, which needs what the entity declares in its IMPORT sections, and expects it to pass. */
  protected void assertPassesThanksToImports(String code) throws PLMCompilerException, InterruptedException
  {
    exo.getSourceFile(pl, 0).setEditorContent(code, pl);
    String executable = exo.compile(null, StudentOrCorrection.STUDENT, pl);
    exo.runAll(WorldKind.CURRENT, new ArrayList<Future<?>>(), exo.lastResult, pl, executable);
    Assertions.assertEquals(RunOutcome.kind.PASS, exo.lastResult.outcome, exo.lastResult.executionError);
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
