package jlm.core.ui;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import jlm.universe.Entity;


public class EntityCellRenderer extends JLabel implements ListCellRenderer {

	private static final long serialVersionUID = 5274134398293975047L;

	public EntityCellRenderer() {
		//setOpaque(true);
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {

		if (value instanceof Entity) {
			Entity b = (Entity) value;
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
