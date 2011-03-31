package jlm.universe.bat;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.util.List;

import jlm.core.model.Game;
import jlm.core.model.lesson.Exercise;
import jlm.core.ui.WorldView;
import jlm.universe.World;

public class BatWorldView extends WorldView {

		private static final long serialVersionUID = 1L;
		private List<World> worlds;
		private List<World> answerWorlds=null;
		
		public BatWorldView(World w) {
			super(w);
			Exercise exo = Game.getInstance().getCurrentLesson().getCurrentExercise(); 
			if (exo.getCurrentWorld().contains(w)) {
				worlds = exo.getCurrentWorld();
				answerWorlds = exo.getAnswerWorld();
			} else if (exo.getAnswerWorld().contains(w)){
				worlds = exo.getAnswerWorld();				
			} else if (exo.getInitialWorld().contains(w)){
				worlds = exo.getInitialWorld();
			} else {
				throw new RuntimeException("Cannot find the world in any known list of worlds");
			}
		}
		
		@Override
		public boolean isWorldCompatible(World world) {
			return false;// re-get the list of worlds every time
		}
	
		@Override
		public void paintComponent(Graphics g) {
			
			super.paintComponent(g);
			
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(Color.white);
			g2.fill(new Rectangle2D.Double(0.,0.,(double)getWidth(),(double)getHeight()));
			g2.setColor(Color.black);
			for (int i=0;i<worlds.size();i++) {
				BatWorld bw = (BatWorld) worlds.get(i);
				if (answerWorlds != null) {
					BatWorld a = (BatWorld) answerWorlds.get(i);
					if (bw.result == null) {
						if (bw.visible)
							g2.setColor(Color.black);
						else
							g2.setColor(Color.white);
					} else if (a.result.equals(bw.result)) {
						g2.setColor(Color.blue);
					} else {
						g2.setColor(Color.red);
					}
				} else { // we are in objective 
					if (bw.visible)
						g2.setColor(Color.black);
					else
						g2.setColor(Color.white);					
				}
				g2.drawString(bw.getName()+"="+(bw.result==null?"":bw.result.toString()), 0, (i+1)*20);
			}
		}
}
