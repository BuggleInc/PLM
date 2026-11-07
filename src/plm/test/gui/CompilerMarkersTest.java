package plm.test.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Icon;
import javax.swing.SwingUtilities;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.parser.ParserNotice;
import org.fife.ui.rtextarea.GutterIconInfo;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.junit.jupiter.api.Test;
import plm.core.lang.CompilerMessage;
import plm.core.ui.CompilerMarkers;

/**
 * The messages of the compiler in the margin of an editor and underlined in its code, seen through the API of its gutter and of its
 * parser notices, without showing it in a window
 */
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
    markers.show(List.of(new CompilerMessage(2, 0, true, true, "unused <x>"), new CompilerMessage(2, 0, true, false, "undeclared\nnote: here"),
                         new CompilerMessage(3, 0, true, true, "deprecated"), new CompilerMessage(1, 0, false, false, "call left() once")));
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
    markers.show(List.of(new CompilerMessage(2, 0, true, false, "undeclared")));
    markers.show(List.of(new CompilerMessage(3, 0, true, true, "deprecated")));
    assertEquals(0, iconsOn(2).length);
    assertEquals(1, iconsOn(3).length);
    markers.show(List.of());
    assertEquals(0, iconsOn(3).length);
  }

  @Test public void editingTheCodeMovesAndDimsTheIcons() throws Exception
  {
    markers.show(List.of(new CompilerMessage(3, 0, true, false, "undeclared")));
    code.insert("added\n", 0);
    SwingUtilities.invokeAndWait(() -> {}); // the icons are dimmed once the document changed
    assertEquals(0, iconsOn(3).length);
    String tip = iconsOn(4)[0].getToolTip();
    assertTrue(tip.startsWith("<html><i>Potentially obsolete, please recompile.</i><br>") && tip.contains("undeclared"), tip);
    assertTrue(colorOf(iconsOn(4)[0].getIcon()).getAlpha() < 128);
  }

  @Test public void aNewCompilationAfterAnEditShowsBrightIcons() throws Exception
  {
    markers.show(List.of(new CompilerMessage(3, 0, true, false, "undeclared")));
    code.insert("added\n", 0);
    SwingUtilities.invokeAndWait(() -> {});
    markers.show(List.of(new CompilerMessage(3, 0, true, false, "undeclared")));
    assertFalse(iconsOn(3)[0].getToolTip().contains("obsolete"), iconsOn(3)[0].getToolTip());
    assertEquals(255, colorOf(iconsOn(3)[0].getIcon()).getAlpha());
    assertEquals(0, iconsOn(4).length);
  }

  /** The code may have got shorter since its compilation */
  @Test public void aMessageBeyondTheEndOfTheCodeIsOnItsLastLine() throws Exception
  {
    markers.show(List.of(new CompilerMessage(9, 0, true, false, "undeclared")));
    assertEquals(1, iconsOn(5).length);
  }

  /** The notices of the parser of the editor, read in the Swing thread once the tasks queued before (such as the dimming) are done */
  private List<ParserNotice> notices() throws Exception
  {
    List<ParserNotice> notices = new ArrayList<>();
    SwingUtilities.invokeAndWait(() -> notices.addAll(code.getParserNotices()));
    return notices;
  }

  /** The underlines of the code, as "line:offset:length" (counted from 0, without offset nor length for a whole line) */
  private List<String> underlines() throws Exception
  {
    return notices()
        .stream()
        .map(notice -> notice.getLine() + (notice.getKnowsOffsetAndLength() ? ":" + notice.getOffset() + ":" + notice.getLength() : ""))
        .toList();
  }

  /** Puts that code in the editor, then shows these messages, in the Swing thread like the PLM (where the parser works too) */
  private void compiled(String text, CompilerMessage... messages) throws Exception
  {
    SwingUtilities.invokeAndWait(() -> code.setText(text)); // and the dimming that it queues, which would dim the new messages
    SwingUtilities.invokeAndWait(() -> markers.show(List.of(messages)));
  }

  @Test public void theTokenAtTheColumnOfAMessageIsUnderlined() throws Exception
  {
    code.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
    compiled("int a = 1;\n  toto = \"a string\";\n", new CompilerMessage(2, 3, true, false, "undeclared"), new CompilerMessage(2, 10, true, true, "a warning"));
    assertEquals(List.of("1:13:4", "1:20:10"), underlines()); // toto, and the whole string
    ParserNotice error = notices().get(0);
    assertEquals(ParserNotice.Level.ERROR, error.getLevel());
    assertEquals(new Color(0xD3, 0x2F, 0x2F), error.getColor());
    assertTrue(error.getToolTipText().contains("undeclared"), error.getToolTipText());
    ParserNotice warning = notices().get(1);
    assertEquals(ParserNotice.Level.WARNING, warning.getLevel());
    assertEquals(new Color(0xF9, 0xA8, 0x25), warning.getColor());
  }

  @Test public void aMessageWithoutColumnOrPointingAtBlanksUnderlinesItsLineAndAnUnsureOneNothing() throws Exception
  {
    compiled("first\n  second\nthird\n", new CompilerMessage(1, 0, true, false, "no column"), new CompilerMessage(2, 1, true, false, "at blanks"),
             new CompilerMessage(3, 9, true, false, "beyond the line"), new CompilerMessage(1, 0, false, false, "unsure"));
    assertEquals(List.of("0", "1", "2"), underlines());
  }

  @Test public void editingTheCodeMovesAndDimsTheUnderlinesUntilTheNextCompilation() throws Exception
  {
    compiled("first\nsecond\nthird\n", new CompilerMessage(2, 1, true, false, "undeclared"));
    SwingUtilities.invokeAndWait(() -> code.insert("added\n", 0));
    assertEquals(List.of("2:12:6"), underlines());
    ParserNotice notice = notices().get(0);
    assertTrue(notice.getColor().getAlpha() < 128);
    assertTrue(notice.getToolTipText().contains("obsolete"), notice.getToolTipText());
    SwingUtilities.invokeAndWait(() -> markers.show(List.of()));
    assertEquals(List.of(), underlines());
  }
}
