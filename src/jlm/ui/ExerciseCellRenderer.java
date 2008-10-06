package jlm.ui;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import lessons.Exercise;

public class ExerciseCellRenderer extends JLabel implements ListCellRenderer {

	private static final long serialVersionUID = -3830260372340239019L;
    //final static ImageIcon longIcon = new ImageIcon("long.gif");
    //final static ImageIcon shortIcon = new ImageIcon("short.gif");


	public ExerciseCellRenderer() {
		//setOpaque(true);
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {

		if (value instanceof Exercise) {
			Exercise exo = (Exercise) value;

			setText(exo.getName());
			boolean accessible = exo.getLesson().isAccessible(exo);			
			setEnabled(accessible);
			if (accessible) {
				if (exo.isSuccessfullyPassed()) {
					setIcon(ResourcesCache.getIcon("resources/star.png"));
				} else {
					setIcon(ResourcesCache.getIcon("resources/star_white.png"));
				}
			}
		} else {
			setText(value!=null?value.toString():"");
	        setEnabled(list.isEnabled());
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
