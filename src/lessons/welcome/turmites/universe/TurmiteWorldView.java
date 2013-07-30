package lessons.welcome.turmites.universe;

import java.awt.Color;
import java.awt.Graphics;

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
	
}
