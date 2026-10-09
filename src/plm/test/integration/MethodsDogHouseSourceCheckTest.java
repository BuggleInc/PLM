package plm.test.integration;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.Future;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import plm.core.PLMCompilerException;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Exercise.StudentOrCorrection;
import plm.core.model.lesson.Lecture;
import plm.core.utils.FileUtils;

/** MethodsDogHouse refuses, through Exercise.verifySource(), the code that does not call left() exactly once. */
public class MethodsDogHouseSourceCheckTest {
  private static Exercise exo;

  @BeforeAll static void loadExercise() throws Exception
  {
    FileUtils.setLocale(new Locale("en"));
    Game g = Game.getInstance();
    g.getProgressSpyListeners().clear();
    g.removeSessionKit();
    g.setBatchExecution();
    Assertions.assertNotNull(g.switchLesson("lessons.welcome", false));
    for (Lecture l : g.getCurrentLesson().exercises())
      if (l.getClass().getSimpleName().equals("MethodsDogHouse"))
        exo = (Exercise)l;
    Assertions.assertNotNull(exo, "MethodsDogHouse not found in lessons.welcome");
  }

  private static ProgrammingLanguage language(String name)
  {
    for (ProgrammingLanguage lang : Game.getInstance().programmingLanguageManager.langs)
      if (lang.getLang().equalsIgnoreCase(name))
        return lang;
    throw new IllegalArgumentException(name);
  }

  /** Sets the editor content, and returns the message of the refusal of the source, or null if the code is accepted */
  private static String refusal(ProgrammingLanguage lang, String code) throws InterruptedException
  {
    exo.getSourceFile(lang, 0).setEditorContent(code, lang);
    try {
      exo.compileRunCheck(null, StudentOrCorrection.STUDENT, lang, new ArrayList<Future<?>>(), () -> {});
      return null;
    } catch (PLMCompilerException e) {
      return e.getMessage();
    }
  }

  @ParameterizedTest @ValueSource(strings = {"Java", "Python"}) public void testLeftCalls(String langName) throws Exception
  {
    ProgrammingLanguage lang = language(langName);

    boolean py      = lang.isPython();
    String comment  = py ? "# left()\n" : "// left()\n";
    String string   = py ? "s = \"left()\"\n" : "String s = \"left()\";\n";
    String step     = py ? "forward()" : "stepForward()";
    String oneCall  = py ? "def dogHouse():\n    for i in range(4):\n        forward()\n        forward()\n        left()\n"
                         : "void dogHouse() {\n  for (int i = 0; i < 4; i++) {\n    stepForward();\n    stepForward();\n    left();\n  }\n}\n";
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
