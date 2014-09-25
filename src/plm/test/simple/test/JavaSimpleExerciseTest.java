package plm.test.simple.test;

import plm.core.model.Game;

public class JavaSimpleExerciseTest extends JVMCompiledSimpleExerciseTest {
	
	public JavaSimpleExerciseTest() {
		super(Game.JAVA);
	}

	@Override
	public String generateRuntimeExceptionRisingCode() {
		return "public void run() { String s = null; System.out.println(s.length()); }";
	}

	@Override
	public String generateCompilationExceptionRisingCode() {
		return "zqkdçajdé\"\"";
	}
	
}
