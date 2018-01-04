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
	private static final ProgrammingLanguages programmingLanguages = new plm.core.lang.ProgrammingLanguages(ClassLoader.getSystemClassLoader());
	private static ProgrammingLanguage java = programmingLanguages.getProgrammingLanguage("java");
	private static ProgrammingLanguage scala = programmingLanguages.getProgrammingLanguage("scala");
	private static ProgrammingLanguage python = programmingLanguages.getProgrammingLanguage("python");
	private static ProgrammingLanguage blockly = programmingLanguages.getProgrammingLanguage("blocky");

	private Exercises exoBook = new Exercises(new Lessons(ClassLoader.getSystemClassLoader(),null),
			new FileUtils(ClassLoader.getSystemClassLoader()),
			programmingLanguages,
			 new DefaultTipFactory(), null
			);

	private Exercise exo;
	private Locale locale = new Locale("en");
	private Locale[] humanLanguages = { locale, new Locale("fr"), new Locale("pt_BR") };

	public ExerciseFactoryTest() {
	}

	@Before
	public void setUp() {
		exo = exoBook.getExercise("environment.Environment").get();
	}

	@After
	public void tearDown() {
		exo = null;
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
			BuggleWorld initialWorld = (BuggleWorld) w;
			int y = ((AbstractBuggle)initialWorld.getEntity(0)).getY();
			assertEquals("Y of buggle in initial world should be 5", y, 5);
		}
	}

	@Test
	public void testInitializeExerciseShouldNotUpdateCurrentWorlds() {
		for(World w : exo.getWorlds(WorldKind.CURRENT)) {
			BuggleWorld currentWorld = (BuggleWorld) w;
			int y = ((AbstractBuggle)currentWorld.getEntity(0)).getY();
			assertEquals("Y of buggle in current world should be 5", y, 5);
		}
	}

	@Test
	public void testInitializeExerciseShouldInitializeAnswerWorlds() {
		for(World w : exo.getWorlds(WorldKind.ANSWER)) {
			BuggleWorld currentWorld = (BuggleWorld) w;
			int y = ((AbstractBuggle)currentWorld.getEntity(0)).getY();
			assertEquals("Y of buggle in current world should be 6", y, 6);
		}
	}
}
