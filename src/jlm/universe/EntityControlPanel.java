package jlm.universe;

import javax.swing.JPanel;

public abstract class EntityControlPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	public abstract void setEnabledControl(boolean enabled);

}
