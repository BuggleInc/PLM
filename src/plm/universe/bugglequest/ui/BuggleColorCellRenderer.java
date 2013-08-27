package plm.universe.bugglequest.ui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

/**
 * This cell renderer is used by the combo boxes from BuggleButtonPanel in order to show some fancy colored rectangles.
 * @see ListCellRenderer
 * @see BuggleButtonPanel
 */
public class BuggleColorCellRenderer extends JPanel implements ListCellRenderer{

	private static final long serialVersionUID = 1L;

    private JButton cell;	// The container of the colored rectangle
    
    /**
     * Constructor of BuggleColorCellRenderer
     * It initializes the cell and add it to the BuggleColorCellRenderer
     */
    public BuggleColorCellRenderer() {
        super();
        this.cell = new JButton();
        this.add(this.cell);
    }
    
    /**
     * Change the color of the cell according to the selected value
     * @return The BuggleColorCellRenderer with the cell's background of color "value" 
     * @param list Unused here
     * @param value The color that we will assign to our cell. Since it's sure that value is an instance of Color, it's not necessary to check it.
     * @param index Unused here
     * @param isSelected Unused here
     * @param cellHasFocus Unused here
     */
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        this.cell.setBackground((Color) value);
        this.cell.setOpaque(true);
        this.cell.setBorderPainted(false);
        return this;
    }

}