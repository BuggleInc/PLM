package plm.core.ui.action;

import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.BrokenProgrammingLanguageException;
import plm.core.model.Game;

public class SetProgLanguage extends AbstractGameAction {

  private static final long serialVersionUID = 5778501209753480269L;

  private ProgrammingLanguage lang;

  public SetProgLanguage(Game game, ProgrammingLanguage lang)
  {
    super(game, lang.toString(), null);
    this.lang = lang;
  }

  @Override public void actionPerformed(ActionEvent e)
  {
    if (lang.isC()) {
      int res = JOptionPane.showConfirmDialog(null,
                                              i18n.tr("The C langage is currently very experimental in the PLM.\n"
                                                      + "If you go for C, you may not be able to complete some exercises that\n"
                                                      + "are still in progress in C, although some other parts are already okay.\n\n"
                                                      + "Do you want to proceed anyway?"),
                                              i18n.tr("C is still experimental"), JOptionPane.OK_CANCEL_OPTION);
      if (res != JOptionPane.OK_OPTION)
        return;
    }

    try {
      game.setProgramingLanguage(lang);
    } catch (BrokenProgrammingLanguageException ex) {
      JOptionPane.showMessageDialog(null, ex.getMessage(), ex.title, JOptionPane.ERROR_MESSAGE);
    }
  }
}
