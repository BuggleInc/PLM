package plm.test.simple.test;

import plm.core.model.Game;

public class ScalaSimpleExerciseTest extends JVMCompiledSimpleExerciseTest {
	
	public ScalaSimpleExerciseTest() {
		super(Game.SCALA);
	}

	@Override
	public String generateRuntimeExceptionRisingCode() {
		return "override def run() { var s:String = null; println(s.length()); }";
	}

	@Override
	public String generateCompilationExceptionRisingCode() {
		return "zqkdçajdé\"\"";
	}
}
