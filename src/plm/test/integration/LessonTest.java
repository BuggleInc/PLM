package plm.test.integration;

import java.util.Locale;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import plm.core.lang.ProgrammingLanguage;
import plm.core.lang.ProgrammingLanguageManager;
import plm.core.model.BrokenProgrammingLanguageException;
import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.core.model.lesson.Lesson.LoadingOutcome;
import plm.core.model.tracking.GitTracker;
import plm.core.utils.FileUtils;

/** Checks that every lesson of ExoTest.lessonNamesToTest loads in Java, Scala and Python */
public class LessonTest {

  /** Every lesson, paired with every language */
  static public Stream<Arguments> lessonsAndLanguages()
  {
    FileUtils.setLocale(new Locale("en"));
    ProgrammingLanguageManager langs = Game.getInstance().programmingLanguageManager;
    return Stream.of(ExoTest.lessonNamesToTest).flatMap(lesson -> Stream.of(langs.JAVA, langs.SCALA, langs.PYTHON).map(pl -> Arguments.of(lesson, pl)));
  }

  @ParameterizedTest
  @MethodSource("lessonsAndLanguages")
  public void testLesson(String lessonName, ProgrammingLanguage pl)
      throws InstantiationException, IllegalAccessException, ClassNotFoundException, BrokenProgrammingLanguageException
  {
    FileUtils.setLocale(new Locale("en"));
    Game g = Game.getInstance();
    g.setGitTracker(Mockito.mock(GitTracker.class)); // disable all progress trackers (git, etc)
    g.removeSessionKit();
    g.setBatchExecution();

    g.setProgramingLanguage(pl);
    Lesson lesson = g.switchLesson(lessonName, true);
    Assertions.assertTrue(lesson.getLoadingOutcomeState() == LoadingOutcome.SUCCESS, "An error arose while loading lesson " + lessonName + " in " + pl + "...");
  }
}
