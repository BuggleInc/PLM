package plm.universe.posix;

import java.awt.BorderLayout;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import plm.universe.EntityControlPanel;
import plm.universe.IWorldView;

/**
 * Shows the trace of a world under its view: what happened so far (the system calls of the student's process with their result,
 * and the steps of the peers), then the steps in which the peers wait, stamped with their start, and finally the remaining steps
 * of the peers' scripts, greyed and aligned with the events but without timestamp, since their time depends on the student's
 * process.
 */
public class PosixTracePanel extends EntityControlPanel implements IWorldView {
  private static final long serialVersionUID = 1L;
  private final PosixWorld world;
  private final JEditorPane text = new JEditorPane("text/html", "");

  public PosixTracePanel(PosixWorld world)
  {
    this.world = world;
    text.setEditable(false);
    setLayout(new BorderLayout());
    add(new JScrollPane(text), BorderLayout.CENTER);
    world.addWorldUpdatesListener(this);
    refresh();
  }

  @Override public void worldHasMoved() { SwingUtilities.invokeLater(this::refresh); } // the execution threads move the world

  @Override public void worldHasChanged() { worldHasMoved(); }

  private void refresh()
  {
    boolean redacted   = world.redacted();
    StringBuilder html = new StringBuilder("<html><body style='font-family:monospace'>");
    for (PosixWorld.Event event : world.trace)
      html.append(escape("[t=" + event.time() + "] " + event.who() + ": " + event.shown(redacted))).append("<br>");
    for (Peer p : world.peers) // the peers act as far as they can, so what they do now is waiting
      if (p.ongoing() != null)
        html.append(escape("[t=" + p.since + "…] " + (redacted ? PosixWorld.redacted(p.ongoing(), false) : p.ongoing()) + " (ongoing)")).append("<br>");
    html.append("<span style='color:gray'>");
    String blank = "&nbsp;".repeat(("[t=" + world.now + "] ").length()); // the place of the timestamp in the monospace font
    for (Peer p : world.peers)
      for (String step : p.upcoming())
        html.append(blank).append(escape(redacted ? PosixWorld.redacted(step, false) : step)).append("<br>");
    text.setText(html.append("</span></body></html>").toString());
    text.setCaretPosition(currentPoint(world.trace.size()));
  }

  /**
   * The position in the document after that many lines (the past events), where the execution is: the caret goes there so that
   * the panel shows it, which is the beginning before any event.
   */
  private int currentPoint(int lines)
  {
    try {
      String content = text.getDocument().getText(0, text.getDocument().getLength()); // each <br> is a line break there
      int offset     = 0;
      for (int i = 0; i < lines; i++) {
        offset = content.indexOf('\n', offset + 1);
        if (offset < 0)
          return content.length();
      }
      return offset;
    } catch (BadLocationException e) {
      return 0; // cannot happen, as the whole document is read
    }
  }

  private static String escape(String line) { return line.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;"); }

  @Override public void setEnabledControl(boolean enabled) { /* nothing to control */ }

  @Override public void dispose() { world.removeWorldUpdatesListener(this); }
}
