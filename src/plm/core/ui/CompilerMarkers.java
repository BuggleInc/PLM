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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javax.swing.Icon;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import org.fife.ui.rtextarea.GutterIconInfo;
import org.fife.ui.rtextarea.RTextScrollPane;
import plm.core.lang.CompilerMessage;
import plm.core.model.Game;

/**
 * The messages of the compiler as icons in the margin of an editor, each one listing the messages of its line in its tooltip. The
 * icons follow their lines when the code changes, but they are then dimmed, as their messages may be obsolete.
 */
public class CompilerMarkers implements DocumentListener {
  private final RTextScrollPane editor;
  private final List<GutterIconInfo> icons = new ArrayList<>();

  public CompilerMarkers(RTextScrollPane editor)
  {
    this.editor = editor;
    editor.setIconRowHeaderEnabled(true);
    editor.getTextArea().getDocument().addDocumentListener(this);
  }

  /** Replaces the icons by the ones of these messages, grouped by line */
  public void show(List<CompilerMessage> messages)
  {
    editor.getGutter().removeAllTrackingIcons();
    icons.clear();
    Map<Integer, List<CompilerMessage>> byLine = messages.stream().collect(Collectors.groupingBy(CompilerMessage::line, TreeMap::new, Collectors.toList()));
    int size                                   = Math.min(editor.getTextArea().getLineHeight(), 16); // the width of the icon row
    byLine.forEach((line, onLine) -> add(line - 1, new Bullet(onLine, false, size)));
  }

  /** Adds the icon on that line (counted from 0), or on the last line if the code got shorter */
  private void add(int line, Bullet bullet)
  {
    try {
      icons.add(editor.getGutter().addLineTrackingIcon(Math.min(line, editor.getTextArea().getLineCount() - 1), bullet, bullet.tip()));
    } catch (BadLocationException e) {
      throw new IllegalStateException(e); // never happens, as the line exists
    }
  }

  /** Replaces the icons by dimmed ones, on the lines where the changes of the code moved them */
  private void dim()
  {
    if (icons.isEmpty() || ((Bullet)icons.get(0).getIcon()).dimmed())
      return;
    List<GutterIconInfo> bright = new ArrayList<>(icons);
    editor.getGutter().removeAllTrackingIcons();
    icons.clear();
    Element lines = editor.getTextArea().getDocument().getDefaultRootElement();
    for (GutterIconInfo info : bright) {
      Bullet bullet = (Bullet)info.getIcon();
      add(lines.getElementIndex(info.getMarkedOffset()), new Bullet(bullet.messages(), true, bullet.size()));
    }
  }

  @Override public void insertUpdate(DocumentEvent e) { SwingUtilities.invokeLater(this::dim); } // not while the document is changing
  @Override public void removeUpdate(DocumentEvent e) { SwingUtilities.invokeLater(this::dim); }
  @Override public void changedUpdate(DocumentEvent e) {} // the style of the code, not its content

  /**
   * The icon of the messages of a line: a red disc with a white cross if one of them is an error, a yellow one with a ! otherwise. It
   * holds a ? instead if one of them is not sure of its line (and is then on the first line).
   */
  record Bullet(List<CompilerMessage> messages, boolean dimmed, int size) implements Icon {
    private static final Color RED    = new Color(0xD3, 0x2F, 0x2F);
    private static final Color YELLOW = new Color(0xF9, 0xA8, 0x25);

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
      boolean sure = messages.stream().allMatch(CompilerMessage::sure);
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

    /** The messages, each one after a bullet of its color and with its notes indented, below a warning when dimmed */
    String tip()
    {
      StringBuilder tip = new StringBuilder("<html>");
      if (dimmed) {
        // Translator: tooltip of the compiler messages in the margin of the editor, once the code changed since their compilation
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
  }
}
