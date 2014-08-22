package lessons.sort.pancake.universe;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import plm.core.model.Game;
import plm.universe.EntityControlPanel;

/**
 * The control panel for the burned pancake world. 
 * It allows you to use to flip the pancake
 */
public class PancakeFlipButtonPanel extends EntityControlPanel {

	private static final long serialVersionUID = 1L;
	private JButton validateButton;	// a validate button
	private JComboBox pancakesAmountComboBox;	// a combobox which let you choose how many pancake you want to flip
	private boolean burnedPancake;
	/**
     * Constructor of PancakeFlipButtonPanel
     * It initializes the command panel
     */
	public PancakeFlipButtonPanel() {
		super();
		PancakeEntity pe = (PancakeEntity) Game.getInstance().getSelectedEntity();
		this.add(this.createFlipPanel(pe));
	}

	/**
	 * Create the command panel of the PancakeFlipButtonPanel
	 * @param pe The current PancakeEntity
	 * @return a JPanel containing the command panel
	 */
	private Component createFlipPanel(PancakeEntity pe) {
		JPanel flipPanel = new JPanel();
		
		flipPanel.setLayout(new FlowLayout());
		flipPanel.add(new JLabel("flip"));
		
		this.initPancakesAmountComboBox(pe);
		flipPanel.add(this.pancakesAmountComboBox);
		
		this.initValidateButton();
		flipPanel.add(this.validateButton);
		
		return flipPanel;
	}

	/**
	 * Initialize the validate button
	 */
	private void initValidateButton() {
		this.validateButton = new JButton("Validate");
		this.validateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int amount = (Integer) pancakesAmountComboBox.getSelectedItem();
				PancakeEntity pe = (PancakeEntity) Game.getInstance().getSelectedEntity();
				echo(i18n.tr("flip({0})",amount));
				pe.flip(amount);
			}
		});
	}

	/**
	 * Initialize the combo box
	 * @param pe the current selected PancakeEntity 
	 * @param burnedPancake 
	 */
	private void initPancakesAmountComboBox(PancakeEntity pe) {
		int n = pe.getStackSize();
		Integer values[];
		burnedPancake = ((PancakeWorld) Game.getInstance().getSelectedWorld()).isBurnedPancake(); 
		if ( burnedPancake) {
			values = new Integer[n];
			for ( int i = 0 ; i < n ; i++) 
				values[i] = i+1;
			
		} else {
			values = new Integer[n-1];
			for ( int i = 0 ; i < n-1 ; i++) 
				values[i] = i+2;
		}
		this.pancakesAmountComboBox = new JComboBox(values) ;
	}
	
	/**
	 * Allow to enable or disable each component of the control panel
	 */
	@Override
	public void setEnabledControl(boolean enabled) {
		this.validateButton.setEnabled(enabled);
		this.pancakesAmountComboBox.setEnabled(enabled);
	}

	boolean canClick = true;
	public void setSelectedPancake(int amount) {
		if (amount < 1 || amount > pancakesAmountComboBox.getItemCount()+1 || (amount==1&&!burnedPancake) )
			canClick = false;
		else {
			canClick = true;
			pancakesAmountComboBox.setSelectedIndex(amount+ (burnedPancake?-1:-2) );
		}
	}
	public void doMove() {
		if (canClick)
			validateButton.doClick();
	}
}
