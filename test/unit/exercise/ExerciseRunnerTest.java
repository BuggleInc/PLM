package unit.exercise;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import plm.core.lang.LangBlockly;
import plm.core.lang.LangJava;
import plm.core.lang.LangPython;
import plm.core.lang.LangScala;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.lesson.ExecutionProgress;
import plm.core.model.lesson.ExecutionProgress.outcomeKind;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.ExerciseFactory;
import plm.core.model.lesson.ExerciseRunner;
import unit.exercise.examplerunner.ExampleRunner;

@RunWith(Parameterized.class)
public class ExerciseRunnerTest {

	private static LangJava java = new LangJava(true);
	private static LangScala scala = new LangScala(true);
	private static LangPython python = new LangPython(true);
	private static LangBlockly blockly = new LangBlockly(true);
	private static ProgrammingLanguage[] programmingLanguages =  { java, scala, python, blockly };

	private ExerciseFactory exerciseFactory;
	private Exercise exo;
	private Locale locale = new Locale("en");
	private ExerciseRunner exerciseRunner;
	private Locale[] humanLanguages = { locale };
	private String rootDirectory = "test";
	private ProgrammingLanguage progLang;

	@Parameterized.Parameters
	public static Collection<Object[]> programmingLanguages() {
		return Arrays.asList(new Object[][] {
	         { java },
	         { scala },
	         { python },
	         { blockly }
	      });
	}

	public ExerciseRunnerTest(ProgrammingLanguage progLang) {
		this.progLang = progLang;
	}

	@Before 
	public void setUp() {
		exerciseRunner = new ExerciseRunner(locale);
		exerciseFactory = new ExerciseFactory(locale, exerciseRunner, programmingLanguages, humanLanguages);
		exerciseFactory.setRootDirectory(rootDirectory);
		exo = new ExampleRunner();
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
		if(progLang == scala) {
			code = 
					"def run(): Unit {"
					+ "setObjective(true)"
					+ "}";
		}
		else if(progLang == python || progLang == blockly) {
			code =
					"def run():\n"
					+ "  setObjective(true)";
		}
		CompletableFuture<ExecutionProgress> f = exerciseRunner.run(exo, progLang, code);
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
		if(progLang == scala) {
			code = 
					"def run(): Unit = {"
					+ "var s: String = null;"
					+ "var length: Int = s.length;"
					+ "}";
		}
		else if(progLang == python || progLang == blockly) {
			code =
					"def run():\n"
					+ "  truc = None\n"
					+ "  print truc.toto";
		}
		CompletableFuture<ExecutionProgress> f = exerciseRunner.run(exo, progLang, code);
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
		if(progLang == scala) {
			code = 
					"def run(): Unit = {"
					+ "setObjective(false)"
					+ "}";
		}
		else if(progLang == python || progLang == blockly) {
			code =
					"def run():\n"
					+ "  setObjective(False)";
		}
		CompletableFuture<ExecutionProgress> f = exerciseRunner.run(exo, progLang, code);
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
		if(progLang == scala) {
			code = 
					"def run(): Unit = {"
					+ "while(true) {};"
					+ "}";
		}
		else if(progLang == python || progLang == blockly) {
			code =
					"def run():\n"
					+ "  i = 0\n"
					+ "  while(True):\n"
					+ "    i = i + 1";
		}

		// We don't want to wait 10s each time
		exerciseRunner.setWaitingTime(500);
		exerciseRunner.setMaxNumberOfTries(5);

		CompletableFuture<ExecutionProgress> f = exerciseRunner.run(exo, progLang, code);
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
		if(progLang == scala) {
			code = 
					"def run(): Unit = {"
					+ "setObjective(true)"
					+ "}";
		}
		else if(progLang == python || progLang == blockly) {
			code =
					"def run():\n"
					+ "  setObjective(True)";
		}
		CompletableFuture<ExecutionProgress> f = exerciseRunner.run(exo, progLang, code);
		ExecutionProgress result = f.join();
		outcomeKind expected = outcomeKind.PASS;
		outcomeKind actual = result.outcome;

		assertEquals("The outcome should be PASS", expected, actual);
	}
}
