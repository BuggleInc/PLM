package plm.test.simple.test;

import plm.core.model.Game;

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
		return "override def run() { var s:String = null; println(s.length()); }";
	}

	@Override
	public String generateOutOfBoundsErrorCode() {
		return "override def run() { var t:Array[Int] = Array(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);\n println(t(42)); }";
	}

	@Override
	public String generateWrongCode() {
		return "override def run() { world.asInstanceOf[SimpleWorld].setObjectif(false);  }";
	}
}
