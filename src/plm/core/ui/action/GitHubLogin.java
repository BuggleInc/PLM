package plm.core.ui.action;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URI;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import plm.core.model.Game;
import plm.core.model.tracking.GitHubAuth;

/** Logs the user in GitHub with the device flow, or out if she already is. */
public class GitHubLogin extends AbstractGameAction {

  private static final long serialVersionUID = 4106329927741307552L;
  private Component parent;

  public GitHubLogin(Game game, String text, ImageIcon icon, Component parent)
  {
    super(game, text, icon);
    this.parent = parent;
  }

  @Override public void actionPerformed(ActionEvent e)
  {
    String login = GitHubAuth.getLogin();
    if (login != null) {
      Object[] options = {Game.i18n.tr("OK"), Game.i18n.tr("Logout")};
      if (JOptionPane.showOptionDialog(parent, Game.i18n.tr("You are connected to GitHub as {0}.\nYour feedback can be sent under this identity.", login),
                                       Game.i18n.tr("GitHub account"), JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options,
                                       options[0]) == 1)
        GitHubAuth.logout();
      return;
    }

    GitHubAuth.DeviceCode code;
    try {
      code = GitHubAuth.requestCode();
    } catch (Exception ex) {
      showError(ex);
      return;
    }
    boolean opened = true;
    try {
      Desktop.getDesktop().browse(URI.create(code.verificationUri()));
    } catch (Exception ex) {
      opened = false;
    }

    SwingWorker<String, Void> worker = new SwingWorker<>() {
      @Override protected String doInBackground() throws Exception
      {
        return GitHubAuth.completeLogin(code);
      }
    };
    JTextField uri      = new JTextField(code.verificationUri());
    JTextField userCode = new JTextField(code.userCode());
    uri.setEditable(false);
    userCode.setEditable(false);
    Object[] message = {opened ? Game.i18n.tr("Your web browser was asked to open this GitHub page:")
                               : Game.i18n.tr("Please open this GitHub page in your web browser:"),
                        uri, Game.i18n.tr("Then enter this code on that page:"), userCode, Game.i18n.tr("This window closes once you authorized the PLM.")};
    JDialog dialog   = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[] {Game.i18n.tr("Cancel")})
                           .createDialog(parent, Game.i18n.tr("GitHub account"));
    dialog.setModal(false);
    dialog.addWindowListener(new WindowAdapter() {
      @Override public void windowClosed(WindowEvent we)
      {
        worker.cancel(true);
      }
    });
    worker.addPropertyChangeListener(ev -> {
      if (worker.isDone()) {
        dialog.dispose();
        try {
          JOptionPane.showMessageDialog(parent, Game.i18n.tr("Logged in GitHub as {0}.", worker.get()), Game.i18n.tr("GitHub account"),
                                        JOptionPane.INFORMATION_MESSAGE);
        } catch (CancellationException ex) {
          // The user cancelled
        } catch (ExecutionException | InterruptedException ex) {
          showError(ex.getCause() == null ? ex : ex.getCause());
        }
      }
    });
    worker.execute();
    dialog.setVisible(true);
  }

  private void showError(Throwable ex)
  {
    JOptionPane.showMessageDialog(parent, Game.i18n.tr("Cannot log in GitHub: {0}", ex.getLocalizedMessage()), Game.i18n.tr("GitHub login failed"),
                                  JOptionPane.ERROR_MESSAGE);
  }
}
