package plm.core.ui;

import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;
import java.util.stream.Stream;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import net.miginfocom.swing.MigLayout;
import org.xnap.commons.i18n.I18n;
import plm.core.model.Game;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Lecture;
import plm.core.model.lesson.RunOutcome;

public class ExercisePassedDialog extends JDialog {
  private static final long serialVersionUID = 1L;

  public ExercisePassedDialog(Exercise exo, RunOutcome result)
  {
    super(MainFrame.getInstance(), "Exercice passed \\o/", false);

    this.setTitle(Game.i18n.tr("Exercice passed \\o/"));

    setLayout(new MigLayout("fill", "", "[fill,grow,push][]"));

    JTabbedPane panes = new JTabbedPane();
    add(panes, "span,grow");

    /* ------------ The congrats panel ------------ */
    JPanel congratsPane = new JPanel();
    panes.addTab(Game.i18n.tr("Congrats"), congratsPane);
    congratsPane.setLayout(new MigLayout("fill"));

    JLabel msg                          = new JLabel();
    Vector<Lecture> nextExercises       = exo.getNextLectures();
    final JComboBox<Lecture> exoChooser = nextExercises.isEmpty() ? null : new JComboBox<Lecture>(nextExercises);

    if (nextExercises.size() == 0) {
      if (result.passedTests > 1) {
        msg.setText(Game.i18n.tr("<html>Congratulations, you passed this exercise.<br>{0} tests passed.</html>", result.passedTests) + result.executionError);
      } else {
        msg.setText(Game.i18n.tr("<html>Congratulations, you passed this exercise.</html>", result.passedTests) + result.executionError);
      }

      congratsPane.add(new JLabel(ResourcesCache.getIcon("img/trophy.png")));
      congratsPane.add(msg, "wrap,grow,aligny 50%");

    } else {

      if (result.passedTests > 1) {
        msg.setText(Game.i18n.tr("<html>Congratulations, you passed this exercise.<br>({0} tests passed)<br><br><br>Select your next exercise:</html>",
                                 result.passedTests));
      } else {
        msg.setText(Game.i18n.tr("<html>Congratulations, you passed this exercise.<br><br><br><br>Select your next exercise:</html>"));
      }

      congratsPane.add(new JLabel(ResourcesCache.getIcon("img/trophy.png")), "spany 2");
      congratsPane.add(msg, "wrap,grow,aligny 50%");
      congratsPane.add(exoChooser, "wrap,alignx 50%,aligny 5%");
    }

    /* ------------ The feedback panel ------------ */
    JPanel feedbackPane = new JPanel();
    panes.addTab(Game.i18n.tr("Give feedback"), feedbackPane);
    feedbackPane.setLayout(new MigLayout("fill", "[][grow,fill]20[][grow,fill]", "[][][][grow,fill,push]0[]"));

    feedbackPane.add(new JLabel(Game.i18n.tr("Please help us to improve this exercise by filling this little evaluation.")), "growx, growy 0,wrap,spanx 4");

    JLabel lbDifficulty = new JLabel(Game.i18n.tr("Difficulty:"));
    feedbackPane.add(lbDifficulty, "align right");
    /* The English texts go in the report, and their translations in the choosers */
    final String[] difficulties                 = {I18n.marktr("(please choose)"), I18n.marktr("Too easy"),  I18n.marktr("Easy"),
                                                   I18n.marktr("Just right"),      I18n.marktr("Difficult"), I18n.marktr("Too difficult")};
    final JComboBox<String> difficultiesChooser = translated(difficulties);
    feedbackPane.add(difficultiesChooser, "grow");

    JLabel lbInterest = new JLabel(Game.i18n.tr("Interest:"));
    feedbackPane.add(lbInterest, "");
    final String[] interests                = {I18n.marktr("(please choose)"), I18n.marktr("Really good"), I18n.marktr("Amusing"),
                                               I18n.marktr("Just okay"),       I18n.marktr("Boring"),      I18n.marktr("Really bad")};
    final JComboBox<String> interestChooser = translated(interests);
    feedbackPane.add(interestChooser, "grow,wrap");

    feedbackPane.add(new JLabel(Game.i18n.tr("Free comments on that exercise:")), "grow 0,spanx 4,wrap");
    final JEditorPane comment = new JEditorPane();
    JScrollPane spComment     = new JScrollPane(comment);
    feedbackPane.add(spComment, "push,grow 20000 20000,wrap,span");

    /* ------------ The buttons, which record the feedback ------------ */
    Runnable recordFeedback = () ->
    {
      result.feedbackDifficulty = difficulties[difficultiesChooser.getSelectedIndex()];
      result.feedbackInterest   = interests[interestChooser.getSelectedIndex()];
      result.feedback           = comment.getText();
      if (result.feedback.equals(""))
        result.feedback = null;
      dispose();
    };
    JPanel buttons = new JPanel();
    // Translator: button of the dialog of a passed exercise, switching to the next exercise chosen in its list
    String proceed = Game.i18n.tr("Proceed");
    JButton close  = new JButton(exoChooser == null ? Game.i18n.tr("Close") : proceed);
    close.addActionListener(e -> {
      if (exoChooser != null)
        Game.getInstance().setCurrentExercise((Lecture)exoChooser.getSelectedItem());
      recordFeedback.run();
    });
    buttons.add(close);
    if (exoChooser != null) { // the student may want to improve their solution, or to try another language
      // Translator: button of the dialog of a passed exercise, closing it without switching to the next exercise chosen in its list
      JButton stay = new JButton(Game.i18n.tr("Stay on this exercise"));
      stay.addActionListener(e -> recordFeedback.run());
      buttons.add(stay);
    }
    add(buttons, "span, alignx 50%");
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); // the close button of the window stays on this exercise, and keeps the feedback
    addWindowListener(new WindowAdapter() {
      @Override public void windowClosing(WindowEvent e)
      {
        recordFeedback.run();
      }
    });
    getRootPane().registerKeyboardAction(e -> dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);

    /* ------------ wrap up the dialog ------------ */
    setModal(true);
    setModalityType(ModalityType.APPLICATION_MODAL);
    pack();
    close.requestFocusInWindow();
    setMinimumSize(getSize());
    setVisible(true);
  }

  /** A chooser of these English texts, shown translated */
  private static JComboBox<String> translated(String[] texts) { return new JComboBox<>(new Vector<>(Stream.of(texts).map(Game.i18n::tr).toList())); }
}
