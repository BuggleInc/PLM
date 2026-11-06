package plm.core.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import org.fife.ui.rtextarea.RTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;
import plm.core.model.Game;

/**
 * The zoom of the texts, changed with Ctrl (Cmd on macOS) and plus, minus or 0 in the windows where it is bound. It scales the
 * font of the code editors and the one of the HTML panes of a {@link PlmHtmlEditorKit}, live in every window, and is saved in the
 * properties as a number of steps.
 */
public final class Zoom {
  private static final double STEP   = 1.1;
  private static final int MIN_LEVEL = -4;
  private static final int MAX_LEVEL = 8;
  /** The size of the HTML texts without zoom, in points: as big as a CSS font-size of 10px, as the px of Swing are bigger */
  private static final float HTML_SIZE = 13;

  private Zoom() {}

  /** The number of steps of the zoom, 0 when unset or invalid */
  private static int level()
  {
    try {
      return Math.max(MIN_LEVEL, Math.min(MAX_LEVEL, Integer.parseInt(Game.getProperty(Game.PROP_ZOOM, "0", false))));
    } catch (NumberFormatException e) {
      return 0;
    }
  }

  private static int scaled(float size) { return (int)Math.round(size * Math.pow(STEP, level())); }

  /** The font of the HTML panes, whose CSS gives the family */
  static Font htmlFont() { return new Font(Font.SANS_SERIF, Font.PLAIN, scaled(HTML_SIZE)); }

  /** Gives the zoomed fonts to the code editors and to the HTML panes of that component and of its descendants */
  public static void apply(Component component)
  {
    if (component instanceof RTextScrollPane editor) {
      Font font = RTextArea.getDefaultFont();
      font      = font.deriveFont((float)scaled(font.getSize2D()));
      editor.getTextArea().setFont(font);
      editor.getGutter().setLineNumberFont(font);
    } else if (component instanceof JEditorPane pane && pane.getEditorKit() instanceof PlmHtmlEditorKit)
      pane.setFont(htmlFont());
    if (component instanceof Container container)
      for (Component child : container.getComponents())
        apply(child);
  }

  /** Changes the zoom to that number of steps, in every window */
  private static void setLevel(int level)
  {
    Game.setProperty(Game.PROP_ZOOM, String.valueOf(Math.max(MIN_LEVEL, Math.min(MAX_LEVEL, level))));
    for (Window window : Window.getWindows())
      apply(window);
  }

  /** Binds the shortcuts of the zoom in that window, whatever has the focus in it (but the numeric keypad, which folds the code) */
  public static void bindKeys(JRootPane root)
  {
    bind(root, "zoomIn", () -> setLevel(level() + 1), KeyEvent.VK_EQUALS, KeyEvent.VK_PLUS);
    bind(root, "zoomOut", () -> setLevel(level() - 1), KeyEvent.VK_MINUS);
    bind(root, "zoomReset", () -> setLevel(0), KeyEvent.VK_0);
  }

  private static void bind(JRootPane root, String name, Runnable change, int... keys)
  {
    int modifier = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
    for (int key : keys) {
      root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(key, modifier), name);
      root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(key, modifier | InputEvent.SHIFT_DOWN_MASK), name); // + is shifted
    }
    root.getActionMap().put(name, new AbstractAction() {
      private static final long serialVersionUID = 1L;
      @Override public void actionPerformed(ActionEvent e)
      {
        change.run();
      }
    });
  }
}
