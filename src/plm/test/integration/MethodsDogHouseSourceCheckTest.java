package plm.test.integration;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.Future;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Exercise.StudentOrCorrection;
import plm.core.model.lesson.Lecture;
import plm.core.model.lesson.RunOutcome;
import plm.core.utils.FileUtils;

/** MethodsDogHouse refuses, through Exercise.verifySource(), the code that does not call left() exactly once. */
public class MethodsDogHouseSourceCheckTest {
  private static Exercise exo;

  @BeforeAll static void loadExercise() throws Exception
  {
    FileUtils.setLocale(new Locale("en"));
    Game g = Game.getInstance();
    g.getProgressTrackerListeners().clear();
    g.removeSessionKit();
    g.setBatchExecution();
    Assertions.assertNotNull(g.switchLesson("lessons.welcome", false));
    for (Lecture l : g.getCurrentLesson().exercises())
      if (l.getClass().getSimpleName().equals("MethodsDogHouse"))
        exo = (Exercise)l;
    Assertions.assertNotNull(exo, "MethodsDogHouse not found in lessons.welcome");
  }

  /** The languages with a DogHouse entity: all of them but LightBot */
  static Stream<ProgrammingLanguage> languages() { return Stream.of(Game.getInstance().programmingLanguageManager.langs).filter(lang -> !lang.isLightBot()); }

  /** Sets the editor content, and returns the message of the refusal of the source, or null if the code is accepted */
  private static String refusal(ProgrammingLanguage lang, String code) throws InterruptedException
  {
    exo.setEditorContent(lang, code);
    RunOutcome outcome = exo.compileRunCheck(null, StudentOrCorrection.STUDENT, lang, new ArrayList<Future<?>>(), () -> {});
    return outcome.outcome == RunOutcome.kind.COMPILE ? outcome.compilationError : null;
  }

  @ParameterizedTest @MethodSource("languages") public void testLeftCalls(ProgrammingLanguage lang) throws Exception
  {
    String oneCall = switch (lang.getLang()) {
      case "Python" -> "def dogHouse():\n    for i in range(4):\n        forward()\n        forward()\n        left()\n";
      case "Scala" -> "def dogHouse(): Unit = {\n  for (i <- 1 to 4) {\n    stepForward()\n    stepForward()\n    left()\n  }\n}\n";
      case "C" -> "void dogHouse() {\n  int i;\n  for (i = 0; i < 4; i++) {\n    forward(1);\n    forward(1);\n    left();\n  }\n}\n";
      case "Java" -> "void dogHouse() {\n  for (int i = 0; i < 4; i++) {\n    stepForward();\n    stepForward();\n    left();\n  }\n}\n";
      default -> throw new IllegalArgumentException("No DogHouse code for " + lang);
    };
    String comment = lang.isPython() ? "# left()\n" : "// left()\n";
    String string  = switch (lang.getLang()) {
      case "Python" -> "s = \"left()\"\n";
      case "Scala" -> "val s = \"left()\"\n";
      case "C" -> "char *s = \"left()\";\n";
      default -> "String s = \"left()\";\n";
    };
    String step = switch (lang.getLang()) {
      case "Python" -> "forward()";
      case "C" -> "forward(1)";
      default -> "stepForward()";
    };
    String twoCalls = oneCall.replace("left()", "left(); left()");
    String noCall   = oneCall.replace("left()", step);

    Assertions.assertNull(refusal(lang, oneCall), "one call must be accepted");
    Assertions.assertNull(refusal(lang, comment + oneCall), "a call in a comment must be ignored");
    Assertions.assertNull(refusal(lang, string + oneCall), "a call in a string must be ignored");

    String message = refusal(lang, twoCalls);
    Assertions.assertNotNull(message, "two calls must be refused");
    Assertions.assertTrue(message.contains("exactly once"), message);
    Assertions.assertNotNull(refusal(lang, noCall), "no call must be refused");
  }
}
