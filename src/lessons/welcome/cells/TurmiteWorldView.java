package lessons.welcome.cells;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.ImageIcon;

import jlm.core.ui.ResourcesCache;
import jlm.universe.World;
import jlm.universe.bugglequest.ui.BuggleWorldView;



public class TurmiteWorldView extends BuggleWorldView {

	private static final long serialVersionUID = -7164642270965762239L;
	
	public TurmiteWorldView (World w) {
		super(w);
	}
		


	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(Color.black);
		g.drawString("step "+((TurmiteWorld)world).currStep, (int)getPadX()+10, (int)getPadY()+15);	
		
	}	
	
	@Override
	public ImageIcon getIcon() {
		return ResourcesCache.getIcon("resources/IconWorld/BuggleQuest.png");
	}
}
