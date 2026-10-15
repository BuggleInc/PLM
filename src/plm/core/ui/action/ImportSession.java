package plm.core.ui.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import plm.core.model.Game;
import plm.core.model.session.SessionZip;
import plm.core.model.tracking.GitTracker;

public class ImportSession extends AbstractGameAction {

  private static final long serialVersionUID = 5778501209753480269L;

  private Component parent;

  public ImportSession(Game game, String text, ImageIcon icon, Component parent)
  {
    super(game, text, icon);
    this.parent = parent;
  }

  @Override public void actionPerformed(ActionEvent e)
  {
    JFileChooser fc = new JFileChooser();
    fc.setFileFilter(new FileNameExtensionFilter("PLM session (*.zip)", "zip"));

    if (fc.showOpenDialog(this.parent) == JFileChooser.APPROVE_OPTION) {
      File zip           = fc.getSelectedFile();
      GitTracker tracker = this.game.getGitTracker();
      try {
        boolean adoptIdentity = false;
        if (!SessionZip.userUUID(zip).equals(this.game.getUsers().getCurrentUser().getUserUUIDasString())) {
          adoptIdentity = tracker.isSessionEmpty(); // Nothing to lose locally: take the identity of the zip without asking
          if (!adoptIdentity) {
            Object[] options = {Game.i18n.tr("Keep my identity"), Game.i18n.tr("Take the identity of the file"), Game.i18n.tr("Cancel")};
            int choice       = JOptionPane.showOptionDialog(this.parent,
                                                            Game.i18n.tr("This session file belongs to another user than the current one.\n"
                                                                         + "Both sessions will be merged: which identity should be kept?"),
                                                            Game.i18n.tr("Import a session"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                                                            options, options[0]);
            if (choice != 0 && choice != 1)
              return;
            adoptIdentity = choice == 1;
          }
        }
        tracker.importSession(zip, adoptIdentity);
      } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this.parent, Game.i18n.tr("Cannot import the session: {0}", ex.getLocalizedMessage()), Game.i18n.tr("Import failed"),
                                      JOptionPane.ERROR_MESSAGE);
      }
    }
  }
}
