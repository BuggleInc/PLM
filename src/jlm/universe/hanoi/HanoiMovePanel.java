package jlm.universe.hanoi;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import jlm.core.model.Game;
import jlm.universe.EntityControlPanel;

/**
 * The control panel for the burned pancake world. 
 * It allows you to use to flip the pancake
 * @see EntityControlPanel
 * @see HanoiWorld
 */
public class HanoiMovePanel extends EntityControlPanel {

	private static final long serialVersionUID = 1L;
	private JComboBox sourceSelector;
	private JComboBox destinationSelector;
	private JButton validateButton;

	/**
	 * Constructor of HanoiMovePanel
	 * It initializes the command panel
	 */
	public HanoiMovePanel() {
		super();
		this.add(this.createMovePanel());
	}

	/**
	 * Create the command panel of the HanoiMovePanel
	 * @return a JPanel containing the move panel
	 */
	private JPanel createMovePanel() {
		JPanel movePanel = new JPanel() ;

		movePanel.add(new JLabel("move"));

		this.initSelectors();
		movePanel.add(this.sourceSelector);
		movePanel.add(this.destinationSelector);

		this.initValidateButton();
		movePanel.add(this.validateButton);

		return movePanel;
	}

	/**
	 * Initialize the validate button
	 */
	private void initValidateButton() {
		this.validateButton = new JButton("Validate");
		this.validateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				HanoiEntity he = (HanoiEntity) Game.getInstance().getSelectedEntity();
				int src = sourceSelector.getSelectedIndex();
				int dst = destinationSelector.getSelectedIndex();
				try 
				{
					he.move(src, dst);
				} 
				catch (HanoiInvalidMove him) 
				{
					showInvalidMoveMessageDialog(him);
				}

			}
		});
	}

	/**
	 * Initialize the source and the destination selectors
	 */
	private void initSelectors() {
		Integer[] values = new Integer[3];
		for (int i = 0 ; i < 3 ; i++)
		{
			values[i] = i;
		}
		this.sourceSelector = new JComboBox(values);
		this.destinationSelector = new JComboBox(values);
	}

	/**
	 * Allow to enable or disable each component of the control panel
	 * @param enabled if the composants shall be enabled or not
	 */
	@Override
	public void setEnabledControl(boolean enabled) {
		this.sourceSelector.setEnabled(enabled);
		this.destinationSelector.setEnabled(enabled);
		this.validateButton.setEnabled(enabled);
	}
	
	/**
	 * Show a message dialog explaining that the user try to make an invalid move on the field
	 * @param 
	 */
	protected void showInvalidMoveMessageDialog(HanoiInvalidMove him) {
		String message = him.getLocalizedMessage();;
		String title = "Invalid move" ;
		JOptionPane.showMessageDialog(null, message,title, JOptionPane.ERROR_MESSAGE);
	}

}
