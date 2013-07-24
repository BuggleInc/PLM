package lessons.sort.baseball.universe;

import java.awt.Component;
import java.awt.GridLayout;
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
 * @see BaseballWorld
 */
public class BaseballMovePanel extends EntityControlPanel {

	private static final long serialVersionUID = 1L;
	private JButton validateButton;	// a validate button
	private JComboBox baseSelector;	
	private JComboBox playerSelector;	
	private JLabel holeBaseLocation;
	private JLabel holePlayerLocation;

	/**
	 * Constructor of BaseballMovePanel
	 * It initializes the command panel
	 */
	public BaseballMovePanel() {
		super();
		this.setLayout(new GridLayout(2,1));
		BaseballEntity be = (BaseballEntity) Game.getInstance().getSelectedEntity() ;
		this.add(this.createMovePanel(be));
		this.add(this.createHolePanel(be));
	}

	/**
	 * Create the panel showing the location of the hole in the world
	 * @param be The current BaseballEntity
	 * @return the panel showing the location of the hole in the world
	 */
	private Component createHolePanel(BaseballEntity be) {
		JPanel holePanel = new JPanel();

		this.holeBaseLocation = new JLabel();
		this.holePlayerLocation = new JLabel();

		this.refreshHoleLocation(be);

		holePanel.add(new JLabel("The hole is located in the base"));
		holePanel.add(this.holeBaseLocation);

		holePanel.add(new JLabel("at the position"));
		holePanel.add(this.holePlayerLocation);

		return holePanel;
	}

	/**
	 * Create the command panel of the BaseballMovePanel
	 * @param be The current BaseballEntity
	 * @return a JPanel containing the move panel
	 */
	private Component createMovePanel(BaseballEntity be) {
		JPanel movePanel = new JPanel();

		movePanel.add(new JLabel("move"));

		this.initBaseSelector(be);
		movePanel.add(this.baseSelector);

		this.initPlayerSelector(be);
		movePanel.add(this.playerSelector);

		this.initValidateButton();
		movePanel.add(this.validateButton);

		return movePanel;
	}

	/**
	 * Initialize the combo box containing the list of bases.
	 * @param be The current BaseballEntity
	 */
	private void initBaseSelector(BaseballEntity be) {
		int baseAmount = be.getBasesAmount();
		Integer[] values = new Integer[baseAmount];
		for (int i = 0 ; i < baseAmount ; i++)
		{
			values[i] = i;
		}
		this.baseSelector = new JComboBox(values);
	}

	/**
	 * Initialize the combo box containing the list of locations of player.
	 * @param be The current BaseballEntity
	 */
	private void initPlayerSelector(BaseballEntity be) {
		int playerAmount = be.getPositionsAmount();
		Integer[] values = new Integer[playerAmount];
		for (int i = 0 ; i < playerAmount ; i++)
		{
			values[i] = i;
		}
		this.playerSelector = new JComboBox(values);
	}

	/**
	 * Initialize the validate button
	 */
	private void initValidateButton() {
		this.validateButton = new JButton("Validate");
		this.validateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				BaseballEntity be = (BaseballEntity) Game.getInstance().getSelectedEntity();
				int base = baseSelector.getSelectedIndex();
				int player = playerSelector.getSelectedIndex();
				try 
				{
					be.move(base, player);
					refreshHoleLocation(be);
				}
				catch (IllegalArgumentException e1) 
				{
					showInvalidMoveMessageDialog(base,player);
				}
			}
		});
	}

	/**
	 * Refresh the location of the hole
	 * @param be The current BaseballEntity
	 */
	private void refreshHoleLocation(BaseballEntity be) {
		holeBaseLocation.setText(""+be.getHoleBase());
		holePlayerLocation.setText(""+be.getHolePosition());
	}

	/**
	 * Allow to enable or disable each component of the control panel
	 * @param enabled if the components shall be enabled or not
	 */
	@Override
	public void setEnabledControl(boolean enabled) {
		this.validateButton.setEnabled(enabled);
		this.baseSelector.setEnabled(enabled);
		this.playerSelector.setEnabled(enabled);
	}

	/**
	 * Show a message dialog explaining that the user try to make an invalid move on the field
	 * @param base The base of the player
	 * @param player The number of the player try to move
	 */
	public void showInvalidMoveMessageDialog(int base,int player) {
		String message ;
		String title ;
		message = "The player number "+player+" of the base number "+base+" can't move so far on the field !";
		title = "Invalid move";
		JOptionPane.showMessageDialog(null, message,title, JOptionPane.ERROR_MESSAGE);
	}

}
