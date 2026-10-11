package plm.test.simple.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import plm.core.PLMCompilerException;
import plm.core.model.BrokenProgrammingLanguageException;
import plm.core.model.Game;
import plm.core.model.lesson.Exercise.StudentOrCorrection;

/** The entity of these tests includes stdlib.h in an IMPORT section: the codes below rely on it for NULL, malloc() and abs(). */
public class CSimpleExerciseTest extends CompiledSimpleExerciseTest {

  public CSimpleExerciseTest() throws BrokenProgrammingLanguageException { super(Game.getInstance().programmingLanguageManager.C); }

  @Override public String generateSyntaxErrorCode()
  {
    return "void run() {\n"
        + "  int a = 1\n"
        + "}\n";
  }

  @Override public String generateVariableErrorCode()
  {
    return "void run() {\n"
        + "  toto = 1;\n"
        + "}\n";
  }

  @Override public String generateNullPointerErrorCode()
  {
    return "void run() {\n"
        + "  int *p = NULL;\n"
        + "  *p = 42;\n"
        + "}\n";
  }

  @Override public String generateOutOfBoundsErrorCode()
  {
    return "void run() {\n"
        + "  int *tab = malloc(10 * sizeof(int));\n"
        + "  tab[10] = 1;\n"
        + "}\n";
  }

  @Override public String generateExceptionRaisingCode()
  {
    return "void run() {\n"
        + "  *(int *)NULL = 42;\n"
        + "}\n";
  }

  @Override public String generateWrongCode()
  {
    return "void run() {\n"
        + "  setObjectif(false);\n"
        + "}\n";
  }

  @Override public String generateSolutionFollowedByError()
  {
    return "void run() {\n"
        + "  setObjectif(true);\n"
        + "  int *p = NULL;\n"
        + "  *p = 42;\n"
        + "}\n";
  }

  /**
   * C has no stack trace: the address sanitizer report is the closest thing, and it is produced by the dereference of generateExceptionRaisingCode(), not by
   * abort().
   */
  @Override protected String locationOfLine(int line) { return "Entity.c:" + line; }

  /** The #line directive of the generated source makes gcc report the error at its line in the editor. */
  @Test public void testCompilationErrorLineMatchesEditor()
  {
    exo.getSourceFile(pl, 0).setEditorContent("void run() {\n"
                                                  + "  int a = 1;\n"
                                                  + "  toto = 1;\n"
                                                  + "}\n",
                                              pl);
    PLMCompilerException e = Assertions.assertThrows(PLMCompilerException.class, () -> pl.compileExo(exo, null, StudentOrCorrection.STUDENT));
    Assertions.assertTrue(e.getMessage().contains("Entity.c:3:"), e.getMessage());
  }

  @Test public void testImportSegmentIsHonored() throws PLMCompilerException, InterruptedException
  {
    assertPassesThanksToImports("void run() {\n"
                                + "  if (abs(-3) == 3)\n"
                                + "    setObjectif(true);\n"
                                + "}\n");
  }
}
