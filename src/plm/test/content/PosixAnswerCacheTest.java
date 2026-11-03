package plm.test.content;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Future;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import plm.core.model.Game;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Exercise.StudentOrCorrection;
import plm.core.model.lesson.Exercise.WorldKind;
import plm.core.model.lesson.Lesson;
import plm.core.model.lesson.RunOutcome;
import plm.universe.World;
import plm.universe.posix.PosixWorld;

/**
 * The POSIX exercises that do not declare translated texts in their expected worlds (see PosixWorld.holdingTranslations()) read
 * their cache files in every language: their worlds built and run in French must give the files computed in English. When this
 * fails, the exercise should declare its translated texts. Its methods run one after the other, as they change the human
 * language of the whole PLM.
 */
public class PosixAnswerCacheTest {

  private static String written(World w) throws IOException
  {
    StringWriter text = new StringWriter();
    try (BufferedWriter writer = new BufferedWriter(text)) {
      w.writeToFile(writer);
    }
    return text.toString();
  }

  @ParameterizedTest @MethodSource("plm.test.content.ExoTest#exercises") public void cachedInEveryLanguage(Lesson lesson, Exercise exo) throws Exception
  {
    Assumptions.assumeTrue(exo.getWorlds(WorldKind.INITIAL).get(0) instanceof PosixWorld, "Not a POSIX exercise");
    List<String> english = new ArrayList<>();
    for (World w : exo.getWorlds(WorldKind.ANSWER))
      english.add(written(w));

    Game.getInstance().setLocale(new Locale("fr"));
    try {
      Assumptions.assumeTrue(exo.getWorlds(WorldKind.INITIAL).get(0).haveIO(), "The expected worlds hold translated texts, cached in English only");
      Exercise french = exo.getClass().getConstructor(Lesson.class).newInstance(lesson); // its worlds built in French
      Game.waitInitThreads();
      for (World w : french.getWorlds(WorldKind.INITIAL))
        w.setDelay(0);
      RunOutcome outcome = french.compileRunCheck(null, StudentOrCorrection.CORRECTION, Game.getInstance().programmingLanguageManager.JAVA,
                                                  new ArrayList<Future<?>>(), () -> {});
      Assertions.assertTrue(outcome.isPassed(), exo.getId() + " fails in French against its cache: " + outcome.executionError);
      List<World> current = french.getWorlds(WorldKind.CURRENT);
      for (int i = 0; i < current.size(); i++)
        Assertions.assertEquals(english.get(i), written(current.get(i)),
                                exo.getId() + ": world " + current.get(i).getName() + " differs in French, so it should declare its translated texts");
    } finally {
      Game.getInstance().setLocale(new Locale("en"));
    }
  }
}
