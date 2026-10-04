package plm.test.simple.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import plm.core.PLMCompilerException;
import plm.core.model.BrokenProgrammingLanguageException;
import plm.core.model.Game;
import plm.core.model.lesson.Exercise.StudentOrCorrection;

public class PythonSimpleExerciseTest extends CompiledSimpleExerciseTest {

  public PythonSimpleExerciseTest() throws BrokenProgrammingLanguageException { super(Game.getInstance().programmingLanguageManager.PYTHON); }

  @Override public String generateSyntaxErrorCode() { return "zqkdçajdé\"\""; }

  @Override public String generateVariableErrorCode() { return "toto++;\n"; }

  @Override public String generateNullPointerErrorCode()
  {
    return "def run():\n"
        + "  truc = None\n"
        + "  print(truc.toto)";
  }

  @Override public String generateExceptionRaisingCode()
  {
    return "def run():\n"
        + "  raise Exception(\"I know python!\")";
  }

  @Override public String generateOutOfBoundsErrorCode()
  {
    return "def run():\n"
        + "  tab = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]\n"
        + "  print(tab[42])";
  }

  @Override public String generateWrongCode()
  {
    return "def run():\n"
        + "  setObjectif(False)\n";
  }

  @Override public String generateSolutionFollowedByError()
  {
    return "def run():\n"
        + "  setObjectif(False)\n"
        + "  tab = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]\n"
        + "  print(tab[42])";
  }

  @Override protected String locationOfLine(int line) { return "Entity.py\", line " + line + ","; }

  /** The error of a compilation is reported at its line in the editor, not in the generated source. */
  @Test public void testCompilationErrorLineMatchesEditor()
  {
    setDebug(false);
    try {
      exo.getSourceFile(pl, 0).setEditorContent("def run():\n"
                                                    + "  a = 1\n"
                                                    + "  toto +=\n",
                                                pl);
      PLMCompilerException e = Assertions.assertThrows(PLMCompilerException.class, () -> exo.compile(null, StudentOrCorrection.STUDENT, pl));
      Assertions.assertTrue(e.getMessage().contains("Entity.py\", line 3"), e.getMessage());
    } finally {
      setDebug(true);
    }
  }
}
