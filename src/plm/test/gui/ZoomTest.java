package plm.test.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import plm.core.model.Game;
import plm.core.ui.PlmHtmlEditorKit;
import plm.core.ui.Zoom;

/** The zoom of the texts, through the actions bound in a window that holds a code editor and an HTML pane, without showing it */
public class ZoomTest {
  private final JFrame frame         = new JFrame();
  private final RTextScrollPane code = new RTextScrollPane(new RSyntaxTextArea());
  private final JEditorPane html     = PlmHtmlEditorKit.pane();
  private static final int CODE_SIZE = RTextArea.getDefaultFont().getSize();
  private static final int HTML_SIZE = 13;

  public ZoomTest()
  {
    frame.add(code, BorderLayout.CENTER);
    frame.add(html, BorderLayout.NORTH);
    Zoom.apply(code);
    Zoom.bindKeys(frame.getRootPane());
  }

  @AfterEach public void reset()
  {
    press("zoomReset");
    frame.dispose();
  }

  private void press(String action) { frame.getRootPane().getActionMap().get(action).actionPerformed(null); }

  @Test public void theShortcutsAreBoundInTheWindow()
  {
    int ctrl = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
    assertEquals("zoomIn", frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).get(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, ctrl)));
    assertEquals(
        "zoomIn",
        frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).get(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, ctrl | InputEvent.SHIFT_DOWN_MASK)));
    assertEquals("zoomIn", frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).get(KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, ctrl)));
    assertEquals("zoomOut", frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).get(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, ctrl)));
    assertEquals("zoomReset", frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).get(KeyStroke.getKeyStroke(KeyEvent.VK_0, ctrl)));
  }

  @Test public void theCodeAndTheHtmlAreZoomedTogetherAndTheZoomIsSaved()
  {
    assertEquals(CODE_SIZE, code.getTextArea().getFont().getSize());
    assertEquals(HTML_SIZE, html.getFont().getSize());
    for (int i = 0; i < 3; i++)
      press("zoomIn");
    assertEquals((int)Math.round(CODE_SIZE * Math.pow(1.1, 3)), code.getTextArea().getFont().getSize());
    assertEquals(code.getTextArea().getFont(), code.getGutter().getLineNumberFont());
    assertEquals((int)Math.round(HTML_SIZE * Math.pow(1.1, 3)), html.getFont().getSize());
    assertEquals("3", Game.getProperty(Game.PROP_ZOOM, "", false));
    press("zoomOut");
    assertEquals("2", Game.getProperty(Game.PROP_ZOOM, "", false));
    press("zoomReset");
    assertEquals(CODE_SIZE, code.getTextArea().getFont().getSize());
    assertEquals(HTML_SIZE, html.getFont().getSize());
  }

  @Test public void aNewPaneFollowsTheZoom()
  {
    press("zoomIn");
    assertEquals(html.getFont(), PlmHtmlEditorKit.pane().getFont());
  }

  @Test public void theZoomIsBounded()
  {
    for (int i = 0; i < 20; i++)
      press("zoomIn");
    assertEquals("8", Game.getProperty(Game.PROP_ZOOM, "", false));
    for (int i = 0; i < 20; i++)
      press("zoomOut");
    assertEquals("-4", Game.getProperty(Game.PROP_ZOOM, "", false));
    assertTrue(html.getFont().getSize() > 0);
  }

  @Test public void anInvalidZoomInThePropertiesIsIgnored()
  {
    Game.setProperty(Game.PROP_ZOOM, "big");
    assertEquals(HTML_SIZE, PlmHtmlEditorKit.pane().getFont().getSize());
  }
}
