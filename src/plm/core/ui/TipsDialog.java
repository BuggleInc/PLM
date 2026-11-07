package plm.core.ui;

import java.awt.event.KeyEvent;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import plm.core.model.Game;
import plm.core.model.lesson.Lecture;

@SuppressWarnings("serial")
public class TipsDialog extends AbstractAboutDialog {

  public TipsDialog(JFrame parent)
  {
    super(parent);
    currentExerciseHasChanged(Game.getInstance().getCurrentLesson().getCurrentExercise());
    area.setEditorKit(new PlmHtmlEditorKit(Game.getInstance().getCurrentLesson().getCurrentExercise()));
    getRootPane().registerKeyboardAction(e -> dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
  }

  @Override public void currentExerciseHasChanged(Lecture lect)
  {
    setTitle(Game.i18n.tr("Tips"));
    this.area.setText(Game.i18n.tr("(no tips to display)"));
    this.area.setCaretPosition(0);
  }

  public void setText(String txt)
  {
    this.area.setText(txt);
    this.area.setCaretPosition(0);
  }
}
