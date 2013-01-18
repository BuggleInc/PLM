package jlm.universe.bat;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.util.List;

import javax.swing.ImageIcon;

import jlm.core.ui.ResourcesCache;
import jlm.core.ui.WorldView;
import jlm.universe.World;

public class BatWorldView extends WorldView {

		private static final long serialVersionUID = 1L;
		
		public BatWorldView(World w) {
			super(w);
		}
		
		@Override
		public boolean isWorldCompatible(World world) {
			return world instanceof BatWorld;
		}
	
		@Override
		public void paintComponent(Graphics g) {
			
			super.paintComponent(g);
			
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(Color.white);
			g2.fill(new Rectangle2D.Double(0.,0.,(double)getWidth(),(double)getHeight()));
			
			List<BatTest> tests = ((BatWorld) world).getTests();
			boolean foundError=false;
			for (int i=0;i<tests.size();i++) {
				BatTest currTest = tests.get(i);
				if (!currTest.isVisible() && foundError) 
					break;
				
				if (currTest.isObjective()) {
					if (currTest.isVisible()) 
						g2.setColor(Color.black);
					else 
						g2.setColor(Color.white);
				} else {
					if (currTest.isAnswered()) {
						
						if (currTest.isCorrect()) 
							g2.setColor(Color.blue);
						else { 
							g2.setColor(Color.red);
							foundError = true;
						}
					} else {
						if (currTest.isVisible()) 
							g2.setColor(Color.black);
						else 
							g2.setColor(Color.white);						
					}
				}
				g2.drawString(currTest.getName()+"="+currTest.getResult()
						+(currTest.isAnswered() && !currTest.isCorrect() ?" (expected: "+currTest.expected+")":"")
						, 0, (i+1)*20);
			}
				
		}
		
		@Override
		public ImageIcon getIcon() {
			return ResourcesCache.getIcon("resources/IconWorld/bat.png");
		}
}
