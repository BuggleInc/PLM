package jlm.universe.sort;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JToggleButton;

import jlm.core.model.Game;
import jlm.universe.EntityControlPanel;
import jlm.universe.bugglequest.AbstractBuggle;
import jlm.universe.bugglequest.exception.BuggleWallException;

public class SortButtonPanel extends EntityControlPanel {

	private static final long serialVersionUID = 1L;
	private JButton fButton;
	private JButton bButton;
	private JButton rButton;
	private JButton lButton;
	private JToggleButton brushButton;
	
	public SortButtonPanel() {
		//setFloatable(false);

		fButton = new JButton("forward");
		fButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					((AbstractBuggle)Game.getInstance().getSelectedEntity()).forward();
				} catch (BuggleWallException e) {
					 e.printStackTrace();
					//game.getOutputWriter().log(e);
				}
			}
		});
		fButton.setMnemonic(KeyEvent.VK_UP);

		bButton = new JButton("backward");
		bButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					((AbstractBuggle)Game.getInstance().getSelectedEntity()).backward();
				} catch (BuggleWallException e) {
					// e.printStackTrace();
					Game.getInstance().getOutputWriter().log(e);
				}
			}
		});
		bButton.setMnemonic(KeyEvent.VK_DOWN);

		lButton = new JButton("turn left");
		lButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				((AbstractBuggle)Game.getInstance().getSelectedEntity()).turnLeft();
			}
		});
		lButton.setMnemonic(KeyEvent.VK_LEFT);

		rButton = new JButton("turn right");
		rButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				((AbstractBuggle)Game.getInstance().getSelectedEntity()).turnRight();
			}
		});
		rButton.setMnemonic(KeyEvent.VK_RIGHT);

		brushButton = new JToggleButton("mark");
		brushButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				AbstractBuggle b = (AbstractBuggle)Game.getInstance().getSelectedEntity();
				if (b.isBrushDown()) {
					b.brushUp();
				} else {
					b.brushDown();
				}
			}
		});
		brushButton.setMnemonic(KeyEvent.VK_SPACE);
		brushButton.setSelected(((AbstractBuggle)(Game.getInstance().getSelectedEntity())).isBrushDown());
		
		
		GridBagLayout gdLayout = new GridBagLayout();
		setLayout(gdLayout);

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(3, 3, 3, 3);

		c.gridy = 1;
		c.gridx = 1;
		c.gridwidth = 1;
		gdLayout.setConstraints(fButton, c);
		add(fButton);

		c.gridy = 2;
		c.gridx = 0;
		gdLayout.setConstraints(lButton, c);
		add(lButton);

		c.gridy = 2;
		c.gridx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		gdLayout.setConstraints(brushButton, c);
		add(brushButton);

		c.gridy = 2;
		c.gridx = 2;
		gdLayout.setConstraints(rButton, c);
		add(rButton);

		c.gridy = 3;
		c.gridx = 1;
		gdLayout.setConstraints(bButton, c);
		add(bButton);
	}
	

	@Override
	public void setEnabledControl(boolean enabled) {
		fButton.setEnabled(enabled);
		bButton.setEnabled(enabled);
		lButton.setEnabled(enabled);
		rButton.setEnabled(enabled);
		brushButton.setEnabled(enabled);
	}
}
