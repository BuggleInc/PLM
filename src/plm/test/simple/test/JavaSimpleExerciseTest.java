package plm.test.simple.test;

import plm.core.model.Game;

public class JavaSimpleExerciseTest extends JVMCompiledSimpleExerciseTest {
	
	public JavaSimpleExerciseTest() {
		super(Game.JAVA);
	}

	@Override
	public String generateSyntaxErrorCode() {
		return "zqkdçajdé\"\"";
	}

	@Override
	public String generateVariableErrorCode() {
		return "toto++;\n";
	}

	@Override
	public String generateNullPointerErrorCode() {
		return "public void run() { String s = null;\nSystem.out.println(s.length()); }";
	}

	@Override
	public String generateOutOfBoundsErrorCode() {
		return "public void run() { int t[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};\nSystem.out.println(t[42]); }";
	}
	
}
