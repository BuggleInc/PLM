package plm.core.ui.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.eclipse.jgit.api.errors.GitAPIException;
import plm.core.model.Game;
import plm.core.model.UserAbortException;

public class ExportSession extends AbstractGameAction {

  private static final long serialVersionUID = 5778501209753480269L;
  private Component parent;

  public ExportSession(Game game, String text, ImageIcon icon, Component parent)
  {
    super(game, text, icon);
    this.parent = parent;
  }

  @Override public void actionPerformed(ActionEvent e)
  {
    JFileChooser fc = new JFileChooser();
    fc.setFileFilter(new FileNameExtensionFilter("PLM session (*.zip)", "zip"));
    fc.setSelectedFile(new File("plm-session.zip"));

    if (fc.showSaveDialog(this.parent) == JFileChooser.APPROVE_OPTION) {
      try {
        this.game.saveSession();
        this.game.getGitTracker().exportSession(fc.getSelectedFile());
      } catch (UserAbortException | IOException | GitAPIException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this.parent, Game.i18n.tr("Cannot export the session: {0}", ex.getLocalizedMessage()), Game.i18n.tr("Export failed"),
                                      JOptionPane.ERROR_MESSAGE);
      }
    }
  }
}
