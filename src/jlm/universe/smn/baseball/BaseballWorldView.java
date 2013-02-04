package jlm.universe.smn.baseball;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

import javax.swing.ImageIcon;

import jlm.core.ui.ResourcesCache;
import jlm.core.ui.WorldView;
import jlm.universe.World;

public class BaseballWorldView extends WorldView
{
	private static final long serialVersionUID = 1L;


	public BaseballWorldView(World w)
	{
		super(w);
	}
	
	/**
	 * Draw the component of the world
	 * @param g The Graphics2D context to draw on
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		double renderedX = 300.;
		double renderedY = 250.;		
		double ratio = Math.min(((double) getWidth()) / renderedX, ((double) getHeight()) / renderedY);
		g2.translate(Math.abs(getWidth() - ratio * renderedX)/2., Math.abs(getHeight() - ratio * renderedY)/2.);
		g2.scale(ratio, ratio);
		
		/* clear board */
		g2.setColor(Color.white);
		g2.fill(new Rectangle2D.Double(0., 0., renderedX, renderedY));
		
		BaseballWorld myWorld = (BaseballWorld) this.world ;
		
		int theta = 360 / myWorld.getAmountOfBases();
		int r = 75 ;
		
		for ( int i=0 ; i < myWorld.getAmountOfBases();i++)
		{
			drawBase(g2, r, theta*i, (int) renderedX/2, (int) renderedY/2 , myWorld.field.getBase(i));
		}
	}
	
	/**
	 * Draw a rectangle which represent the base
	 * @param g The Graphics2D context to draw on
	 * @param r the distance between the symmetry center of the rectangle and the point of Cartesian coordinates ( x , y )
	 * @paran theta the angle made between the x-axis of the two-dimensional Cartesian system of origin ( x , y ) and the symmetry center of the rectange
	 * @param x the x coordinate in the classic coordinate system in graphics of the origin of our Cartesian system
	 * @param y the y coordinate in the classic coordinate system in graphics of the origin of our Cartesian system
	 * @param base the base that we want to draw
	 */
	private void drawBase(Graphics2D g, int r, int theta, int x, int y, BaseballBase base) {
		// draw the base
		
		
		//draw PlayerOne as a disc
		  int playerRadius = 0;
		  int centerPlayerOne = 0; 
		  Color colorPlayerOne = obtainColor(base.getPlayer1().getColor());
		  drawPlayer(g, centerPlayerOne, playerRadius, colorPlayerOne);
		  
		// draw Player Two
		  int centerPlayerTwo = 0;
		  Color colorPlayerTwo = obtainColor(base.getPlayer2().getColor());
		  drawPlayer(g, centerPlayerTwo, playerRadius, colorPlayerTwo);
	}

	/**
	 * Draw a disk which represent the player
	 * @param g The Graphics2D context to draw on
	 * @param centerPlayerOne the center of the disk which will represent the player
	 * @param colorPlayer the color with which the disk shall be filled
	 */
	private void drawPlayer(Graphics2D g, int centerPlayerOne, int diskRadius,Color colorPlayer) {
	
	}
	
	/**
	 * Return the color corresponding to colorIndex
	 * @param colorIndex a 
	 * @return the color corresponding to colorIndex
	 */
	private Color obtainColor(int colorIndex) {
		Color colorSent ;
		switch (colorIndex)
		{
		case 0:
			colorSent = Color.BLACK;
			break;
		case 1:
			colorSent = Color.RED;
			break;
		case 2:
			colorSent = Color.BLUE;
			break;
		case 3:
			colorSent = Color.GREEN;
			break;
		case 4:
			colorSent = Color.ORANGE;
			break;
		case 5:
			colorSent = Color.PINK;
			break;
		case 6:
			colorSent = Color.YELLOW;
			break;
		default:
			System.out.println("Unexpected colorIndex : "+colorIndex+"\nYou should add some colors in BaseballWorldView.obtainColor");
			colorSent = Color.MAGENTA;
			break;
		}
		return colorSent;
	}
	
	/**
	 * Return the icon of the world
	 * @return the icon for the exercise selection
	 */
	public ImageIcon getIcon()
	{
		return ResourcesCache.getIcon("resources/IconWorld/baseball.png");
	}

}
