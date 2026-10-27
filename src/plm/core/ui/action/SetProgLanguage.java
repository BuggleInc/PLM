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
    try {
      game.setProgrammingLanguage(lang);
    } catch (BrokenProgrammingLanguageException ex) {
      JOptionPane.showMessageDialog(null, ex.getMessage(), ex.title, JOptionPane.ERROR_MESSAGE);
    }
  }
}
