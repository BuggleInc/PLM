package plm.test.simple.test;

import plm.core.model.Game;

public class PythonSimpleExerciseTest extends ScriptingSimpleExerciseTest {

	public PythonSimpleExerciseTest() {
		super(Game.PYTHON);
	}

	@Override
	public String generateRuntimeExceptionRisingCode() {
		return "def toto():\nédvw<fe\"v[>";
	}

	@Override
	public String generateCompilationExceptionRisingCode() {
		return generateRuntimeExceptionRisingCode();
	}

}
