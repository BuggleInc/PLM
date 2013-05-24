package jlm.core.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import jlm.core.model.lesson.ExecutionProgress;
import net.miginfocom.swing.MigLayout;

public class ExerciseFailedDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	
	public I18n i18n = I18nFactory.getI18n(getClass(),"org.jlm.i18n.Messages",getLocale(), I18nFactory.FALLBACK);


	public ExerciseFailedDialog(ExecutionProgress ep) {
		super(MainFrame.getInstance(), "Exercise failed /o\\", true);
		
		this.setTitle(i18n.tr("Exercise failed /o\\"));
		
		setLayout(new MigLayout("fill",""));
		add(new JLabel( (Icon) UIManager.getLookAndFeelDefaults().get("OptionPane.errorIcon") ));
		
		JLabel msg;
		JTextArea ta = new JTextArea();
		ta.setEditable(false);
		if (ep.compilationError == null) { 
			msg = new JLabel(i18n.tr("You didn't manage to reach your objective." ));
			ta.setText(ep.details);
		} else {
			msg = new JLabel( i18n.tr("Compilation error") );
			ta.setText(ep.compilationError);
		}
		ta.setCaretPosition(0);
		add(msg,"wrap");
		add(new JScrollPane(ta),"spanx, grow, growprio 200, wrap");
		
		JButton close = new JButton(i18n.tr("Close"));
		close.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		add(close,"span, alignx 50%");
		
		
		pack();
		setVisible(true);
	}
}
