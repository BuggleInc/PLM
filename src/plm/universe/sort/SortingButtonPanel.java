package plm.universe.sort;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import plm.core.model.Game;
import plm.universe.EntityControlPanel;

/**
 * The control panel for the sorting world. 
 * It allows you to use the copy, setValue and swap methods interactively.
 * @see EntityControlPanel
 * @see SortingWorld
 */
public class SortingButtonPanel extends EntityControlPanel {

	private static final long serialVersionUID = 1L;
	private JButton validateButton;	// a validate button
	private JComboBox operationsComboBox = new JComboBox();	// the available operations on the array ( setValue, copy and swap )
	private JComboBox leftValueComboBox;	// the value for the first parameter of the selected operation
	private JComboBox rightValueComboBox;	// the value for the second parameter of the selected operation
	
	/**
     * Constructor of SortingButtonPanel
     * It initializes the command panel
     */
	public SortingButtonPanel() {
		super();
		SortingEntity se = (SortingEntity) Game.getInstance().getSelectedEntity();
		Game.getInstance().addHumanLangListener(this);
		this.add(this.createCommandPanel(se));
	}
	
	/**
	 * Initialize the command panel of the SortingButtonPanel
	 * @param se The current sorting Entity
	 * @return a JPanel containing the command panel
	 */
	private JPanel createCommandPanel(SortingEntity se) {
		JPanel commandPane = new JPanel();
		commandPane.setLayout(new FlowLayout());

		currentHumanLanguageHasChanged(null);
		
		int n = se.getValueCount();
		Integer index[] = new Integer[n];
		for ( int i = 0 ; i < n ; i++) {
			index[i] = i; 
		}
		this.leftValueComboBox = new JComboBox(index) ;
		this.rightValueComboBox = new JComboBox(index) ;
		
		this.validateButton = new JButton(i18n.tr("go"));
		this.validateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int i = leftValueComboBox.getSelectedIndex();
				int j = rightValueComboBox.getSelectedIndex();
				int selectedOperation = operationsComboBox.getSelectedIndex();
				SortingEntity se = (SortingEntity) Game.getInstance().getSelectedEntity();
				switch (selectedOperation)
				{
					case 0:
						echo(i18n.tr("swap({0},{1})",i,j));
						se.swap(i,j);
						break;
					case 1:
						echo(i18n.tr("setValue({0},{1})",i,j));
						se.setValue(i,j);
						break;
					case 2:
						echo(i18n.tr("copy({0},{1})",i,j));
						se.copy(i, j);
						break;
					default:
						System.err.println("Unknown operation index: "+selectedOperation);
				}
			}
		}
		);
		
		commandPane.add(this.operationsComboBox);
		commandPane.add(this.leftValueComboBox);
		commandPane.add(this.rightValueComboBox);
		commandPane.add(this.validateButton);
		
		return commandPane;
	}

	@Override
	public void setEnabledControl(boolean enabled) {
		this.validateButton.setEnabled(enabled);
		this.operationsComboBox.setEnabled(enabled);
		this.leftValueComboBox.setEnabled(enabled);
		this.rightValueComboBox.setEnabled(enabled);
	}
	
	@Override
	public void currentHumanLanguageHasChanged(Locale newLang) {
		super.currentHumanLanguageHasChanged(newLang);
		operationsComboBox.removeAllItems();
		for (String s : new String[] { i18n.tr("swap"),i18n.tr("setValue"),i18n.tr("copy")}) 
			operationsComboBox.addItem(s);
		
	}
}
