package plm.test.gui;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.fife.ui.rtextarea.GutterIconInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import plm.core.model.Game;
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

  /** Running a code that does not compile puts its errors in the margin of the editor (in creative mode, to skip the dialogs) */
  @Test public void theCompilationErrorsGoToTheMarginOfTheEditor() throws Exception
  {
    Game game = Game.getInstance();
    game.switchLesson("lessons.welcome", true);
    game.setProgrammingLanguage(game.programmingLanguageManager.JAVA);
    GuiActionRunner.execute(
        () -> ((JavaEditorPanel)MainFrame.getInstance().getEditorTabs().getComponentAt(1)).getTextArea().setText("public void run() {\n  notDeclared();\n}\n"));
    game.switchCreative();
    try {
      game.startExerciseExecution();
      for (int i = 0; i < 600 && marginTips().isEmpty(); i++)
        Thread.sleep(100);
      Assertions.assertFalse(marginTips().isEmpty(), "no compilation error in the margin");
      Assertions.assertTrue(marginTips().stream().anyMatch(tip -> tip.contains("notDeclared")), marginTips().toString());
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
