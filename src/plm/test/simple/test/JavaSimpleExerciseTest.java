package plm.test.simple.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import plm.core.PLMCompilerException;
import plm.core.model.BrokenProgrammingLanguageException;
import plm.core.model.Game;
import plm.core.model.lesson.Exercise.StudentOrCorrection;

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

  /** The error of a compilation is reported at its line in the editor, not in the generated source. */
  @Test public void testCompilationErrorLineMatchesEditor()
  {
    setDebug(false);
    try {
      exo.getSourceFile(pl, 0).setEditorContent("public void run() {\n"
                                                    + "    int a = 1;\n"
                                                    + "    toto++;\n"
                                                    + "}",
                                                pl);
      PLMCompilerException e = Assertions.assertThrows(PLMCompilerException.class, () -> pl.compileExo(exo, null, StudentOrCorrection.STUDENT));
      Assertions.assertTrue(e.getMessage().contains("Entity.java:3: "), e.getMessage());
    } finally {
      setDebug(true);
    }
  }
}
