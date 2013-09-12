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
		super(MainFrame.getInstance(), "Mmm, not quite /o\\", false);
		
		this.setTitle(i18n.tr("Mmm, not quite /o\\"));
		
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
			msg = new JLabel(i18n.tr("<html>You didn't manage to reach your objective. <br>\n"
					+ "You should graphically compare the final state of your world <br>\n"
					+ "with the objective to understand the problem. <br>\n"
					+ "If you don't see the error, check the details below to for a <br>\n"
					+ "textual description of this difference between both worlds.</html>" ));
			ta.setText(ep.details);
			ta.setCaretPosition(0);
		} else {
			msg = new JLabel( i18n.tr("<html>Compilation error.<br>\n"
					+ "You can find below the detailed error message (as given by {0}).<br>\n"
					+ "Please read it carfully to understand the problem, and fix your code.</html>",
						Game.getProgrammingLanguage().getLang()) );
			ta.setText(ep.compilationError);
			ta.setCaretPosition(0);
		}
		add(msg,"wrap");
		final JScrollPane sp = new JScrollPane(ta);
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
		pack();
		setMinimumSize(getSize());
		close.requestFocusInWindow();
		setVisible(true);
	}
}
