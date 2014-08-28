package plm.core.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import net.miginfocom.swing.MigLayout;

import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import plm.core.model.Game;
import plm.core.model.lesson.ExecutionProgress;

public class ExerciseFailedDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	public I18n i18n = I18nFactory.getI18n(getClass(),"org.plm.i18n.Messages",getLocale(), I18nFactory.FALLBACK);


	public ExerciseFailedDialog(ExecutionProgress ep) {
		super(MainFrame.getInstance(), "Mmm, not quite", false);
		
		String[] fortune = new String[] {
				i18n.tr("<i>Practice makes perfect</i>, as they say."),
				i18n.tr("<i>To err is human</i>, as they say."),
				i18n.tr("You should keep trying until you get it right!"),
				i18n.tr("We all learn from our mistakes, so, good job!"),
				i18n.tr("We learn from failure, not from success!"),
				i18n.tr("<i>Anyone who has never made a mistake has never <br>tried anything new.</i> (Einstein)."),
				i18n.tr("<i>Success does not consist in never making mistakes,<br> but in never making the same one a second time.</i> (Shaw)")
		};
		
		this.setTitle(i18n.tr("Mmm, not quite"));
		
		setLayout(new MigLayout("fill",""));
		add(new JLabel( (Icon) UIManager.getLookAndFeelDefaults().get("OptionPane.errorIcon") ));

		JButton close = new JButton(i18n.tr("Close"));
		close.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		JLabel msg;
		JTextArea ta = new JTextArea();
		ta.setEditable(false);
		if (ep.compilationError == null) { 
			msg = new JLabel(i18n.tr("<html>You didn''t manage to reach your objective this time. <br>\n"
					+ "That''s fine. {0}<br>\n"
					+ "<br>\n"
					+ "You should graphically compare the final state of your world <br>\n"
					+ "with the objective to understand the problem. <br>\n"
					+ "<br>\n"
					+ "If you don''t see the error, check the details below for a <br>\n"
					+ "textual description of this difference between both worlds.</html>",
					fortune[(int)  (Math.random()*fortune.length)]));
			ta.setText(ep.details);
			ta.setCaretPosition(0);
		} else {
			msg = new JLabel( i18n.tr("<html>Compilation error.<br>\n"
					+ "You can find below the detailed error message (as given by {0}).<br>\n"
					+ "Please read it carefully to understand the problem, and fix your code.</html>",
						Game.getProgrammingLanguage().getLang()) );
			ta.setText(ep.compilationError);
			ta.setCaretPosition(0);
		}
		msg.setFocusable(false);
		ta.setFocusable(false);
		add(msg,"wrap");
		final JScrollPane sp = new JScrollPane(ta);
		sp.setFocusable(false);
		if (ep.compilationError == null) {
			sp.setPreferredSize(new Dimension(0, 250));
			JPanel buttons = new JPanel();
			final JButton detailsBt = new JButton(i18n.tr("Details >>"));
			detailsBt.addActionListener(new ActionListener() {
				boolean shown = false;
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if (shown) {
						detailsBt.setText(i18n.tr("Details >>"));
						remove(sp);
					} else {
						detailsBt.setText(i18n.tr("Details <<"));
						add(sp,"spanx, grow, growprio 200, wrap");
					}
					shown = !shown;
					ExerciseFailedDialog.this.pack();
				}
			});
			buttons.add(detailsBt);
			buttons.add(close);
			add(buttons,"span, alignx 50%");
		} else {
			add(sp,"spanx, grow, growprio 200, wrap");			
			add(close,"span, alignx 50%");
		}
		setMinimumSize(getSize());
		close.requestFocusInWindow();
		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		pack();
		setVisible(true);
	}
}
