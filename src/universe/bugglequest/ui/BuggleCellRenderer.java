package universe.bugglequest.ui;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import universe.bugglequest.AbstractBuggle;



public class BuggleCellRenderer extends JLabel implements ListCellRenderer {

	private static final long serialVersionUID = 5274134398293975047L;

	public BuggleCellRenderer() {
		//setOpaque(true);
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {

		if (value instanceof AbstractBuggle) {
			AbstractBuggle b = (AbstractBuggle) value;
			setText(b.getName());
		} else {
			setText(value!=null?value.toString():"");
		}     
       
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        
        setFont(list.getFont());
        setOpaque(true);
        return this;
	}

}
