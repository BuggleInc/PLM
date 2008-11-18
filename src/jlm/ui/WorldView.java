package jlm.ui;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import universe.IWorldView;
import universe.World;


public abstract class WorldView extends JComponent  implements IWorldView {

	private static final long serialVersionUID = -4599730915218800968L;

	protected World world;
	
	public WorldView(World w) {
		this.world = w;
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
		//Logger.log("WorldView:worldHasMoved","");
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				//Logger.log("WorldView:worldHasMoved:SwingUtilities","(ask for repaint)");
				repaint();
			}
		});
	}
	
	@Override
	public void worldHasChanged() {
		/* nothing specific to do here since we already react to HasMoved */
	}

	public abstract boolean isWorldCompatible(World world);

}
