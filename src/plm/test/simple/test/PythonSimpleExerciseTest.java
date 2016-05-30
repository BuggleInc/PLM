package plm.test.simple.test;

import static org.mockito.Mockito.mock;

import java.util.Locale;
import java.util.UUID;

import plm.core.model.Game;
import plm.core.model.LogHandler;

public class PythonSimpleExerciseTest extends ScriptingSimpleExerciseTest {

	public PythonSimpleExerciseTest() {
		super(new Game(UUID.randomUUID().toString(), mock(LogHandler.class), new Locale("en"), "Python", false), Game.PYTHON);
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
		return "def run():\n"
				+ "  truc = None\n"
				+ "  print truc.toto";
	}

	@Override
	public String generateExceptionRaisingCode() {
		return "def run():\n"
				+ "  raise Exception(\"I know python!\")";
	}
	
	@Override
	public String generateOutOfBoundsErrorCode() {
		return "def run():\n"
				+ "  tab = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]\n"
				+ "  print tab[42]";
	}

	@Override
	public String generateWrongCode() {
		return "def run():\n"
				+ "  w.setObjectif(False)\n";
	}

	@Override
	public String generateSolutionFollowedByError() {
		return "def run():\n"
				+ "  w.setObjectif(False)\n"
				+ "  tab = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]\n"
				+ "  print tab[42]";
	}
}
