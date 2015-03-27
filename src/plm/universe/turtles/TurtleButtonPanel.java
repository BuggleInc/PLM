package plm.universe.turtles;

import javax.swing.JLabel;
import javax.swing.JTextArea;

import net.miginfocom.swing.MigLayout;
import plm.core.model.Game;
import plm.universe.EntityControlPanel;

public class TurtleButtonPanel extends EntityControlPanel {

	private static final long serialVersionUID = 1L;
	JLabel lForward;
	JTextArea taForward;
	
	public TurtleButtonPanel(Game game) {
		super(game);
		setLayout(new MigLayout());
		//lForward = new JLabel("Forward");
		//taForward = new JTextArea();
		//add(lForward);
		//add(taForward,"wrap");	
	}

	@Override
	public void setEnabledControl(boolean enabled) {
		//lForward.setEnabled(enabled);
		//taForward.setEnabled(enabled);
	}
}
