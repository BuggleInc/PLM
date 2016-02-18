package unit.exercise;

import static org.junit.Assert.*;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import plm.core.lang.LangBlockly;
import plm.core.lang.LangJava;
import plm.core.lang.LangPython;
import plm.core.lang.LangScala;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.lesson.ExecutionProgress;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.ExerciseFactory;
import plm.core.model.lesson.ExerciseRunner;
import plm.core.model.lesson.ExecutionProgress.outcomeKind;
import unit.exercise.example.Example;

public class ExerciseRunnerTest {

	private LangJava java = new LangJava(true);
	private LangScala scala = new LangScala(true);
	private LangPython python = new LangPython(true);
	private LangBlockly blockly = new LangBlockly(true);

	private ExerciseFactory exerciseFactory;
	private Exercise exo;
	private Locale locale = new Locale("en");
	private ExerciseRunner exerciseRunner;
	private ProgrammingLanguage[] programmingLanguages =  { java, scala, python };
	private Locale[] humanLanguages = { locale, new Locale("fr"), new Locale("pt_BR") };
	private String rootDirectory = "test";

	public ExerciseRunnerTest() {}

	@Before 
	public void setUp() {
		exerciseRunner = new ExerciseRunner(locale);
		exerciseFactory = new ExerciseFactory(locale, exerciseRunner, programmingLanguages, humanLanguages);
		exerciseFactory.setRootDirectory(rootDirectory);
		exo = new Example();
		exerciseFactory.initializeExercise(exo, java);
	}

	@After
	public void tearDown() {
		exo = null;
		exerciseFactory = null;
		exerciseRunner = null;
	}

	@Test
	public void testRunSyntaxErrorCodeShouldReturnCompile() {
		String code = 
				"public void run() {"
				+ "setObjective(true)"
				+ "}";
		CompletableFuture<ExecutionProgress> f = exerciseRunner.run(exo, java, code);
		ExecutionProgress result = f.join();
		outcomeKind expected = outcomeKind.COMPILE;
		outcomeKind actual = result.outcome;

		assertEquals("The outcome should be COMPILATION", expected, actual);
	}

	@Test
	public void testRunExecutionErrorCodeShouldReturnFail() {
		String code = 
				"public void run() {"
				+ "String s = null;"
				+ "int length = s.length();"
				+ "}";
		CompletableFuture<ExecutionProgress> f = exerciseRunner.run(exo, java, code);
		ExecutionProgress result = f.join();
		outcomeKind expected = outcomeKind.FAIL;
		outcomeKind actual = result.outcome;

		assertEquals("The outcome should be FAIL", expected, actual);
	}

	@Test
	public void testRunWrongCodeShouldReturnFail() {
		String code = 
				"public void run() {"
				+ "setObjective(false);"
				+ "}";
		CompletableFuture<ExecutionProgress> f = exerciseRunner.run(exo, java, code);
		ExecutionProgress result = f.join();
		outcomeKind expected = outcomeKind.FAIL;
		outcomeKind actual = result.outcome;

		assertEquals("The outcome should be FAIL", expected, actual);
	}

	@Test
	public void testRunInfiniteLoopCodeShouldReturnTimeout() {
		String code = 
				"public void run() {"
				+ "while(true) {}"
				+ "}";

		// We don't want to wait 10s each time
		exerciseRunner.setWaitingTime(500);
		exerciseRunner.setMaxNumberOfTries(5);

		CompletableFuture<ExecutionProgress> f = exerciseRunner.run(exo, java, code);
		ExecutionProgress result = f.join();
		outcomeKind expected = outcomeKind.TIMEOUT;
		outcomeKind actual = result.outcome;

		assertEquals("The outcome should be TIMEOUT", expected, actual);
	}

	@Test
	public void testRunSolutionCodeShouldReturnPass() {
		String code = 
				"public void run() {"
				+ "setObjective(true);"
				+ "}";
		CompletableFuture<ExecutionProgress> f = exerciseRunner.run(exo, java, code);
		ExecutionProgress result = f.join();
		outcomeKind expected = outcomeKind.PASS;
		outcomeKind actual = result.outcome;

		assertEquals("The outcome should be PASS", expected, actual);
	}
}
