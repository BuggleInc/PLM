package plm.test.simple.test;

import plm.core.model.Game;



/* FIXME : THIS IS DISABLED IN AllSimpleExercises */




public class ScalaSimpleExerciseTest extends JVMCompiledSimpleExerciseTest {
	
	public ScalaSimpleExerciseTest() {
		super(Game.SCALA);
	}
	
	@Override
	public String generateSyntaxErrorCode() {
		return "zqkdçajdé\"\"";
	}

	@Override
	public String generateVariableErrorCode() {
		return "toto += 1;\n";
	}

	@Override
	public String generateNullPointerErrorCode() {
		return "override def run() {\n"
				+ "  var s:String = null;\n"
				+ "  println(s.length());\n"
				+ "}";
	}

	@Override
	public String generateOutOfBoundsErrorCode() {
		return "override def run() {\n"
				+ "  var t:Array[Int] = Array(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);\n"
				+ "  println(t(42));\n"
				+ "}";
	}

	@Override
	public String generateWrongCode() {
		return "override def run() {\n"
				+ "  world.asInstanceOf[SimpleWorld].setObjectif(false);\n"
				+ "}";
	}

	@Override
	public String generateSolutionFollowedByError() {
		return "override def run() {\n"
				+ "  world.asInstanceOf[SimpleWorld].setObjectif(true);\n"
				+ "  var t:Array[Int] = Array(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);\n"
				+ "  println(t(42));\n"
				+ "}";
	}

	@Override
	public String generateExceptionRaisingCode() {
		return "override def run() {\n"
				+ "  throw new Exception(\"easy exception\")\n"
				+ "}";
	}
}
