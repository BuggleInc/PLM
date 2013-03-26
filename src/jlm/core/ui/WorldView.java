package jlm.core.ui;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import jlm.universe.IWorldView;
import jlm.universe.World;



public abstract class WorldView extends JComponent  implements IWorldView {

	private static final long serialVersionUID = -4599730915218800968L;

	protected World world;
	
	public WorldView(World w) {
		this.world = w;
		w.doDelay();
		this.world.addWorldUpdatesListener(this);
	}
	
	public void setWorld(World w) {
		this.world.removeWorldUpdatesListener(this);
		this.world = w;
		this.world.addWorldUpdatesListener(this);
		worldHasMoved();
	}

	@Override
	public void worldHasMoved() {
		if (SwingUtilities.isEventDispatchThread()) {
			repaint();
		} else {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					repaint();
				}
			});			
		}
	}
	
	@Override
	public void worldHasChanged() {
		/* nothing specific to do here since we already react to HasMoved */
	}

	public boolean isWorldCompatible(World world) {
		return world.getClass().equals(this.world.getClass());
	}

	/** Returns what should be added to the tab name */
	public String getTabName() {
		return ""; 
	}

	/** Returns what should be added to the tooltip */
	public String getTip() {
		return "";
	}

	public abstract ImageIcon getIcon();

}
