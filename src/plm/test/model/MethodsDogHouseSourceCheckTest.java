package plm.test.model;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.Future;
import java.util.stream.Stream;
import lessons.welcome.methods.basics.MethodsDogHouse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;
import plm.core.PLMCompilerException;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Exercise.StudentOrCorrection;
import plm.core.model.lesson.Lecture;
import plm.core.model.lesson.RunOutcome;
import plm.core.model.tracking.GitTracker;
import plm.core.utils.FileUtils;

/**
 * MethodsDogHouse refuses, through Exercise.verifySource(), the code that does not call left() exactly once. The check is called
 * directly, as compiling and running the code of each case in each language would take minutes.
 */
public class MethodsDogHouseSourceCheckTest {
  private static Exercise exo;

  @BeforeAll static void loadExercise() throws Exception
  {
    FileUtils.setLocale(new Locale("en"));
    Game g = Game.getInstance();
    g.setGitTracker(Mockito.mock(GitTracker.class));
    g.removeSessionKit();
    g.setBatchExecution();
    Assertions.assertNotNull(g.switchLesson("lessons.welcome", false));
    for (Lecture l : g.getCurrentLesson().exercises())
      if (l instanceof MethodsDogHouse)
        exo = (Exercise)l;
    Assertions.assertNotNull(exo, "MethodsDogHouse not found in lessons.welcome");
  }

  /** The languages with a DogHouse entity: all of them but LightBot */
  static Stream<ProgrammingLanguage> languages() { return Stream.of(Game.getInstance().programmingLanguageManager.langs).filter(lang -> !lang.isLightBot()); }

  /** The message of the refusal of the code by verifySource(), or null if the code is accepted */
  private static String refusal(String code)
  {
    try {
      exo.verifySource(code);
      return null;
    } catch (PLMCompilerException e) {
      return e.getMessage();
    }
  }

  private static String oneCall(ProgrammingLanguage lang)
  {
    return switch (lang.getLang()) {
      case "Python" -> "def dogHouse():\n    for i in range(4):\n        step()\n        step()\n        left()\n";
      case "Scala" -> "def dogHouse(): Unit = {\n  for (i <- 1 to 4) {\n    step()\n    step()\n    left()\n  }\n}\n";
      case "C" -> "void dogHouse() {\n  int i;\n  for (i = 0; i < 4; i++) {\n    step();\n    step();\n    left();\n  }\n}\n";
      case "Java" -> "void dogHouse() {\n  for (int i = 0; i < 4; i++) {\n    step();\n    step();\n    left();\n  }\n}\n";
      default -> throw new IllegalArgumentException("No DogHouse code for " + lang);
    };
  }

  /** The calls are counted ignoring the comments and strings of each language */
  @ParameterizedTest @MethodSource("languages") public void testLeftCalls(ProgrammingLanguage lang)
  {
    String oneCall = oneCall(lang);
    String comment = lang.isPython() ? "# left()\n" : "// left()\n";
    String string  = switch (lang.getLang()) {
      case "Python" -> "s = \"left()\"\n";
      case "Scala" -> "val s = \"left()\"\n";
      case "C" -> "char *s = \"left()\";\n";
      default -> "String s = \"left()\";\n";
    };

    Assertions.assertNull(refusal(oneCall), "one call must be accepted");
    Assertions.assertNull(refusal(comment + oneCall), "a call in a comment must be ignored");
    Assertions.assertNull(refusal(string + oneCall), "a call in a string must be ignored");
    String message = refusal(oneCall.replace("left()", "left(); left()"));
    Assertions.assertNotNull(message, "two calls must be refused");
    Assertions.assertTrue(message.contains("exactly once"), message);
    Assertions.assertNotNull(refusal(oneCall.replace("left()", "step()")), "no call must be refused");
  }

  /** The alias of left() in the current human language counts as well, and names the primitive in the message */
  @Test public void testAliasOfTheHumanLanguage()
  {
    String oneCall = oneCall(Game.getInstance().programmingLanguageManager.JAVA);
    I18n previous  = Game.i18n;
    try {
      Game.i18n = I18nFactory.getI18n(getClass(), "org.plm.i18n.Messages", Locale.FRENCH, I18nFactory.FALLBACK);
      Assertions.assertNull(refusal(oneCall.replace("left()", "gauche()")), "one call to the French alias must be accepted");
      String message = refusal(oneCall.replace("left()", "left(); gauche()"));
      Assertions.assertNotNull(message, "a call and a call to the French alias must be refused");
      Assertions.assertTrue(message.contains("gauche()"), message);
    } finally {
      Game.i18n = previous;
    }
  }

  /** compileRunCheck() reports the refusal as a compilation error, without running the code */
  @Test public void testRefusalIsReportedAsACompilationError() throws InterruptedException
  {
    ProgrammingLanguage java = Game.getInstance().programmingLanguageManager.JAVA;
    exo.setEditorContent(java, oneCall(java).replace("left()", "left(); left()"));
    RunOutcome outcome = exo.compileRunCheck(null, StudentOrCorrection.STUDENT, java, new ArrayList<Future<?>>(), () -> {});

    Assertions.assertTrue(outcome.isCompilationError(), "the refusal must be a compilation error");
    Assertions.assertTrue(outcome.compilationError.contains("exactly once"), outcome.compilationError);
  }
}
