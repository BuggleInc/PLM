package plm.test.unit.exercise;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Locale;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import plm.core.lang.LangBlockly;
import plm.core.lang.LangJava;
import plm.core.lang.LangPython;
import plm.core.lang.LangScala;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Exercise.WorldKind;
import plm.core.model.lesson.ExerciseFactory;
import plm.core.model.lesson.ExerciseRunner;
import plm.core.model.lesson.tip.DefaultTipFactory;
import plm.core.model.session.SourceFile;
import plm.core.utils.FileUtils;
import plm.universe.World;
import plm.universe.unittest.ExampleWorld;
import unittest.Example;

public class ExerciseFactoryTest {
	private LangJava java = new LangJava(ClassLoader.getSystemClassLoader(), true);
	private LangScala scala = new LangScala(ClassLoader.getSystemClassLoader(), true);
	private LangPython python = new LangPython(true);
	private LangBlockly blockly = new LangBlockly(true);

	private ExerciseFactory exerciseFactory;
	private Exercise exo;
	private Locale locale = new Locale("en");
	private ExerciseRunner exerciseRunner;
	private ProgrammingLanguage[] programmingLanguages =  { java, scala, python };
	private Locale[] humanLanguages = { locale, new Locale("fr"), new Locale("pt_BR") };

	public ExerciseFactoryTest() {
	}

	@Before
	public void setUp() {
		exerciseRunner = new ExerciseRunner(locale);
		exerciseFactory = new ExerciseFactory(new FileUtils(ClassLoader.getSystemClassLoader()),
			locale, exerciseRunner, programmingLanguages, humanLanguages, new DefaultTipFactory());
		exo = new Example(new FileUtils(ClassLoader.getSystemClassLoader()));
		exerciseFactory.initializeExercise(exo, java);
	}

	@After
	public void tearDown() {
		exo = null;
		exerciseFactory = null;
		exerciseRunner = null;
	}

	@Test
	public void testInitializeExerciseShouldAddSupportedProgLang() {
		ProgrammingLanguage[] expectedSupportedProgLangs = { java, python };

		for(ProgrammingLanguage expectedSupportedProgLang : expectedSupportedProgLangs) {
			boolean actual = exo.isProgLangSupported(expectedSupportedProgLang);

			assertTrue("Should mark as supported "+expectedSupportedProgLang.getLang(), actual);
		}
	}

	@Test
	public void testInitializeExerciseShouldNotAddNonSupportedProgLangByExercise() {
		boolean actual = exo.isProgLangSupported(scala);

		assertFalse("Should not mark as supported progLang not supported by the exercise", actual);
	}

	@Test
	public void testInitializeExerciseShouldNotAddNonSupportedProgLangByFactory() {
		boolean actual = exo.isProgLangSupported(blockly);

		assertFalse("Should not mark as supported progLang not supported by the factory", actual);
	}

	@Test
	public void testInitializeExerciseShouldGenerateDefaultSourceFilesForEachSupportedProgLang() {
		Set<ProgrammingLanguage> supportedProgLangs = exo.getProgLanguages();

		for(ProgrammingLanguage supportedProgLang : supportedProgLangs) {
			boolean actual = exo.getDefaultSourceFile(supportedProgLang) instanceof SourceFile;

			assertTrue("Should have generated a source file for each supported progLang", actual);
		}
	}

	@Test
	public void testInitializeExerciseShouldNotGenerateDefaultSourceFilesForNonSupportedProgLangs() {
		ProgrammingLanguage[] nonSupportedProgLangs = { scala, blockly };

		for(ProgrammingLanguage nonSupportedProgLang : nonSupportedProgLangs) {
			boolean actual = exo.getDefaultSourceFile(nonSupportedProgLang) == null;

			assertTrue("Should have not generated a source file for non supported progLang", actual);
		}
	}

	@Test
	public void testInitializeExerciseShouldGenerateMissionsForEachSupportedHumanLang() {
		String filename = exo.getBaseName().replaceAll("\\.", "/");
		for(Locale humanLanguage : humanLanguages) {
			String expected = "";
			StringBuffer sb = null;
			try {
				sb = new FileUtils(ClassLoader.getSystemClassLoader()).readContentAsText(filename, humanLanguage, "html", true);
				expected = sb.toString();
			} catch (IOException ex) {}

			String actual = exo.getMission(humanLanguage, java);
			assertEquals("Should have retrieve the same content", expected, actual);
		}
	}

	@Test
	public void testInitializeExerciseShouldNotUpdateInitialWorlds() {
		for(World w : exo.getWorlds(WorldKind.INITIAL)) {
			ExampleWorld initialWorld = (ExampleWorld) w;
			boolean actual = initialWorld.getObjective();
			assertFalse("Initial world's objective should be false", actual);
		}
	}

	@Test
	public void testInitializeExerciseShouldNotUpdateCurrentWorlds() {
		for(World w : exo.getWorlds(WorldKind.CURRENT)) {
			ExampleWorld currentWorld = (ExampleWorld) w;
			boolean actual = currentWorld.getObjective();
			assertFalse("Current world's objective should be false", actual);
		}
	}

	@Test
	public void testInitializeExerciseShouldInitializeAnswerWorlds() {
		for(World w : exo.getWorlds(WorldKind.ANSWER)) {
			ExampleWorld answerWorld = (ExampleWorld) w;
			boolean actual = answerWorld.getObjective();
			assertTrue("Answer world's objective should be true", actual);
		}
	}
}
