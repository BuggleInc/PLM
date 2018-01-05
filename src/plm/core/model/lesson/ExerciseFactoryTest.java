package plm.core.model.lesson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;

import org.junit.Test;

import plm.core.lang.ProgrammingLanguage;
import plm.core.lang.ProgrammingLanguages;
import plm.core.model.lesson.Exercise.WorldKind;
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
	private static ProgrammingLanguage blocklyLang = programmingLanguages.getProgrammingLanguage("blockly");

	private Locale locale = new Locale("en");
	private Locale[] humanLanguages = { locale, new Locale("fr"), new Locale("pt_BR") };
	private scala.collection.Iterable<String> langs = 
			  scala.collection.JavaConversions.iterableAsScalaIterable(Arrays.asList(new String[] { "en" }));
	private Exercises exoBook = new Exercises(new Lessons(ClassLoader.getSystemClassLoader(), langs),
			new FileUtils(ClassLoader.getSystemClassLoader()),
			programmingLanguages,
			 new DefaultTipFactory(), humanLanguages
			);

	public ExerciseFactoryTest() {
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
	
	@Test
	public void testInitializeExerciseShouldAddSupportedProgLang() {
		Exercise exo = exoBook.getExercise("environment.Environment").get();

		ProgrammingLanguage[] expectedSupportedProgLangs = { javaLang, pythonLang };

		for(ProgrammingLanguage expectedSupportedProgLang : expectedSupportedProgLangs) {
			boolean actual = exo.isProgLangSupported(expectedSupportedProgLang);

			assertTrue("Should mark as supported "+expectedSupportedProgLang.getLang()+" in exo "+exo.getName(), actual);
		}
	}

	@Test
	public void testInitializeExerciseShouldNotAddNonSupportedProgLang() {
		Exercise exo = exoBook.getExercise("bat.bool1.MonkeyTrouble").get();
		
		boolean actual = exo.isProgLangSupported(blocklyLang);

		assertFalse("Should not mark as supported progLang that are not supported by the exercise "+exo.getName() , actual);
	}

	@Test
	public void testInitializeExerciseShouldGenerateDefaultSourceFilesForEachSupportedProgLang() {
		Exercise exo = exoBook.getExercise("bat.bool1.MonkeyTrouble").get();
		
		Set<ProgrammingLanguage> supportedProgLangs = exo.getProgLanguages();

		for(ProgrammingLanguage supportedProgLang : supportedProgLangs) {
			boolean actual = exo.getDefaultSourceFile(supportedProgLang) instanceof SourceFile;

			assertTrue("Should have generated a source file for the supported progLang "+supportedProgLang+" in exo "+exo.getName(), actual);
		}
	}

	@Test
	public void testInitializeExerciseShouldNotGenerateDefaultSourceFilesForNonSupportedProgLangs() {
		Exercise exo = exoBook.getExercise("bat.bool1.MonkeyTrouble").get();

		ProgrammingLanguage[] nonSupportedProgLangs = { blocklyLang };

		for(ProgrammingLanguage nonSupportedProgLang : nonSupportedProgLangs) {
			boolean actual = exo.getDefaultSourceFile(nonSupportedProgLang) == null;

			assertTrue("Should have not generated a source file for non supported progLang "+nonSupportedProgLang.getLang()+" in exo "+exo.getName(), 
					actual);
		}
	}

	@Test
	public void testInitializeExerciseShouldGenerateMissionsForEachSupportedHumanLang() {
		Exercise exo = exoBook.getExercise("environment.Environment").get();

		String filename = exo.getBaseName().replaceAll("\\.", "/");
		for(Locale humanLanguage : humanLanguages) {
			String actual = exo.getMission(humanLanguage, javaLang);
			assertNotEquals("Should have retrieve the same content", "", actual);
		}
	}

	@Test
	public void testInitializeExerciseShouldNotUpdateInitialWorlds() {
		Exercise exo = exoBook.getExercise("environment.Environment").get();

		for(World w : exo.getWorlds(WorldKind.INITIAL)) {
			BuggleWorld initialWorld = (BuggleWorld) w;
			int y = ((AbstractBuggle)initialWorld.getEntity(0)).getY();
			assertEquals("Y of buggle in initial world should be 3", y, 3);
		}
	}

	@Test
	public void testInitializeExerciseShouldNotUpdateCurrentWorlds() {
		Exercise exo = exoBook.getExercise("environment.Environment").get();
		
		for(World w : exo.getWorlds(WorldKind.CURRENT)) {
			BuggleWorld currentWorld = (BuggleWorld) w;
			int y = ((AbstractBuggle)currentWorld.getEntity(0)).getY();
			assertEquals("Y of buggle in current world should be 3", y, 3);
		}
	}

	@Test
	public void testInitializeExerciseShouldInitializeAnswerWorlds() {
		Exercise exo = exoBook.getExercise("environment.Environment").get();
		
		for(World w : exo.getWorlds(WorldKind.ANSWER)) {
			BuggleWorld currentWorld = (BuggleWorld) w;
			int y = ((AbstractBuggle)currentWorld.getEntity(0)).getY();
			assertEquals("Y of buggle in answer world should be 2", y, 2);
		}
	}
}
