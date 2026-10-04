package plm.test.simple.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import plm.core.PLMCompilerException;
import plm.core.model.BrokenProgrammingLanguageException;
import plm.core.model.Game;
import plm.core.model.lesson.Exercise.StudentOrCorrection;

public class ScalaSimpleExerciseTest extends CompiledSimpleExerciseTest {

  public ScalaSimpleExerciseTest() throws BrokenProgrammingLanguageException { super(Game.getInstance().programmingLanguageManager.SCALA); }

  @Override public String generateSyntaxErrorCode() { return "zqkdçajdé\"\""; }

  @Override public String generateVariableErrorCode() { return "toto += 1;\n"; }

  @Override public String generateNullPointerErrorCode()
  {
    return "override def run(): Unit = {\n"
        + "  var s:String = null;\n"
        + "  println(s.length());\n"
        + "}";
  }

  @Override public String generateOutOfBoundsErrorCode()
  {
    return "override def run(): Unit = {\n"
        + "  var t:Array[Int] = Array(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);\n"
        + "  println(t(42));\n"
        + "}";
  }

  @Override public String generateWrongCode()
  {
    return "override def run(): Unit = {\n"
        + "  setObjectif(false);\n"
        + "}";
  }

  @Override public String generateSolutionFollowedByError()
  {
    return "override def run(): Unit = {\n"
        + "  setObjectif(true);\n"
        + "  var t:Array[Int] = Array(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);\n"
        + "  println(t(42));\n"
        + "}";
  }

  @Override public String generateExceptionRaisingCode()
  {
    return "override def run(): Unit = {\n"
        + "  throw new Exception(\"easy exception\")\n"
        + "}";
  }

  /** The error of a compilation is reported at its line in the editor, not in the generated source. */
  @Test public void testCompilationErrorLineMatchesEditor()
  {
    setDebug(false);
    try {
      exo.getSourceFile(pl, 0).setEditorContent("override def run(): Unit = {\n"
                                                    + "  var a = 1;\n"
                                                    + "  toto += 1;\n"
                                                    + "}",
                                                pl);
      PLMCompilerException e = Assertions.assertThrows(PLMCompilerException.class, () -> exo.compile(null, StudentOrCorrection.STUDENT, pl));
      Assertions.assertTrue(e.getMessage().contains("Entity.scala:3:"), e.getMessage());
    } finally {
      setDebug(true);
    }
  }
}
