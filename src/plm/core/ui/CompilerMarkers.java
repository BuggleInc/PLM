package plm.core.ui;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javax.swing.Icon;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.Position;
import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.parser.AbstractParser;
import org.fife.ui.rsyntaxtextarea.parser.DefaultParseResult;
import org.fife.ui.rsyntaxtextarea.parser.DefaultParserNotice;
import org.fife.ui.rsyntaxtextarea.parser.ParseResult;
import org.fife.ui.rsyntaxtextarea.parser.ParserNotice;
import org.fife.ui.rtextarea.RTextScrollPane;
import plm.core.lang.CompilerMessage;
import plm.core.model.Game;

/**
 * The messages of the compiler in an editor: icons in its margin, each one listing the messages of its line in its tooltip, and
 * wavy underlines in the code, of the token at the column of their message or of its whole line, with the message as tooltip (but
 * for the messages unsure of their line). They follow their lines when the code changes, but they are then dimmed, as their messages
 * may be obsolete. The underlines are the notices of this parser of RSyntaxTextArea, which calls it again after each change.
 */
public class CompilerMarkers extends AbstractParser implements DocumentListener {
  private static final Color RED    = new Color(0xD3, 0x2F, 0x2F);
  private static final Color YELLOW = new Color(0xF9, 0xA8, 0x25);

  private final RTextScrollPane editor;
  private final RSyntaxTextArea code;
  /** The messages of the last compilation, with the place of their token or line */
  private List<Mark> marks = List.of();
  /** Whether the code changed since that compilation */
  private boolean dimmed;

  /** A message from that position, which follows the changes of the code, on the length of its token (0 for its whole line) */
  private record Mark(CompilerMessage message, Position start, int length) {}

  public CompilerMarkers(RTextScrollPane editor)
  {
    this.editor = editor;
    this.code   = (RSyntaxTextArea)editor.getTextArea();
    editor.setIconRowHeaderEnabled(true);
    code.getDocument().addDocumentListener(this);
    code.addParser(this);
    code.setUseFocusableTips(false); // the tooltips of the underlines look like the ones of the icons
  }

  /** Replaces the icons and the underlines by the ones of these messages */
  public void show(List<CompilerMessage> messages)
  {
    marks  = messages.stream().map(this::mark).toList();
    dimmed = false;
    refresh();
  }

  /** Dims the icons and the underlines, as their messages may be obsolete */
  private void dim()
  {
    if (marks.isEmpty() || dimmed)
      return;
    dimmed = true;
    refresh();
  }

  /** The mark of that message: the token at its column, or its whole line if it has none or if it points at blanks */
  private Mark mark(CompilerMessage message)
  {
    Element line =
        code.getDocument().getDefaultRootElement().getElement(Math.min(Math.max(message.line(), 1), code.getLineCount()) - 1); // first line if unknown
    int offset    = line.getStartOffset() + message.column() - 1;
    Token token   = message.column() > 0 && offset < line.getEndOffset() - 1 ? code.modelToToken(offset) : null;
    boolean whole = token == null || token.isWhitespace();
    try {
      return new Mark(message, code.getDocument().createPosition(whole ? line.getStartOffset() : token.getOffset()), whole ? 0 : token.length());
    } catch (BadLocationException e) {
      throw new IllegalStateException(e); // never happens, as the offset is in the document
    }
  }

  /**
   * Replaces the icons by the ones of the marks, grouped on the current line of their position, which they then follow, and the
   * underlines by asking RSyntaxTextArea to parse again
   */
  private void refresh()
  {
    editor.getGutter().removeAllTrackingIcons();
    Element lines = code.getDocument().getDefaultRootElement();
    int size      = Math.min(code.getLineHeight(), 16); // the width of the icon row
    marks.stream()
        .collect(Collectors.groupingBy(
            mark -> lines.getElementIndex(mark.start().getOffset()), TreeMap::new, Collectors.mapping(Mark::message, Collectors.toList())))
        .forEach((line, messages) -> {
          try {
            editor.getGutter().addLineTrackingIcon(line, new Bullet(messages, dimmed, size), tip(messages, dimmed));
          } catch (BadLocationException e) {
            throw new IllegalStateException(e); // never happens, as the line exists
          }
        });
    code.forceReparsing(this);
  }

  /** The underlines of the messages sure of their line, on the current lines of their positions */
  @Override public ParseResult parse(RSyntaxDocument document, String style)
  {
    DefaultParseResult result = new DefaultParseResult(this);
    Element lines             = document.getDefaultRootElement();
    for (Mark mark : marks) {
      CompilerMessage message = mark.message();
      if (message.line() == 0)
        continue;
      int line                   = lines.getElementIndex(mark.start().getOffset());
      DefaultParserNotice notice = mark.length() == 0 ? new DefaultParserNotice(this, message.text(), line)
                                                      : new DefaultParserNotice(this, message.text(), line, mark.start().getOffset(), mark.length());
      Color color                = message.warning() ? YELLOW : RED;
      notice.setLevel(message.warning() ? ParserNotice.Level.WARNING : ParserNotice.Level.ERROR);
      notice.setColor(dimmed ? new Color(color.getRed(), color.getGreen(), color.getBlue(), 90) : color);
      notice.setToolTipText(tip(List.of(message), dimmed));
      result.addNotice(notice);
    }
    return result;
  }

  @Override public void insertUpdate(DocumentEvent e) { SwingUtilities.invokeLater(this::dim); } // not while the document is changing
  @Override public void removeUpdate(DocumentEvent e) { SwingUtilities.invokeLater(this::dim); }
  @Override public void changedUpdate(DocumentEvent e) {} // the style of the code, not its content

  /** The messages, each one after a bullet of its color and with its notes indented, below a warning when dimmed */
  private static String tip(List<CompilerMessage> messages, boolean dimmed)
  {
    StringBuilder tip = new StringBuilder("<html>");
    if (dimmed) {
      // Translator: tooltip of the compiler messages in the margin and in the code of the editor, once the code changed since their compilation
      tip.append("<i>").append(escape(Game.i18n.tr("Potentially obsolete, please recompile."))).append("</i><br>");
    }
    for (CompilerMessage message : messages) {
      String color = String.format("#%06x", (message.warning() ? YELLOW : RED).getRGB() & 0xFFFFFF);
      tip.append("<font color='").append(color).append("'>&#9679;</font> ");
      tip.append(escape(message.text()).replace("\n", "<br>&nbsp;&nbsp;&nbsp;&nbsp;")).append("<br>");
    }
    return tip.substring(0, tip.length() - "<br>".length()) + "</html>";
  }

  private static String escape(String text) { return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;"); }

  /**
   * The icon of the messages of a line: a red disc with a white cross if one of them is an error, a yellow one with a ! otherwise. It
   * holds a ? instead if one of them is not sure of its line (and is then on the first line).
   */
  record Bullet(List<CompilerMessage> messages, boolean dimmed, int size) implements Icon {
    private boolean error() { return messages.stream().anyMatch(message -> !message.warning()); }

    @Override public int getIconWidth() { return size; }
    @Override public int getIconHeight() { return size; }

    @Override public void paintIcon(Component c, Graphics g, int x, int y)
    {
      Graphics2D g2 = (Graphics2D)g.create();
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      if (dimmed)
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.35f));
      g2.setColor(error() ? RED : YELLOW);
      g2.fillOval(x + 1, y + 1, size - 2, size - 2);
      g2.setColor(error() ? Color.WHITE : Color.BLACK);
      boolean sure = messages.stream().allMatch(message -> message.line() > 0);
      if (sure && error()) {
        g2.setStroke(new BasicStroke(size / 8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        int from = x + size * 5 / 16, to = x + size - size * 5 / 16, top = y + size * 5 / 16, bottom = y + size - size * 5 / 16;
        g2.drawLine(from, top, to, bottom);
        g2.drawLine(from, bottom, to, top);
      } else {
        String symbol = sure ? "!" : "?";
        g2.setFont(c.getFont().deriveFont(Font.BOLD, size * 0.75f));
        FontMetrics metrics = g2.getFontMetrics();
        g2.drawString(symbol, x + (size - metrics.stringWidth(symbol)) / 2f, y + (size - metrics.getHeight()) / 2f + metrics.getAscent());
      }
      g2.dispose();
    }
  }
}
