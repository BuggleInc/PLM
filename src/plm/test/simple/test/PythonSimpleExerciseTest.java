package plm.test.simple.test;

import plm.core.model.Game;

public class PythonSimpleExerciseTest extends ScriptingSimpleExerciseTest {

	public PythonSimpleExerciseTest() {
		super(Game.PYTHON);
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
		return "def run():\n  truc = None\n  print truc.toto";
	}

	@Override
	public String generateOutOfBoundsErrorCode() {
		return "def run():\n  tab = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]\n  print tab[42]";
	}

	@Override
	public String generateWrongCode() {
		return "def run():\n  w.setObjectif(False)\n";
	}
}
