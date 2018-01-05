package plm.core.model.lesson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

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
import plm.core.model.lesson.ExecutionProgress.outcomeKind;
import plm.core.model.lesson.tip.DefaultTipFactory;
import plm.core.utils.FileUtils;

@RunWith(Parameterized.class)
public class ExerciseRunnerTest {

	private Locale locale = new Locale("en");
	private Locale[] humanLanguages = { locale };
	
	private static final ProgrammingLanguages programmingLanguages = new ProgrammingLanguages(ClassLoader.getSystemClassLoader());
	private static ProgrammingLanguage javaLang = programmingLanguages.getProgrammingLanguage("java");
	private static ProgrammingLanguage scalaLang = programmingLanguages.getProgrammingLanguage("scala");
	private static ProgrammingLanguage pythonLang = programmingLanguages.getProgrammingLanguage("python");
	private static ProgrammingLanguage blocklyLang = programmingLanguages.getProgrammingLanguage("blockly");

	private scala.collection.Iterable<String> langs = 
	  scala.collection.JavaConversions.iterableAsScalaIterable(Arrays.asList(new String[] { "en" }));
	private Exercises exoBook = new Exercises(new Lessons(ClassLoader.getSystemClassLoader(), langs),
			new FileUtils(ClassLoader.getSystemClassLoader()),
			programmingLanguages,
			 new DefaultTipFactory(), humanLanguages
			);
	private Exercise exo;
	private ExerciseRunner exerciseRunner;
	private ProgrammingLanguage progLang;

	@Parameterized.Parameters
	public static Collection<Object[]> programmingLanguages() {
		return Arrays.asList(new Object[][] {
	         { javaLang },
	         { scalaLang },
	         { pythonLang },
	         { blocklyLang }
	      });
	}

	@Test
	public void testInitializationValidity() {
		assertNotEquals("java != scala", javaLang, scalaLang);
		assertNotEquals("java != python", javaLang, pythonLang);
		assertNotEquals("java != blockly", javaLang, blocklyLang);
		
		assertNotEquals("scala != python", scalaLang, pythonLang);
		assertNotEquals("scala != blockly", scalaLang, blocklyLang);
		
		assertNotEquals("python != blockly", pythonLang, blocklyLang);
	}

	
	public ExerciseRunnerTest(ProgrammingLanguage progLang) {
		this.progLang = progLang;
	}

	@Before 
	public void setUp() {
		exerciseRunner = new ExerciseRunner(locale);
		exo = exoBook.getExercise("environment.Environment").get();
	}

	@After
	public void tearDown() {
		exo = null;
		exerciseRunner = null;
	}

	@Test
	public void testRunSyntaxErrorCodeShouldReturnCompile() {
		Logger.info("---------------------------------------------------");
		Logger.info("#### Test testRunSyntaxErrorCodeShouldReturnCompile in "+progLang);
		
		String code = "This is an invalid source code";
		CompletableFuture<ExecutionProgress> f = exerciseRunner.run(exo, progLang, code);
		ExecutionProgress result = f.join();
		outcomeKind expected = outcomeKind.COMPILE;
		outcomeKind actual = result.outcome;

		if (!expected.equals(actual))
			Logger.info("Expected: "+expected+"; Outcome: "+actual+";"+result);
		assertEquals("The outcome should be COMPILATION", expected, actual);
	}

	@Test
	public void testRunExceptionRaisingCodeShouldReturnFail() {
		Logger.info("---------------------------------------------------");
		Logger.info("#### Test testRunExceptionRaisingCodeShouldReturnFail in "+progLang);

		String code = "";
		if (progLang.equals(javaLang)) {
			code = "String s = null; \n"
				 + "int length = s.length();\n";
		}
		else if(progLang.equals(scalaLang)) {
			code = "val s: String = null;\n"
				 + "val i = s.length;\n";
		}
		else if(progLang.equals(pythonLang) || progLang.equals(blocklyLang)) {
			code = "truc = None\n"
				 + "print truc.toto";
		}
		else 
			throw new RuntimeException("unknown proglang: "+progLang);
		
		CompletableFuture<ExecutionProgress> f = exerciseRunner.run(exo, progLang, code);
		ExecutionProgress result = f.join();
		outcomeKind expected = outcomeKind.FAIL;
		outcomeKind actual = result.outcome;

		if (!expected.equals(actual))
			Logger.info("Expected: "+expected+"; Outcome: "+actual+";"+result);
		assertEquals("The outcome should be FAIL", expected, actual);
	}

	@Test
	public void testRunWrongCodeShouldReturnFail() {
		Logger.info("---------------------------------------------------");
		Logger.info("#### Test testRunWrongCodeShouldReturnFail in "+progLang);

		String code;
		if(progLang.equals(javaLang) || progLang.equals(scalaLang) || progLang.equals(pythonLang) || progLang.equals(blocklyLang)) {
			code = "backward();";
		}
		else 
			throw new RuntimeException("unknown proglang: "+progLang);
		CompletableFuture<ExecutionProgress> f = exerciseRunner.run(exo, progLang, code);
		ExecutionProgress result = f.join();
		outcomeKind expected = outcomeKind.FAIL;
		outcomeKind actual = result.outcome;

		if (!expected.equals(actual))
			Logger.info("Expected: "+expected+"; Outcome: "+actual+";"+result);
		assertEquals("The outcome should be FAIL", expected, actual);
	}

	@Test
	public void testRunInfiniteLoopCodeShouldReturnTimeout() {
		Logger.info("---------------------------------------------------");
		Logger.info("#### Test testRunInfiniteLoopCodeShouldReturnTimeout in "+progLang);

		String code = "";
		if (progLang.equals(javaLang)) {
			code = "while(true) {}";
		}
		else if(progLang.equals(scalaLang)) {
			code = "while(true) {};";
		}
		else if(progLang.equals(pythonLang) || progLang.equals(blocklyLang)) {
			code = "i = 0\n"
				 + "while(True):\n"
				 + "  i = i + 1";
		}		
		else 
			throw new RuntimeException("unknown proglang: "+progLang);

		CompletableFuture<ExecutionProgress> f = exerciseRunner.run(exo, progLang, code);
		ExecutionProgress result = f.join();
		outcomeKind expected = outcomeKind.TIMEOUT;
		outcomeKind actual = result.outcome;

		if (!expected.equals(actual))
			Logger.info("Expected: "+expected+"; Outcome: "+actual+";"+result);
		assertEquals("The outcome should be TIMEOUT", expected, actual);
	}

	@Test
	public void testRunSolutionCodeShouldReturnPass() {
		Logger.info("---------------------------------------------------");
		Logger.info("#### Test testRunSolutionCodeShouldReturnPass in "+progLang);
		
		String code = "forward();";
		if(progLang.equals(scalaLang)) {
			code = "forward()";
		}
		else if(progLang.equals(pythonLang) || progLang.equals(blocklyLang)) {
			code = "forward()";
		}
		CompletableFuture<ExecutionProgress> f = exerciseRunner.run(exo, progLang, code);
		ExecutionProgress result = f.join();
		outcomeKind expected = outcomeKind.PASS;
		outcomeKind actual = result.outcome;

		if (!expected.equals(actual))
			Logger.info("Expected: "+expected+"; Outcome: "+actual+";"+result);
		assertEquals("The outcome should be PASS", expected, actual);
	}
}
