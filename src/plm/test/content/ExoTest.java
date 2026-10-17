package plm.test.content;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.Mockito;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.BrokenProgrammingLanguageException;
import plm.core.model.DemoRunner;
import plm.core.model.Game;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Exercise.StudentOrCorrection;
import plm.core.model.lesson.Exercise.WorldKind;
import plm.core.model.lesson.Lecture;
import plm.core.model.lesson.Lesson;
import plm.core.model.lesson.RunOutcome;
import plm.core.model.tracking.GitTracker;
import plm.core.utils.FileUtils;

/* This ancestor class defines useful methods that are used in specific tests, that are subclasses */

public class ExoTest {

  static final String[] lessonNamesToTest = new String[] {
      // WARNING, keep ChooseLessonDialog.lessons synchronized
      "lessons.welcome",
      "lessons.turmites",
      "lessons.maze",
      "lessons.turtleart",
      "lessons.sort.basic",
      "lessons.sort.dutchflag",
      "lessons.sort.baseball",
      "lessons.sort.pancake",
      "lessons.recursion.cons",
      "lessons.recursion.lego",
      "lessons.recursion.hanoi",
      // "lessons.lightbot", // Well, testing this requires testing the swing directly I guess
      "lessons.bat.string1",
      "lessons.lander",
  };

  /** Exercises that run for long: the turmites do tens of thousands of remote-primitive round trips, and the fractals draw very many segments */
  private static final Set<String> SLOW_EXERCISES = Set.of("turmites.TurmiteCreator", "turmites.LangtonColors", "recursion.lego.SquareKoch",
                                                           "recursion.lego.PentaKoch", "recursion.lego.DragonCurve1", "recursion.lego.DragonCurve2");

  protected static Duration timeout(Exercise exo) { return Duration.ofSeconds(SLOW_EXERCISES.contains(exo.getId()) ? 60 : 20); }

  /* Generate the stream of parameters we want to run our parameterized tests on */
  static public Stream<Arguments> exercises() throws BrokenProgrammingLanguageException
  {
    List<Arguments> result = new LinkedList<>();

    FileUtils.setLocale(new Locale("en"));
    Game g = Game.getInstance();
    g.setGitTracker(Mockito.mock(GitTracker.class)); // disable all progress trackers (git, etc)
    g.removeSessionKit();
    g.setBatchExecution();

    /* Compute the answers with the java entities */
    Game.getInstance().setProgramingLanguage(Game.getInstance().programmingLanguageManager.JAVA);

    Set<Lecture> alreadySeenExercises = new HashSet<Lecture>();
    for (String lessonName : lessonNamesToTest) {
      Lesson lesson = null;
      try {
        lesson = g.switchLesson(lessonName, false);
      } catch (Throwable t) {
        Assertions.fail("Switching to lesson " + lessonName + " raised a " + t.getClass().getName(), t);
      }
      Assertions.assertNotNull(lesson, "Cannot load the lesson " + lessonName);

      System.out.println("Lesson " + lessonName + " loaded (" + g.getCurrentLesson().getExerciseCount() + " exercises)");
      Assertions.assertNotEquals(0, g.getCurrentLesson().getExerciseCount(), "Cannot find any exercise in " + lessonName);
      for (Lecture l : g.getCurrentLesson().exercises())
        if (l instanceof Exercise) {
          Assertions.assertTrue(alreadySeenExercises.add(l), "The exercise " + l.getName() + " is listed twice");
          result.add(Arguments.of(Game.getInstance().getCurrentLesson(), l));
        }
    }
    System.out.println("There is currently " + result.size() + " exercises in our database. Yes sir.");
    System.out.println("");

    // g.switchDebug();
    g.setLocale(new Locale("en")); // Test the templates in English, to catch translation issues

    return result.stream();
  }

  protected void initExerciseState(Exercise exo)
  {
    // disable delay on world execution
    for (int worldRank = 0; worldRank < exo.getWorldCount(); worldRank++) {
      exo.getWorlds(WorldKind.INITIAL).get(worldRank).setDelay(0);
    }
  }

  /** Resets current world, populate it with the correction entity, and rerun it */
  protected void testCorrectionEntity(Exercise exo, ProgrammingLanguage lang) throws BrokenProgrammingLanguageException, InterruptedException
  {
    System.err.println("Test exo " + exo.getName() + " in " + lang + " (" + exo.getId() + ")");
    RunOutcome outcome;
    try {
      outcome = exo.compileRunCheck(null, StudentOrCorrection.CORRECTION, lang, new ArrayList<Future<?>>(), () -> {});
    } catch (UnsupportedOperationException uoe) {
      System.err.println("UnsupportedOperationException while executing" + exo.getId());
      throw uoe;
    }

    if (outcome.isCompilationError()) {
      String msg = exo.getId() + ": compilation error: " + outcome.compilationError + ". Compiled file:\n" +
                   ((exo.getSourceFileCount(lang) > 0) ? (exo.getSourceFile(lang, 0).getCorrectionSegments().body()) : "none");
      System.err.println(msg);
      Assertions.fail(msg);
    }

    if (!outcome.isPassed()) {
      String msg = "Test of " + exo.getId() + " failed (" + outcome.passedTests + "/" + outcome.totalTests + " passed): '" + outcome.executionError + "'";
      System.err.println(msg);
      for (int wnum = 0; wnum < exo.getWorldCount(); wnum++)
        try {
          String name = "FailingWorld-" + exo.getId() + "-World" + wnum + ".txt";
          exo.getWorld(wnum).writeToFile(new File(name));
          System.err.println(" World " + wnum + " dumped to " + name);
        } catch (IOException e) {
          System.err.println("Cannot write the bugged world to disk because of the following exception");
          e.printStackTrace();
        }
      Assertions.fail(msg);
    }
  }
}
