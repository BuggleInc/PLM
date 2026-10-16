package plm.core.ui;

import java.util.Locale;
import javax.swing.JFrame;
import plm.core.HumanLangChangesListener;
import plm.core.ProgLangChangesListener;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Exercise.WorldKind;
import plm.core.model.lesson.Lecture;
import plm.universe.World;

/** Shows the documentation of the world of the current exercise (Ctrl-H), which depends on both the human and programming languages. */
public class AboutWorldDialog extends AbstractAboutDialog implements ProgLangChangesListener, HumanLangChangesListener {

  private static final long serialVersionUID = 1766486738385426108L;

  public AboutWorldDialog(JFrame parent)
  {
    super(parent);
    currentExerciseHasChanged(Game.getInstance().getCurrentLesson().getCurrentExercise());
    Game.getInstance().addProgLangListener(this);
    Game.getInstance().addHumanLangListener(this);
  }

  @Override public void currentExerciseHasChanged(Lecture lecture) { refresh(lecture, 0); }

  @Override public void currentProgrammingLanguageHasChanged(ProgrammingLanguage newLang)
  {
    refresh(Game.getInstance().getCurrentLesson().getCurrentExercise(), area.getCaretPosition());
  }

  @Override public void currentHumanLanguageHasChanged(Locale newLang)
  {
    refresh(Game.getInstance().getCurrentLesson().getCurrentExercise(), area.getCaretPosition());
  }

  /** Shows the documentation of the first world of the lecture at the given position, or hides the dialog if the lecture has no world. */
  private void refresh(Lecture lecture, int caret)
  {
    if (!(lecture instanceof Exercise exo)) {
      // FIXME: should disable the entry menu when seing a lecture, and close any preexisting window when switching to a lecture
      setVisible(false);
      return;
    }
    World world = exo.getWorlds(WorldKind.CURRENT).get(0);
    setTitle(Game.i18n.tr("About world - {0}", world.getClass().getSimpleName()));
    area.setText(world.getAbout());
    area.setCaretPosition(Math.min(caret, area.getDocument().getLength()));
  }
}
