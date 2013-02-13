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

/**
 * @author Julien BASTIAN & Geoffrey HUMBERT
 * @see WorldView
 * @see BaseballWorld
 */
public class BaseballWorldView extends WorldView
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the class BaseballWorldView
	 * @param w : a world
	 */
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
		double renderedY = 300.;		
		double ratio = Math.min(((double) getWidth()) / renderedX, ((double) getHeight()) / renderedY);
		g2.translate(Math.abs(getWidth() - ratio * renderedX)/2., Math.abs(getHeight() - ratio * renderedY)/2.);
		g2.scale(ratio, ratio);
		
		/* drawn the field */
		g2.setColor(new Color(58,157,35)); // gazon
		g2.fill(new Rectangle2D.Double(0., 0., renderedX, renderedY));
		
		int radius = 120 ;
		int[] fieldCenter = {(int) renderedX/2, (int) renderedY/2};
		drawDisk(g2, fieldCenter , radius+30, new Color(174,74,52));
		
		BaseballWorld myWorld = (BaseballWorld) this.world ;
		int amountOfBases = myWorld.getAmountOfBases();
		double theta = 2*Math.PI / amountOfBases;
		
		for ( int i=0 ; i < myWorld.getAmountOfBases();i++)
		{
			drawBase(g2, radius, theta*i, (int) renderedX/2, (int) renderedY/2 , myWorld.field.getBase(i),amountOfBases);
		}
	}
	
	/**
	 * Draw the base and the players
	 * @param g The Graphics2D context to draw on
	 * @param r the distance between the symmetry center of the rectangle and the point of Cartesian coordinates ( x , y )
	 * @paran theta the angle made between the x-axis of the two-dimensional Cartesian system of origin ( x , y ) and the symmetry center of the rectange
	 * @param x the x coordinate in the classic coordinate system in graphics of the origin of our Cartesian system
	 * @param y the y coordinate in the classic coordinate system in graphics of the origin of our Cartesian system
	 * @param base the base that we want to draw
	 * @param amountOfBases the total amount of bases in the field
	 */
	private void drawBase(Graphics2D g, int r, double theta, int x, int y, BaseballBase base,int amountOfBases) {
		
		int L = Math.max(3*(20-amountOfBases),10); // adapting the size of the base to the total amount of bases
		r+=amountOfBases-5; // adapting the position of the base to the total amount of bases
		
		// draw the base
		drawRectangle(g,r,theta,x,y,obtainColor(base.getColor()),L);
		
		int radius = L/2-2; // the radius of the disk
		
		//draw PlayerOne as a disc
		int centerPlayerOne[] = new int[2]; 
		
		centerPlayerOne[0] = (int) (x+r*Math.cos(theta)-(L/2)*Math.sin(theta)); // x coordinate of the center of the disk representing player one
		centerPlayerOne[1] = (int) (y-((L/2)*Math.cos(theta)+r*Math.sin(theta))); // y coordinate of the center of the disk representing player one
		  
		Color colorPlayerOne = obtainColor(base.getPlayer(0).getColor());
		drawDisk(g, centerPlayerOne, radius, colorPlayerOne);
		  
		// draw Player Two
		int centerPlayerTwo[] = new int[2];
		
		centerPlayerTwo[0] = (int) (x+r*Math.cos(theta)+(L/2)*Math.sin(theta)); // x coordinate of the center of the disk representing player two
		centerPlayerTwo[1] = (int) (y-(-(L/2)*Math.cos(theta)+r*Math.sin(theta))); // y coordinate of the center of the disk representing player two
		
		Color colorPlayerTwo = obtainColor(base.getPlayer(1).getColor());
		drawDisk(g, centerPlayerTwo, radius, colorPlayerTwo);
	}

	/**
	 * Draw a rectangle representing the base
	 * @param g The Graphics2D context to draw on
	 * @param r the distance between the symmetry center of the rectangle and the point of Cartesian coordinates ( x , y )
	 * @paran theta the angle made between the x-axis of the two-dimensional Cartesian system of origin ( x , y ) and the symmetry center of the rectange
	 * @param x the x coordinate in the classic coordinate system in graphics of the origin of our Cartesian system
	 * @param y the y coordinate in the classic coordinate system in graphics of the origin of our Cartesian system
	 * @param baseColor the color in which we will draw the base
	 * @param L a coefficient which change the size of the rectangle depending from the amount of bases
	 */
	private void drawRectangle(Graphics2D g, int r, double theta, int x, int y,Color baseColor,int L) {
		int amountOfPoints = 4 ;
		int[] xPoints = new int[amountOfPoints];
		int[] yPoints = new int[amountOfPoints];
		
		xPoints[0] = (int) (x - L*Math.sin(theta) + (r+L/2)*Math.cos(theta)); // x coordinate of the upper-left point
		xPoints[1] = (int) (x + L*Math.sin(theta) + (r+L/2)*Math.cos(theta)); // x coordinate of the upper-right point
		xPoints[2] = (int) (x + L*Math.sin(theta) + (r-L/2)*Math.cos(theta)); // x coordinate of the lower-right point
		xPoints[3] = (int) (x - L*Math.sin(theta) + (r-L/2)*Math.cos(theta)); // x coordinate of the lower-left point
		
		yPoints[0] = (int) (y - ( L*Math.cos(theta) + (r+L/2)*Math.sin(theta))); // y coordinate of the upper-left point
		yPoints[1] = (int) (y - ( -L*Math.cos(theta) + (r+L/2)*Math.sin(theta))); // y coordinate of the upper-right point
		yPoints[2] = (int) (y - ( -L*Math.cos(theta) + (r-L/2)*Math.sin(theta))); // y coordinate of the lower-right point
		yPoints[3] = (int) (y - ( L*Math.cos(theta) + (r-L/2)*Math.sin(theta))); // y coordinate of the lower-left point
		
		g.setColor(baseColor);
		g.fillPolygon(xPoints, yPoints, amountOfPoints);
		
		g.setColor(Color.BLACK);
		g.drawPolygon(xPoints, yPoints, amountOfPoints);
	}

	/**
	 * Draw a disk which represent the player
	 * @param g The Graphics2D context to draw on
	 * @param centerPlayerOne the center of the disk which will represent the player
	 * @param color the color with which the disk shall be filled
	 */
	private void drawDisk(Graphics2D g, int[] center, int radius,Color color) {
		g.setColor(Color.BLACK);
		g.drawOval(center[0]-radius, center[1]-radius, radius*2, radius*2);
		g.setColor(color);
		g.fillOval(center[0]-radius, center[1]-radius, radius*2, radius*2);
	}
	
	/**
	 * Draw an arrow which represent the last movement
	 * @see http://manu.kegtux.org/Java/Tutoriels/AWT/Graphisme2D.html
	 * 
	 */
	private void drawMovement(){;}
	
	/**
	 * Return the color corresponding to colorIndex
	 * ( from : http://fr.wikipedia.org/wiki/Liste_de_couleurs )
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
			colorSent = Color.YELLOW;
			break;
		case 4:
			colorSent = new Color(158,253,56); // vert lime
			break;
		case 5:
			colorSent =  new Color(255,73,1); // feu
						break;
		case 6:
			colorSent = new Color(204,204,255); // pervenche
			break;
		case 7:
			colorSent = new Color(230,126,48); // abricot
			break;
		case 8:
			colorSent = new Color(102,0,255); // bleu persan
			break;
		case 9:
			colorSent = new Color(109,7,26); // bordeaux
			break;
		case 10:
			colorSent = new Color(53,122,183); // Caeruléum
			break;
		case 11:
			colorSent = new Color(70,46,1); // café
			break;
		case 12:
			colorSent = new Color(254,195,172); // carnation
			break;
		case 13:
			colorSent = new Color(75,0,130); // indigo du web
			break;
		case 14:
			colorSent = new Color(150,85,120); // mauve
			break;
		case 15:
			colorSent = Color.GREEN;
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
