package universe.turtles.ui;

import javax.swing.JLabel;
import javax.swing.JTextArea;

import net.miginfocom.swing.MigLayout;
import universe.EntityControlPanel;

public class TurtleButtonPanel extends EntityControlPanel {

	private static final long serialVersionUID = 1L;
	JLabel lForward;
	JTextArea taForward;
	
	public TurtleButtonPanel() {
		setLayout(new MigLayout());
		lForward = new JLabel("Forward");
		taForward = new JTextArea();
		add(lForward);
		add(taForward,"wrap");	
	}

	@Override
	public void setEnabledControl(boolean enabled) {
		lForward.setEnabled(enabled);
		taForward.setEnabled(enabled);
	}
}
