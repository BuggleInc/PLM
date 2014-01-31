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

import plm.core.model.Game;
import plm.universe.EntityControlPanel;

/**
 * The control panel for the baseball world. 
 * It allows to move the players interactively
 */
public class BaseballMovePanel extends EntityControlPanel {

	private static final long serialVersionUID = 1L;
	private JButton validateButton;	// a validate button
	private JComboBox<Integer> baseSelector;	
	private JComboBox<Integer> playerSelector;	
	private BaseballWorld field;

	public BaseballMovePanel() {
		super();
		field = (BaseballWorld) Game.getInstance().getSelectedEntity().getWorld() ;
		setLayout(new GridLayout(2,1));
		add(createMovePanel());
	}
	
	/** Get the selection from elsewhere (eg, a click on the view */
	public void setPlayer(int base, int pos) {
		baseSelector.setSelectedIndex(base);
		playerSelector.setSelectedIndex(pos);
	}
	/** Do the action as commanded from elsewhere (eg, a double click on the view) */
	public void doMove() {
		validateButton.doClick();
	}

	/** Creates the command panel of the BaseballMovePanel */
	private Component createMovePanel() {
		JPanel movePanel = new JPanel();

		movePanel.add(new JLabel("move"));

		/* Create the base selector */
		Integer[] values = new Integer[field.getBasesAmount()];
		for (int i = 0 ; i < field.getBasesAmount() ; i++)
			values[i] = i;		
		baseSelector = new JComboBox<Integer>(values);

		/* Create the position selector */
		values = new Integer[field.getPositionsAmount()];
		for (int i = 0 ; i < field.getPositionsAmount() ; i++)
			values[i] = i;
		playerSelector = new JComboBox<Integer>(values);

		/* Create the button */
		validateButton = new JButton(i18n.tr("Go"));
		validateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				BaseballEntity be = (BaseballEntity) Game.getInstance().getSelectedEntity();
				int base = baseSelector.getSelectedIndex();
				int player = playerSelector.getSelectedIndex();
				try  {
					echo(i18n.tr("move({0},{1})",base,player));
					be.move(base, player);
				} catch (IllegalArgumentException e) {
					JOptionPane.showMessageDialog(null, i18n.tr("The player {0} of the base {1} cannot reach the hole that is too far from its position", player, base),
							i18n.tr("Invalid move"),JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		/* Do the layout and return the result */
		movePanel.add(baseSelector);
		movePanel.add(playerSelector);
		movePanel.add(validateButton);

		return movePanel;
	}

	/**
	 * Enables or disables each component of the control panel
	 * @param enabled whether the components shall be enabled or not
	 */
	@Override
	public void setEnabledControl(boolean enabled) {
		validateButton.setEnabled(enabled);
		baseSelector.setEnabled(enabled);
		playerSelector.setEnabled(enabled);
	}
}
