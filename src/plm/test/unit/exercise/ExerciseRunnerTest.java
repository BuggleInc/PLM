package plm.test.unit.exercise;

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

import plm.core.lang.ProgrammingLanguage;
import plm.core.lang.ProgrammingLanguages;
import plm.core.log.Logger;
import plm.core.model.lesson.ExecutionProgress;
import plm.core.model.lesson.ExecutionProgress.outcomeKind;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.ExerciseFactory;
import plm.core.model.lesson.ExerciseRunner;
import plm.core.model.lesson.Exercises;
import plm.core.model.lesson.Lessons;
import plm.core.model.lesson.tip.DefaultTipFactory;
import plm.core.utils.FileUtils;

@RunWith(Parameterized.class)
public class ExerciseRunnerTest {

	private static final ProgrammingLanguages programmingLanguages = new plm.core.lang.ProgrammingLanguages(ClassLoader.getSystemClassLoader());
	private static ProgrammingLanguage javaLang = programmingLanguages.getProgrammingLanguage("java");
	private static ProgrammingLanguage scalaLang = programmingLanguages.getProgrammingLanguage("scala");
	private static ProgrammingLanguage pythonLang = programmingLanguages.getProgrammingLanguage("python");
	private static ProgrammingLanguage blocklyLang = programmingLanguages.getProgrammingLanguage("blocky");

	private scala.collection.Iterable<String> langs = 
	  scala.collection.JavaConversions.iterableAsScalaIterable(Arrays.asList(new String[] { "en" }));
	private Exercises exoBook = new Exercises(new Lessons(ClassLoader.getSystemClassLoader(), langs),
			new FileUtils(ClassLoader.getSystemClassLoader()),
			programmingLanguages,
			 new DefaultTipFactory(), null
			);
	private ExerciseFactory exerciseFactory;
	private Exercise exo;
	private Locale locale = new Locale("en");
	private ExerciseRunner exerciseRunner;
	private Locale[] humanLanguages = { locale };
	private ProgrammingLanguage progLang;

	@Parameterized.Parameters
	public static Collection<Object[]> programmingLanguages() {
		Logger.info("Starting");
		return Arrays.asList(new Object[][] {
	         { javaLang },
	         { scalaLang }/*,
	         { python },
	         { blockly }*/
	      });
	}

	public ExerciseRunnerTest(ProgrammingLanguage progLang) {
		this.progLang = progLang;
	}

	@Before 
	public void setUp() {
		exerciseRunner = new ExerciseRunner(locale);
		exerciseFactory = new ExerciseFactory(
				new FileUtils(ClassLoader.getSystemClassLoader()),
				locale, exerciseRunner, programmingLanguages.programmingLanguages(), humanLanguages, new DefaultTipFactory());
		exo = exoBook.getExercise("environment.Environment").get();
		exerciseFactory.initializeExercise(exo, javaLang);
	}

	@After
	public void tearDown() {
		exo = null;
		exerciseFactory = null;
		exerciseRunner = null;
	}

	@Test
	public void testRunSyntaxErrorCodeShouldReturnCompile() {
		Logger.info("---------------------------------------------------");
		Logger.info("#### Test testRunSyntaxErrorCodeShouldReturnCompile");
		
		String code = 
				"public void run() {"
				+ "setObjective(true)"
				+ "}";
		if(progLang == scalaLang) {
			code = 
					"def run(): Unit {"
					+ "setObjective(true)"
					+ "}";
		}
		else if(progLang == pythonLang || progLang == blocklyLang) {
			code =
					"def run():\n"
					+ "  setObjective(true)";
		}
		CompletableFuture<ExecutionProgress> f = exerciseRunner.run(exo, progLang, code);
		ExecutionProgress result = f.join();
		outcomeKind expected = outcomeKind.COMPILE;
		outcomeKind actual = result.outcome;

		Logger.info("Expected: "+expected+"; Outcome: "+actual+";"+result);
		assertEquals("The outcome should be COMPILATION", expected, actual);
	}

	@Test
	public void testRunExceptionRaisingCodeShouldReturnFail() {
		Logger.info("---------------------------------------------------");
		Logger.info("#### Test testRunExceptionRaisingCodeShouldReturnFail in "+progLang);

		String code = "";
		if (progLang == javaLang) {
			code =
				"public void run() {"
				+ "String s = null;"
				+ "int length = s.length();"
				+ "}";
		}
		else if(progLang == scalaLang) {
			code = 
					"def run(): Unit = {"
					+ "val s: String = null;"
					+ "val i = s.length;"
					+ "}";
		}
		else if(progLang == pythonLang || progLang == blocklyLang) {
			code =
					"def run():\n"
					+ "  truc = None\n"
					+ "  print truc.toto";
		}
		else 
			throw new RuntimeException("unknown proglang: "+progLang);
		CompletableFuture<ExecutionProgress> f = exerciseRunner.run(exo, progLang, code);
		ExecutionProgress result = f.join();
		outcomeKind expected = outcomeKind.FAIL;
		outcomeKind actual = result.outcome;

		Logger.info("Expected: "+expected+"; Outcome: "+actual+";"+result);
		assertEquals("The outcome should be FAIL", expected, actual);
	}

	@Test
	public void testRunWrongCodeShouldReturnFail() {
		Logger.info("---------------------------------------------------");
		Logger.info("#### Test testRunWrongCodeShouldReturnFail in "+progLang);

		String code = 
				"public void run() {"
				+ "setObjective(false);"
				+ "}";
		if(progLang == scalaLang) {
			code = 
					"def run(): Unit = {"
					+ "setObjective(false)"
					+ "}";
		}
		else if(progLang == pythonLang || progLang == blocklyLang) {
			code =
					"def run():\n"
					+ "  setObjective(False)";
		}
		CompletableFuture<ExecutionProgress> f = exerciseRunner.run(exo, progLang, code);
		ExecutionProgress result = f.join();
		outcomeKind expected = outcomeKind.FAIL;
		outcomeKind actual = result.outcome;

		Logger.info("Expected: "+expected+"; Outcome: "+actual+";"+result);
		assertEquals("The outcome should be FAIL", expected, actual);
	}

	@Test
	public void testRunInfiniteLoopCodeShouldReturnTimeout() {
		Logger.info("---------------------------------------------------");
		Logger.info("#### Test testRunInfiniteLoopCodeShouldReturnTimeout in "+progLang);

		String code = "";
		if (progLang == javaLang) {
			code =
				"public void run() {"
				+ "while(true) {}"
				+ "}";
		}
		else if(progLang == scalaLang) {
			code = 
					"def run(): Unit = {"
					+ "while(true) {};"
					+ "}";
		}
		else if(progLang == pythonLang || progLang == blocklyLang) {
			code =
					"def run():\n"
					+ "  i = 0\n"
					+ "  while(True):\n"
					+ "    i = i + 1";
		}		
		else 
			throw new RuntimeException("unknown proglang: "+progLang);

		CompletableFuture<ExecutionProgress> f = exerciseRunner.run(exo, progLang, code);
		ExecutionProgress result = f.join();
		outcomeKind expected = outcomeKind.TIMEOUT;
		outcomeKind actual = result.outcome;

		Logger.info("Expected: "+expected+"; Outcome: "+actual+";"+result);
		assertEquals("The outcome should be TIMEOUT", expected, actual);
	}

	@Test
	public void testRunSolutionCodeShouldReturnPass() {
		Logger.info("---------------------------------------------------");
		Logger.info("#### Test testRunSolutionCodeShouldReturnPass in "+progLang);
		
		String code = 
				"public void run() {"
				+ "setObjective(true);"
				+ "}";
		if(progLang == scalaLang) {
			code = 
					"def run(): Unit = {"
					+ "setObjective(true)"
					+ "}";
		}
		else if(progLang == pythonLang || progLang == blocklyLang) {
			code =
					"def run():\n"
					+ "  setObjective(True)";
		}
		CompletableFuture<ExecutionProgress> f = exerciseRunner.run(exo, progLang, code);
		ExecutionProgress result = f.join();
		outcomeKind expected = outcomeKind.PASS;
		outcomeKind actual = result.outcome;

		Logger.info("Expected: "+expected+"; Outcome: "+actual+";"+result);
		assertEquals("The outcome should be PASS", expected, actual);
	}
}
