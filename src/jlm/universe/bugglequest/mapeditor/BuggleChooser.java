/**
 * JComboxBox of buggles used to select a buggle in order to move him in the map
 */

package jlm.universe.bugglequest.mapeditor;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import jlm.universe.Entity;
import jlm.universe.IWorldView;
import jlm.universe.World;

public class BuggleChooser extends JComboBox implements ActionListener, IWorldView
{
	World world;
	
	public BuggleChooser(World world)
	{
		this.world = world;
		setEditable(false);
		setToolTipText("Switch the buggle");
		setRenderer(new ListCellRenderer()
		{
			protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
			private final Dimension preferredSize = new Dimension(0, 20);
			
			@Override
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus)
			{
				JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			    if (value instanceof Entity) {
			      renderer.setText(((Entity)value).getName());
			    }
			    renderer.setPreferredSize(preferredSize);
			    return renderer;
			}
		});
		for(Entity e:world.getEntities())
			addItem(e);
	}
	
	public void actionPerformed(ActionEvent e)
	{
        JComboBox cb = (JComboBox)e.getSource();
        String petName = (String)cb.getSelectedItem();
    }

	@Override
	public void worldHasMoved()
	{
		// TODO Auto-generated method stub
		
	}

	public void addItem(Object e)
	{
		boolean exist = false;
		for (int i=0; i<getItemCount(); i++)
		{
			Object elt = getItemAt(i);
			if (elt.equals(e))
			{
				exist = true;
				break;
			}
		}
		
		if (!exist)
			super.addItem(e);
	}
	
	@Override
	public void worldHasChanged()
	{
		for(Entity e:world.getEntities())
			addItem(e);	
	}
}