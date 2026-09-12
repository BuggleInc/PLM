package plm.test.simple.test;

import plm.core.model.BrokenProgrammingLanguageException;
import plm.core.model.Game;

public class ScalaSimpleExerciseTest extends CompiledSimpleExerciseTest {

  public ScalaSimpleExerciseTest() throws BrokenProgrammingLanguageException { super(Game.getInstance().programmingLanguageManager.SCALA); }

  @Override public String generateSyntaxErrorCode() { return "zqkdçajdé\"\""; }

  @Override public String generateVariableErrorCode() { return "toto += 1;\n"; }

  @Override public String generateNullPointerErrorCode()
  {
    return "override def run() {\n"
        + "  var s:String = null;\n"
        + "  println(s.length());\n"
        + "}";
  }

  @Override public String generateOutOfBoundsErrorCode()
  {
    return "override def run() {\n"
        + "  var t:Array[Int] = Array(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);\n"
        + "  println(t(42));\n"
        + "}";
  }

  @Override public String generateWrongCode()
  {
    return "override def run() {\n"
        + "  setObjectif(false);\n"
        + "}";
  }

  @Override public String generateSolutionFollowedByError()
  {
    return "override def run() {\n"
        + "  setObjectif(true);\n"
        + "  var t:Array[Int] = Array(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);\n"
        + "  println(t(42));\n"
        + "}";
  }

  @Override public String generateExceptionRaisingCode()
  {
    return "override def run() {\n"
        + "  throw new Exception(\"easy exception\")\n"
        + "}";
  }
}
