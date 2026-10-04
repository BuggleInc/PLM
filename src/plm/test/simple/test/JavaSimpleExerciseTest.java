package plm.test.simple.test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.concurrent.Future;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import plm.core.PLMCompilerException;
import plm.core.model.BrokenProgrammingLanguageException;
import plm.core.model.Game;
import plm.core.model.lesson.Exercise.StudentOrCorrection;
import plm.core.model.lesson.Exercise.WorldKind;

public class JavaSimpleExerciseTest extends CompiledSimpleExerciseTest {

  public JavaSimpleExerciseTest() throws BrokenProgrammingLanguageException { super(Game.getInstance().programmingLanguageManager.JAVA); }

  @Override public String generateSyntaxErrorCode() { return "zqkdçajdé\"\""; }

  @Override public String generateVariableErrorCode() { return "toto++;\n"; }

  @Override public String generateNullPointerErrorCode()
  {
    return "public void run() {\n"
        + "    String s = null;\n"
        + "    System.out.println(s.length());\n"
        + "}";
  }

  @Override public String generateOutOfBoundsErrorCode()
  {
    return "public void run() {\n"
        + "    int t[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};\n"
        + "    System.out.println(t[42]);\n"
        + "}";
  }

  @Override public String generateWrongCode()
  {
    return "public void run() {\n"
        + "    setObjectif(false);\n"
        + "}";
  }

  @Override public String generateSolutionFollowedByError()
  {
    return "public void run() {\n"
        + "    setObjectif(true);\n"
        + "    int t[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};\n"
        + "    System.out.println(t[42]);\n"
        + "}";
  }

  @Override public String generateExceptionRaisingCode()
  {
    return "public void run() {\n"
        + "    throw new RuntimeException(\"easy exception\");\n"
        + "}";
  }

  /** Debugging keeps the lines of the generated source, so it is switched off where the lines of the editor are checked. */
  private static void setDebug(boolean enabled)
  {
    if (Game.getInstance().isDebugEnabled() != enabled)
      Game.getInstance().switchDebug();
  }

  /** The error of a compilation is reported at its line in the editor, not in the generated source. */
  @Test public void testCompilationErrorLineMatchesEditor()
  {
    setDebug(false);
    try {
      exo.getSourceFile(pl, 0).setBody("public void run() {\n"
                                           + "    int a = 1;\n"
                                           + "    toto++;\n"
                                           + "}",
                                       pl);
      PLMCompilerException e = Assertions.assertThrows(PLMCompilerException.class, () -> exo.compile(null, StudentOrCorrection.STUDENT, pl));
      Assertions.assertTrue(e.getMessage().contains("Entity.java:3: "), e.getMessage());
    } finally {
      setDebug(true);
    }
  }

  /** The frames of a stack trace are reported at their line in the editor, not in the generated source. */
  @Test public void testStackTraceLineMatchesEditor() throws PLMCompilerException, InterruptedException
  {
    PrintStream realErr           = System.err;
    ByteArrayOutputStream capture = new ByteArrayOutputStream();
    setDebug(false);
    System.setErr(new PrintStream(capture, true));
    try {
      exo.getSourceFile(pl, 0).setBody(generateExceptionRaisingCode(), pl);
      String executable = exo.compile(null, StudentOrCorrection.STUDENT, pl);
      exo.runAll(WorldKind.CURRENT, new ArrayList<Future<?>>(), exo.lastResult, pl, executable);
    } finally {
      System.setErr(realErr);
      setDebug(true);
    }
    Assertions.assertTrue(capture.toString().contains("(Entity.java:2)"), capture.toString());
  }
}
