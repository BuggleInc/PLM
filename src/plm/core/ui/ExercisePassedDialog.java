package plm.core.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import net.miginfocom.swing.MigLayout;

import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import plm.core.model.Game;
import plm.core.model.lesson.ExecutionProgress;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Lecture;

public class ExercisePassedDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	
	public I18n i18n = I18nFactory.getI18n(getClass(),"org.plm.i18n.Messages",getLocale(), I18nFactory.FALLBACK);


	public ExercisePassedDialog(Exercise exo) {
		super(MainFrame.getInstance(), "Exercice passed \\o/", false);
		
		this.setTitle(i18n.tr("Exercice passed \\o/"));
		
		setLayout(new MigLayout("fill","","[fill,grow,push][]"));
		
		JTabbedPane panes = new JTabbedPane();
		add(panes,"span,grow");
		
		/* ------------ The congrats panel ------------ */
		JPanel congratsPane = new JPanel();
		panes.addTab(i18n.tr("Congrats"), congratsPane);
		congratsPane.setLayout(new MigLayout("fill"));
		
		JLabel msg = new JLabel();		
		Vector<Lecture> nextExercises =  exo.getDependingLectures();	
		final JComboBox<Lecture> exoChooser = nextExercises.isEmpty()? null : new JComboBox<Lecture>(nextExercises);
		
		if ( nextExercises.size() == 0) {
			if (exo.lastResult.passedTests > 1) {
				msg.setText(i18n.tr("<html>Congratulations, you passed this exercise.<br>{0} tests passed.</html>",
								exo.lastResult.passedTests) + exo.lastResult.executionError);
			} else { 
				msg.setText(i18n.tr("<html>Congratulations, you passed this exercise.</html>",
								exo.lastResult.passedTests) + exo.lastResult.executionError);
			}

			congratsPane.add(new JLabel( ResourcesCache.getIcon("img/trophy.png") ));
			congratsPane.add(msg, "wrap,grow,aligny 50%");

		} else {
			
			if (exo.lastResult.passedTests > 1) {
				msg.setText(i18n.tr("<html>Congratulations, you passed this exercise.<br>({0} tests passed)<br>Which exercise will you do now?</html>",
								exo.lastResult.passedTests)); 
			} else {
				msg.setText(i18n.tr("<html>Congratulations, you passed this exercise.<br>Which exercise will you do now?</html>"));
			}
			
			congratsPane.add(new JLabel( ResourcesCache.getIcon("img/trophy.png") ), "spany 2");
			congratsPane.add(msg, "wrap,grow,aligny 50%");
			congratsPane.add(exoChooser,"wrap,alignx 50%");
		}
		
		/* ------------ The feedback panel ------------ */
		JPanel feedbackPane = new JPanel();
		panes.addTab(i18n.tr("Give feedback"), feedbackPane);
		feedbackPane.setLayout(new MigLayout("fill","[][grow,fill]20[][grow,fill]","[][][][grow,fill,push]0[]"));
		
		feedbackPane.add(new JLabel(i18n.tr("Please help us to improve this exercise by filling this little evaluation.")), "growx, growy 0,wrap,spanx 4");
		
		JLabel lbDifficulty = new JLabel(i18n.tr("Difficulty:"));
		feedbackPane.add(lbDifficulty,"align right");
		/* Difficulties are the text presented to the user while difficultiesEN are the one that will be added to the report */
		String[] difficulties = new String[] {i18n.tr("(please choose)"),i18n.tr("Too easy"),i18n.tr("Easy"),i18n.tr("Just right"),i18n.tr("Difficult"),i18n.tr("Too difficult")};
		final String[] difficultiesEN = new String[] {"Too easy","Easy","Just right","Difficult","Too difficult"};
		final JComboBox<String> difficultiesChooser = new JComboBox<String>(difficulties);
		feedbackPane.add(difficultiesChooser,"grow");

		JLabel lbInterest = new JLabel(i18n.tr("Interest:"));
		feedbackPane.add(lbInterest,"");
		String[] interests = new String[] {i18n.tr("(please choose)"),i18n.tr("Really good"),i18n.tr("Amusing"),i18n.tr("Just okay"),i18n.tr("Boring"),i18n.tr("Really bad"),};
		final String[] interestsEN = new String[] {"(please choose)","Really good","Amusing","Just okay","Boring","Really bad"};
		final JComboBox<String> interestChooser = new JComboBox<String>(interests);
		feedbackPane.add(interestChooser,"grow,wrap");
		
		feedbackPane.add(new JLabel(i18n.tr("Free comments on that exercise:")),"grow 0,spanx 4,wrap");
		final JEditorPane comment = new JEditorPane();
		JScrollPane spComment = new JScrollPane(comment);
 		feedbackPane.add(spComment,"push,grow 20000 20000,wrap,span");
		
		
		/* ------------ The close button ------------ */
 		final ExecutionProgress result = exo.lastResult;
		JButton close = new JButton(i18n.tr("Close"));
		close.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (exoChooser != null) {
					Game.getInstance().setCurrentExercise((Lecture) exoChooser.getSelectedItem());
				}
				
				String chosenDifficulty = null;
				for (String d: difficultiesEN) 
					if (difficultiesChooser.getSelectedItem().equals(i18n.tr(d)))
						chosenDifficulty = d;
				String chosenInterest = null;
				for (String d: interestsEN) 
					if (interestChooser.getSelectedItem().equals(i18n.tr(d)))
						chosenInterest = d;
				
				result.feedbackDifficulty = chosenDifficulty;
				result.feedbackInterest = chosenInterest;
				result.feedback = comment.getText();
				if (result.feedback.equals(""))
					result.feedback = null;
				dispose();
			}
		});
		add(close,"span, alignx 50%");
		
		/* ------------ wrap up the dialog ------------ */
		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		pack();
		close.requestFocusInWindow();
		setMinimumSize(getSize());
		setVisible(true);
	}
}
