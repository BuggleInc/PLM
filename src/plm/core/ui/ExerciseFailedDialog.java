package plm.core.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import net.miginfocom.swing.MigLayout;
import plm.core.model.Game;
import plm.core.model.lesson.RunOutcome;

public class ExerciseFailedDialog extends JDialog {
  private static final long serialVersionUID = 1L;

  public ExerciseFailedDialog(RunOutcome ep)
  {
    super(MainFrame.getInstance(), "Mmm, not quite", false);

    String[] fortune = new String[] {Game.i18n.tr("<i>Practice makes perfect</i>, as they say."),
                                     Game.i18n.tr("<i>To err is human</i>, as they say."),
                                     Game.i18n.tr("You should keep trying until you get it right!"),
                                     Game.i18n.tr("We all learn from our mistakes, so, good job!"),
                                     Game.i18n.tr("We learn from failure, not from success!"),
                                     Game.i18n.tr("Success comes after failure"),
                                     Game.i18n.tr("<i>The master has failed more times than the beginner has even tried</i>, as they say."),
                                     Game.i18n.tr("<i>Anyone who has never made a mistake has never tried anything new.</i> (Einstein)."),
                                     Game.i18n.tr("<i>Success does not consist in never making mistakes, but in never making the same one a "
                                                  + "second time.</i> (Shaw)")};

    this.setTitle(Game.i18n.tr("Mmm, not quite"));

    setLayout(new MigLayout("fill", "[][grow, fill]")); // the icon in a fixed column, the texts taking the space
    add(new JLabel((Icon)UIManager.getLookAndFeelDefaults().get("OptionPane.errorIcon")));

    JButton close = new JButton(Game.i18n.tr("Close"));
    close.addActionListener(new ActionListener() {
      @Override public void actionPerformed(ActionEvent e)
      {
        dispose();
      }
    });

    JEditorPane msg = PlmHtmlEditorKit.pane(); // wraps its text at the width of the dialog
    msg.setOpaque(false);
    msg.setBorder(null);
    JTextArea ta = new JTextArea();
    ta.setEditable(false);
    if (ep.compilationError == null) {
      msg.setText(Game.i18n.tr("<html>You didn''t manage to reach your objective this time. That''s fine. {0}"
                                   + "<p>You should graphically compare the final state of your world with the objective to understand the problem."
                                   + "<p>If you don''t see the error, check the details below for a textual description of this difference "
                                   + "between both worlds.</html>",
                               fortune[(int)(Math.random() * fortune.length)]));
      ta.setText(ep.executionError);
      ta.setCaretPosition(0);
    } else {
      msg.setText(Game.i18n.tr("<html>Compilation error.<p>You can find below the detailed error message (as given by {0}). Please read it "
                                   + "carefully to understand the problem, and fix your code.</html>",
                               Game.getInstance().getProgrammingLanguage().getLang()));
      ta.setText(ep.compilationError);
      ta.setCaretPosition(0);
    }
    msg.setFocusable(false);
    ta.setFocusable(true);
    add(msg, "w 550:550:, hmin 0, wrap"); // MigLayout computes the height of a wrapping text at its minimum width
    final JScrollPane sp = new JScrollPane(ta);
    sp.setFocusable(false);
    if (ep.compilationError == null) {
      sp.setPreferredSize(new Dimension(0, 250));
      JPanel buttons          = new JPanel();
      final JButton detailsBt = new JButton(Game.i18n.tr("Details >>"));
      detailsBt.addActionListener(new ActionListener() {
        boolean shown = false;

        @Override public void actionPerformed(ActionEvent e)
        {
          if (shown) {
            detailsBt.setText(Game.i18n.tr("Details >>"));
            remove(sp);
          } else {
            detailsBt.setText(Game.i18n.tr("Details <<"));
            add(sp, "spanx, grow 20000 20000,push, growprio 200, wrap");
          }
          shown = !shown;
          ExerciseFailedDialog.this.pack();
        }
      });
      buttons.add(detailsBt);
      buttons.add(close);
      add(buttons, "span, alignx 50%");
    } else {
      add(sp, "spanx, grow, growprio 200, wrap");
      add(close, "span, alignx 50%");
    }
    getRootPane().registerKeyboardAction(e -> dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
    close.requestFocusInWindow();
    setModal(true);
    setModalityType(ModalityType.APPLICATION_MODAL);
    pack();
    setMinimumSize(getSize());
    setVisible(true);
  }
}
