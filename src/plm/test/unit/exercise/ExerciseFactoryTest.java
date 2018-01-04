package plm.test.unit.exercise;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import plm.core.lang.ProgrammingLanguage;
import plm.core.lang.ProgrammingLanguages;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Exercise.WorldKind;
import plm.core.model.lesson.Exercises;
import plm.core.model.lesson.Lessons;
import plm.core.model.lesson.tip.DefaultTipFactory;
import plm.core.model.session.SourceFile;
import plm.core.utils.FileUtils;
import plm.universe.World;
import plm.universe.bugglequest.AbstractBuggle;
import plm.universe.bugglequest.BuggleWorld;

public class ExerciseFactoryTest {
	private static final ProgrammingLanguages programmingLanguages = new ProgrammingLanguages(ClassLoader.getSystemClassLoader());
	private static ProgrammingLanguage javaLang = programmingLanguages.getProgrammingLanguage("java");
	private static ProgrammingLanguage scalaLang = programmingLanguages.getProgrammingLanguage("scala");
	private static ProgrammingLanguage pythonLang = programmingLanguages.getProgrammingLanguage("python");
	private static ProgrammingLanguage blocklyLang = programmingLanguages.getProgrammingLanguage("blocky");

	private Locale locale = new Locale("en");
	private Locale[] humanLanguages = { locale, new Locale("fr"), new Locale("pt_BR") };
	private scala.collection.Iterable<String> langs = 
			  scala.collection.JavaConversions.iterableAsScalaIterable(Arrays.asList(new String[] { "en" }));
	private Exercises exoBook = new Exercises(new Lessons(ClassLoader.getSystemClassLoader(), langs),
			new FileUtils(ClassLoader.getSystemClassLoader()),
			programmingLanguages,
			 new DefaultTipFactory(), humanLanguages
			);

	private Exercise exo;

	public ExerciseFactoryTest() {
	}

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
		exo = null;
	}

	@Test
	public void testInitializeExerciseShouldAddSupportedProgLang() {
		exo = exoBook.getExercise("environment.Environment").get();

		ProgrammingLanguage[] expectedSupportedProgLangs = { javaLang, pythonLang };

		for(ProgrammingLanguage expectedSupportedProgLang : expectedSupportedProgLangs) {
			boolean actual = exo.isProgLangSupported(expectedSupportedProgLang);

			assertTrue("Should mark as supported "+expectedSupportedProgLang.getLang(), actual);
		}
	}

	@Test
	public void testInitializeExerciseShouldNotAddNonSupportedProgLang() {
		exo = exoBook.getExercise("bat.bool1.MonkeyTrouble").get();
		
		boolean actual = exo.isProgLangSupported(blocklyLang);

		assertFalse("Should not mark as supported progLang that are not supported by the exercise", actual);
	}

	@Test
	public void testInitializeExerciseShouldGenerateDefaultSourceFilesForEachSupportedProgLang() {
		exo = exoBook.getExercise("bat.bool1.MonkeyTrouble").get();
		
		Set<ProgrammingLanguage> supportedProgLangs = exo.getProgLanguages();

		for(ProgrammingLanguage supportedProgLang : supportedProgLangs) {
			boolean actual = exo.getDefaultSourceFile(supportedProgLang) instanceof SourceFile;

			assertTrue("Should have generated a source file for each supported progLang", actual);
		}
	}

	@Test
	public void testInitializeExerciseShouldNotGenerateDefaultSourceFilesForNonSupportedProgLangs() {
		exo = exoBook.getExercise("bat.bool1.MonkeyTrouble").get();

		ProgrammingLanguage[] nonSupportedProgLangs = { blocklyLang };

		for(ProgrammingLanguage nonSupportedProgLang : nonSupportedProgLangs) {
			boolean actual = exo.getDefaultSourceFile(nonSupportedProgLang) == null;

			assertTrue("Should have not generated a source file for non supported progLang", actual);
		}
	}

	@Test
	public void testInitializeExerciseShouldGenerateMissionsForEachSupportedHumanLang() {
		exo = exoBook.getExercise("environment.Environment").get();

		String filename = exo.getBaseName().replaceAll("\\.", "/");
		for(Locale humanLanguage : humanLanguages) {
			String actual = exo.getMission(humanLanguage, javaLang);
			assertNotEquals("Should have retrieve the same content", "", actual);
		}
	}

	@Test
	public void testInitializeExerciseShouldNotUpdateInitialWorlds() {
		exo = exoBook.getExercise("environment.Environment").get();

		for(World w : exo.getWorlds(WorldKind.INITIAL)) {
			BuggleWorld initialWorld = (BuggleWorld) w;
			int y = ((AbstractBuggle)initialWorld.getEntity(0)).getY();
			assertEquals("Y of buggle in initial world should be 3", y, 3);
		}
	}

	@Test
	public void testInitializeExerciseShouldNotUpdateCurrentWorlds() {
		exo = exoBook.getExercise("environment.Environment").get();
		
		for(World w : exo.getWorlds(WorldKind.CURRENT)) {
			BuggleWorld currentWorld = (BuggleWorld) w;
			int y = ((AbstractBuggle)currentWorld.getEntity(0)).getY();
			assertEquals("Y of buggle in current world should be 3", y, 3);
		}
	}

	@Test
	public void testInitializeExerciseShouldInitializeAnswerWorlds() {
		exo = exoBook.getExercise("environment.Environment").get();
		
		for(World w : exo.getWorlds(WorldKind.ANSWER)) {
			BuggleWorld currentWorld = (BuggleWorld) w;
			int y = ((AbstractBuggle)currentWorld.getEntity(0)).getY();
			assertEquals("Y of buggle in answer world should be 2", y, 2);
		}
	}
}
