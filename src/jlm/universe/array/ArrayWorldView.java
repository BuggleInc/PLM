package jlm.universe.array;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

import javax.swing.ImageIcon;

import jlm.core.ui.ResourcesCache;
import jlm.core.ui.WorldView;
import jlm.universe.World;

public class ArrayWorldView extends WorldView {

		private static final long serialVersionUID = 1L;

		public ArrayWorldView(World w) {
			super(w);
		}
		
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(Color.white);
			g2.fill(new Rectangle2D.Double(0.,0.,(double)getWidth(),(double)getHeight()));

			int[] values = ((ArrayWorld) world).getValues();
			int dx = 30; // 30 pixels square
			
			int cellsByLineCount = Math.min(getWidth() / dx, values.length);
			int lineWidth = cellsByLineCount * dx;
			int xorigin = (getWidth()-lineWidth) / 2;
			int yorigin = 10;
			
			
			//int rowHeight = (dx+5);
			//g2.translate(Math.abs((getWidth() - lineWidth) / 2.), 
			//		Math.abs(getHeight() - rowHeight*(1+(values.length % cellsByLineCount))) / 2.);
			FontMetrics fm;

			int x = xorigin;
			int y = yorigin;
			for (int i=0; i<values.length;  i++) {
				if (x+dx > getWidth()) {
					y += dx+20;
					x = xorigin;
				}
				g2.setColor(Color.black);
				g2.draw(new Rectangle2D.Double(x, 5+y, dx, dx));

				g2.setFont(new Font("Monaco", Font.PLAIN, 9));
				fm = g2.getFontMetrics();
				String str = Integer.toString(i);
				g2.drawString(str, x+(dx-fm.stringWidth(str))/2, y+(dx+fm.getHeight())+2);				
				
				g2.setColor(Color.blue);
				g2.setFont(new Font("Monaco", Font.PLAIN, 14));
				fm = g2.getFontMetrics();
				str = Integer.toString(values[i]);
				g2.drawString(str, x+(dx-fm.stringWidth(str))/2, y+(dx+fm.getHeight())/2+2);				
				
				
				
				
				x += dx;
			}
	
			g2.setColor(Color.red);
			g2.setFont(new Font("Monaco", Font.PLAIN, 18));
			fm = g2.getFontMetrics();

			ArrayEntity e = (ArrayEntity) world.getEntity(0);			
			int result = e.getResult();
			String resultString = "value = "+((result == Integer.MIN_VALUE)?"n/a":Integer.toString(result));
	
			y = getHeight() - fm.getHeight();
			x = (getWidth()-fm.stringWidth(resultString)) / 2 ;
			g2.drawString(resultString, x, y);
		}

		@Override
		public ImageIcon getIcon() {
			return ResourcesCache.getIcon("resources/IconWorld/arrays.png");
		}
}
