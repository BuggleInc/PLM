package plm.universe.sort;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

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
	private JComboBox operationsComboBox;	// the available operations on the array ( setValue, copy and swap )
	private JComboBox leftValueComboBox;	// the value for the first parameter of the selected operation
	private JComboBox rightValueComboBox;	// the value for the second parameter of the selected operation
	
	private I18n i18n = I18nFactory.getI18n(getClass(),"org.jlm.i18n.Messages",getLocale(), I18nFactory.FALLBACK);

	/**
     * Constructor of SortingButtonPanel
     * It initializes the command panel
     */
	public SortingButtonPanel() {
		super();
		SortingEntity se = (SortingEntity) Game.getInstance().getSelectedEntity();
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
		
		String operationsAvailable[] = { "swap","setValue","copy"};
		this.operationsComboBox = new JComboBox(operationsAvailable);
		
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
						se.swap(i,j);
						break;
					case 1:
						se.setValue(i,j);
						break;
					case 2:
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
}
