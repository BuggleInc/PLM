package plm.test.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.Icon;
import javax.swing.SwingUtilities;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.GutterIconInfo;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.junit.jupiter.api.Test;
import plm.core.lang.CompilerMessage;
import plm.core.ui.CompilerMarkers;

/** The messages of the compiler in the margin of an editor, seen through the API of its gutter, without showing it in a window */
public class CompilerMarkersTest {
  private final RSyntaxTextArea code    = new RSyntaxTextArea("first\nsecond\nthird\nfourth\n");
  private final RTextScrollPane editor  = new RTextScrollPane(code);
  private final CompilerMarkers markers = new CompilerMarkers(editor);

  public CompilerMarkersTest()
  {
    editor.setSize(400, 300);
    editor.doLayout();
    editor.getViewport().doLayout();
  }

  /** The icons on that line of the editor (counted from 1) */
  private GutterIconInfo[] iconsOn(int line) throws Exception { return editor.getGutter().getTrackingIcons(new Point(0, code.yForLine(line - 1))); }

  /** The color at the left of the disc of the icon, where no symbol is drawn */
  private static Color colorOf(Icon icon)
  {
    BufferedImage image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
    icon.paintIcon(new RSyntaxTextArea(), image.getGraphics(), 0, 0);
    return new Color(image.getRGB(3, icon.getIconHeight() / 2), true);
  }

  @Test public void theMessagesOfALineShareOneIconOfTheMostSevereColor() throws Exception
  {
    markers.show(List.of(new CompilerMessage(2, true, true, "unused <x>"), new CompilerMessage(2, true, false, "undeclared\nnote: here"),
                         new CompilerMessage(3, true, true, "deprecated"), new CompilerMessage(1, false, false, "call left() once")));
    assertEquals(1, iconsOn(1).length);
    assertTrue(iconsOn(1)[0].getToolTip().contains("call left() once"), iconsOn(1)[0].getToolTip());
    GutterIconInfo[] second = iconsOn(2);
    assertEquals(1, second.length);
    assertTrue(second[0].getToolTip().contains("unused &lt;x&gt;<br>"), second[0].getToolTip());
    assertTrue(second[0].getToolTip().contains("undeclared<br>&nbsp;&nbsp;&nbsp;&nbsp;note: here"), second[0].getToolTip());
    assertEquals(new Color(0xD3, 0x2F, 0x2F), colorOf(second[0].getIcon()));
    assertEquals(new Color(0xF9, 0xA8, 0x25), colorOf(iconsOn(3)[0].getIcon()));
    assertEquals(0, iconsOn(4).length);
  }

  @Test public void aNewCompilationReplacesTheIcons() throws Exception
  {
    markers.show(List.of(new CompilerMessage(2, true, false, "undeclared")));
    markers.show(List.of(new CompilerMessage(3, true, true, "deprecated")));
    assertEquals(0, iconsOn(2).length);
    assertEquals(1, iconsOn(3).length);
    markers.show(List.of());
    assertEquals(0, iconsOn(3).length);
  }

  @Test public void editingTheCodeMovesAndDimsTheIcons() throws Exception
  {
    markers.show(List.of(new CompilerMessage(3, true, false, "undeclared")));
    code.insert("added\n", 0);
    SwingUtilities.invokeAndWait(() -> {}); // the icons are dimmed once the document changed
    assertEquals(0, iconsOn(3).length);
    String tip = iconsOn(4)[0].getToolTip();
    assertTrue(tip.startsWith("<html><i>Potentially obsolete, please recompile.</i><br>") && tip.contains("undeclared"), tip);
    assertTrue(colorOf(iconsOn(4)[0].getIcon()).getAlpha() < 128);
  }
}
