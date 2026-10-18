package plm.core.ui;

import java.util.Locale;
import javax.swing.JTextArea;
import plm.core.HumanLangChangesListener;
import plm.core.model.Game;
import plm.core.model.LogWriter;

public class LoggerPanel extends JTextArea implements LogWriter, HumanLangChangesListener {

  private static final long serialVersionUID = 468774822833769775L;

  public LoggerPanel(Game game)
  {
    super();
    setEditable(false);
    setToolTipText(Game.i18n.tr("Where error and other messages get written"));
    game.setOutputWriter(this);
    game.addHumanLangListener(this);
  }

  public void clear() { setText(null); }

  @Override public void log(String msg) { append(msg); }

  /**
   * Add an exception into the text area
   *
   * @param e
   *            what to log
   */
  public void log(Exception e)
  {
    append(e.toString() + "\n");
    for (StackTraceElement s : e.getStackTrace()) {
      if (s.getClassName().contains("bugglequest.BugglePanel"))
        break;
      append("  in " + s.getClassName() + "." + s.getMethodName() + " at " + s.getFileName() + ":" + s.getLineNumber() + "\n");
    }
    Throwable t = e.getCause();
    if (t != null) {
      append("Caused by:\n  " + t.toString() + "\n");
      for (StackTraceElement s : t.getStackTrace()) {
        if (s.getClassName().contains("bugglequest.BugglePanel"))
          break;
        append("    in " + s.getClassName() + "." + s.getMethodName() + " at " + s.getFileName() + ":" + s.getLineNumber() + "\n");
      }
    }
  }

  @Override public void currentHumanLanguageHasChanged(Locale newLang) { setToolTipText(Game.i18n.tr("Where error and other messages get written")); }
}
