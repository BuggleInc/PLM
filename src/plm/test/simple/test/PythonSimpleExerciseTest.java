package plm.test.simple.test;

import plm.core.model.Game;

public class PythonSimpleExerciseTest extends ScriptingSimpleExerciseTest {

	public PythonSimpleExerciseTest() {
		super(Game.PYTHON);
	}

	@Override
	public String generateRuntimeExceptionRisingCode() {
		return "myList=[1,2,3,4,5,6]\nprint myList[42]";
	}

	@Override
	public String generateCompilationExceptionRisingCode() {
		return "def toto():\ndvw<fe\"v[>";
	}

}
