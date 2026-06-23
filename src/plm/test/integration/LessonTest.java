package plm.test.integration;


import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import plm.core.lang.ProgrammingLanguage;
import plm.core.model.BrokenProgrammingLanguageException;
import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.core.model.lesson.Lesson.LoadingOutcome;
import plm.core.utils.FileUtils;

public class LessonTest {
	
	private static String[] lessonNamesToTest = new String[] { // WARNING, keep ChooseLessonDialog.lessons synchronized
		"lessons.welcome", "lessons.turmites", "lessons.maze", "lessons.turtleart",
		"lessons.sort.basic", "lessons.sort.dutchflag", "lessons.sort.baseball", "lessons.sort.pancake", 
		"lessons.recursion.cons", "lessons.recursion.lego", "lessons.recursion.hanoi",
		// "lessons.lightbot", // Well, testing this requires testing the swing directly I guess
		"lessons.bat.string1", "lessons.lander",
		};
		
	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	public static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	public void setUp() throws Exception {
	}

	@AfterEach
	public void tearDown() throws Exception {
	}
	
	static public Stream<String> lessons() {
			List<String> result = new LinkedList<String>();
			for(String lessonName:lessonNamesToTest) {
					result.add(lessonName);
			}
			return result.stream();
	}
	
	public Lesson loadLesson(String lessonName, ProgrammingLanguage pl) throws InstantiationException, IllegalAccessException, ClassNotFoundException, BrokenProgrammingLanguageException {
		FileUtils.setLocale(new Locale("en"));
		Game g = Game.getInstance();
		g.getProgressSpyListeners().clear(); // disable all progress spies (git, etc)
		g.removeSessionKit();
		g.setBatchExecution();

		/* Compute the answers with the selected language entities */
		Game.getInstance().setProgramingLanguage(pl);
		return Game.getInstance().switchLesson(lessonName, true);
	}
	
	@ParameterizedTest
	@MethodSource("lessons")
	public void testJavaLesson(String lessonName) throws InstantiationException, IllegalAccessException, ClassNotFoundException, BrokenProgrammingLanguageException {
          Lesson lesson = loadLesson(lessonName, Game.getInstance().programmingLanguageManager.JAVA);
          Assertions.assertTrue(lesson.getLoadingOutcomeState() == LoadingOutcome.SUCCESS,
                                "An error arose while loading lesson " + lesson.getName() + "...");
	}
	
	@ParameterizedTest
	@MethodSource("lessons")
	public void testScalaLesson(String lessonName) throws InstantiationException, IllegalAccessException, ClassNotFoundException, BrokenProgrammingLanguageException {
          Lesson lesson = loadLesson(lessonName, Game.getInstance().programmingLanguageManager.SCALA);
          Assertions.assertTrue(lesson.getLoadingOutcomeState() == LoadingOutcome.SUCCESS,
                                "An error arose while loading lesson " + lesson.getName() + "...");
	}
	
	@ParameterizedTest
	@MethodSource("lessons")
	public void testPythonLesson(String lessonName) throws InstantiationException, IllegalAccessException, ClassNotFoundException, BrokenProgrammingLanguageException {
          Lesson lesson = loadLesson(lessonName, Game.getInstance().programmingLanguageManager.PYTHON);
          Assertions.assertTrue(lesson.getLoadingOutcomeState() == LoadingOutcome.SUCCESS,
                                "An error arose while loading lesson " + lesson.getName() + "...");

	}	
}
