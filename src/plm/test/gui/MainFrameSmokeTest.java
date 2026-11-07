package plm.test.gui;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.timing.Condition;
import org.assertj.swing.timing.Pause;
import org.fife.ui.rtextarea.GutterIconInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.core.model.lesson.Exercise;
import plm.core.ui.JavaEditorPanel;
import plm.core.ui.MainFrame;

/**
 * GUI smoke test: launch PLM's main window and check that it actually comes up.
 *
 * Run under a display (the CI test job uses xvfb on Linux and the native display
 * on Windows/macOS), this drives the full Swing stack -- not just the headless
 * engine initialisation the other tests do.
 */
public class MainFrameSmokeTest {

  private FrameFixture window;

  @BeforeEach public void setUp()
  {
    Game.getInstance(); // build the model (also initialises the languages)
    MainFrame frame = GuiActionRunner.execute(() -> MainFrame.getInstance());
    window          = new FrameFixture(frame);
    window.show();
  }

  @Test public void theMainWindowComesUp() { window.requireVisible(); }

  /** The tooltips of the icons in the margin of the editor of the first tab of code */
  private static List<String> marginTips()
  {
    return GuiActionRunner.execute(() -> {
      JavaEditorPanel editor = (JavaEditorPanel)MainFrame.getInstance().getEditorTabs().getComponentAt(1);
      List<String> tips      = new ArrayList<>();
      for (int line = 0; line < editor.getTextArea().getLineCount(); line++)
        for (GutterIconInfo icon : editor.getGutter().getTrackingIcons(new Point(0, editor.getTextArea().yForLine(line))))
          tips.add(icon.getToolTip());
      return tips;
    });
  }

  /**
   * Running a code that does not compile puts its errors in the margin of the editor (in creative mode, to skip the dialogs). The
   * code is given to the exercise once the views are built for it, as they rebuild the editors when the exercise or the language
   * changes, and the session may have restored another exercise, or a lecture without code.
   */
  @Test public void theCompilationErrorsGoToTheMarginOfTheEditor() throws Exception
  {
    Game game                = Game.getInstance();
    ProgrammingLanguage java = game.programmingLanguageManager.JAVA;
    Exercise exo             = (Exercise)game.switchLesson("lessons.welcome", true).exercises().stream().filter(l -> l instanceof Exercise).findFirst().get();
    game.setCurrentExercise(exo);
    game.setProgrammingLanguage(java);
    window.robot().waitForIdle();
    exo.setEditorContent(java, "notDeclared();\n"); // the code of the first exercise (Environment) is the body of run()
    game.switchCreative();
    try {
      game.startExerciseExecution();
      Pause.pause(new Condition("a compilation error in the margin") {
        @Override public boolean test()
        {
          return !marginTips().isEmpty();
        }
      }, 60_000);
      List<String> tips = marginTips();
      Assertions.assertTrue(tips.stream().anyMatch(tip -> tip.contains("notDeclared")), tips.toString());
    } finally {
      game.switchCreative();
    }
  }

  @AfterEach public void tearDown()
  {
    if (window != null)
      window.cleanUp();
  }
}
