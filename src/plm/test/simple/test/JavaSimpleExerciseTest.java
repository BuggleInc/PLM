package plm.test.simple.test;

import static org.mockito.Mockito.mock;

import java.util.Locale;
import java.util.UUID;

import plm.core.model.Game;
import plm.core.model.LogHandler;

public class JavaSimpleExerciseTest extends JVMCompiledSimpleExerciseTest {
	
	public JavaSimpleExerciseTest() {
		super(new Game(UUID.randomUUID().toString(), mock(LogHandler.class), new Locale("en"), "Java", false), Game.JAVA);
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
		return "public void run() {\n"
				+ "    String s = null;\n"
				+ "    System.out.println(s.length());\n"
				+ "}";
	}

	@Override
	public String generateOutOfBoundsErrorCode() {
		return "public void run() {\n"
				+ "    int t[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};\n"
				+ "    System.out.println(t[42]);\n"
				+ "}";
	}

	@Override
	public String generateWrongCode() {
		return "public void run() {\n"
				+ "    ((SimpleWorld) world).setObjectif(false);\n"
				+ "}";
	}

	@Override
	public String generateSolutionFollowedByError() {
		return "public void run() {\n"
				+ "    ((SimpleWorld) world).setObjectif(true);\n"
				+ "    int t[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};\n"
				+ "    System.out.println(t[42]);\n"
				+ "}";
	}

	@Override
	public String generateExceptionRaisingCode() {
		return "public void run() {\n"
				+ "    throw new RuntimeException(\"easy exception\");\n"
				+ "}";
	}
	
}
