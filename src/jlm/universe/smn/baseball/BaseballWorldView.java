package jlm.universe.smn.baseball;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.QuadCurve2D;
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
	   * Draws an arrow on the given Graphics2D context
	   * ( adapted from http://www.bytemycode.com/snippets/snippet/82/ )
	   * @param g The Graphics2D context to draw on
	   * @param xTail The x location of the "tail" of the arrow
	   * @param yTail The y location of the "tail" of the arrow
	   * @param xHead The x location of the "head" of the arrow
	   * @param yHead The y location of the "head" of the arrow
	   */
	  private void drawArrow( Graphics2D g, int xTail, int yTail, int xHead, int yHead)
	  {
	    float arrowWidth = 12.0f ;
	    float theta = 0.6f ;
	    int[] xPoints = new int[ 3 ] ;
	    int[] yPoints = new int[ 3 ] ;
	    float[] vecLine = new float[ 2 ] ;
	    float[] vecLeft = new float[ 2 ] ;
	    float fLength;
	    float th;
	    float ta;
	    float baseX, baseY ;

	    xPoints[ 0 ] = xHead ;
	    yPoints[ 0 ] = yHead ;

	    // build the line vector
	    vecLine[ 0 ] = (float)xPoints[ 0 ] - xTail ;
	    vecLine[ 1 ] = (float)yPoints[ 0 ] - yTail ;

	    // build the arrow base vector - normal to the line
	    vecLeft[ 0 ] = -vecLine[ 1 ] ;
	    vecLeft[ 1 ] = vecLine[ 0 ] ;

	    // setup length parameters
	    fLength = (float)Math.sqrt( vecLine[0] * vecLine[0] + vecLine[1] * vecLine[1] ) ;
	    th = arrowWidth / ( 2.0f * fLength ) ;
	    ta = arrowWidth / ( 2.0f * ( (float)Math.tan( theta ) / 2.0f ) * fLength ) ;

	    // find the base of the arrow
	    baseX = ( (float)xPoints[ 0 ] - ta * vecLine[0]);
	    baseY = ( (float)yPoints[ 0 ] - ta * vecLine[1]);

	    // build the points on the sides of the arrow
	    xPoints[ 1 ] = (int)( baseX + th * vecLeft[0] );
	    yPoints[ 1 ] = (int)( baseY + th * vecLeft[1] );
	    xPoints[ 2 ] = (int)( baseX - th * vecLeft[0] );
	    yPoints[ 2 ] = (int)( baseY - th * vecLeft[1] );

	    g.drawLine( xTail, yTail, (int)baseX, (int)baseY ) ;
	    g.fillPolygon( xPoints, yPoints, 3 ) ;
	  }
	
	/**
	 * Draw the base and the players
	 * @param g The Graphics2D context to draw on
	 * @param L A coefficient which adapt the length of the arrow to the total amount of bases
	 * @param r the distance between the symmetry center of the rectangle and the point of Cartesian coordinates ( x , y )
	 * @paran theta the angle made between the x-axis of the two-dimensional Cartesian system of origin ( x , y ) and the symmetry center of the rectange
	 * @param x the x coordinate in the classic coordinate system in graphics of the origin of our Cartesian system
	 * @param y the y coordinate in the classic coordinate system in graphics of the origin of our Cartesian system
	 * @param base the base that we want to draw
	 * @param amountOfPlayers the total amount of players on the base
	 */
	private void drawBase(Graphics2D g, int L, int r, double theta, int x, int y, BaseballBase base, int amountOfPlayers) {
		int[] xPoints = new int[4];
		int[] yPoints = new int[4];
		
		// Prevent some redundancies during the computing
		double commonPart = L*Math.sin(theta);
		double rightPart = (r+L/2)*Math.cos(theta);
		double leftPart = (r-L/2)*Math.cos(theta) ;
		
		xPoints[0] = (int) (x - commonPart + rightPart); // x coordinate of the upper-right point when theta = 0
		xPoints[1] = (int) (x + commonPart + rightPart); // x coordinate of the lower-right point when theta = 0
		xPoints[2] = (int) (x + commonPart + leftPart); // x coordinate of the lower-left point when theta = 0
		xPoints[3] = (int) (x - commonPart + leftPart); // x coordinate of the upper-left point when theta = 0
		
		commonPart = L*Math.cos(theta);
		rightPart = -(r+L/2)*Math.sin(theta);
		leftPart = -(r-L/2)*Math.sin(theta) ;
		
		yPoints[0] = (int) (y - commonPart + rightPart); // y coordinate of the upper-right point when theta = 0
		yPoints[1] = (int) (y + commonPart + rightPart); // y coordinate of the lower-right point when theta = 0
		yPoints[2] = (int) (y + commonPart + leftPart); // y coordinate of the lower-left point when theta = 0
		yPoints[3] = (int) (y - commonPart + leftPart); // y coordinate of the upper-left point when theta = 0
		
		// draw the base
		drawRectangle(g,xPoints,yPoints,obtainColor(base.getColor()));
		
		// the radius of the disk representing the player
		int radius = L/amountOfPlayers-2; 
		/* 
		 * This array contains the coordinates of the middle of the upper segment ( when theta = 0 ) of the base
		 * 0 => x coordinate ; 1 => y coordinate
		 */
		int[] middleLower = { (xPoints[1]+xPoints[2])/2 , (yPoints[1]+yPoints[2])/2  };
		/* 
		 * This array contains the coordinates of the middle of the lower segment ( when theta = 0 ) of the base
		 * 0 => x coordinate ; 1 => y coordinate
		 */
		int[] middleUpper =  { (xPoints[0]+xPoints[3])/2 , (yPoints[0]+yPoints[3])/2 };
		// This array contains the coordinates of the "step" between two disks -- 0 => x step ; 1 => y step
		int[] delta = { (middleUpper[0] - middleLower[0])/amountOfPlayers , (middleUpper[1] - middleLower[1])/amountOfPlayers };
		// This array contains the coordinates of the center of the disk representing the player -- 0 => x coordinate ; 1 => y coordinate
		int centerPlayer[] = new int[2]; 
		// color of the player
		Color colorPlayer = null;
		//draw the players as disks
		for ( int i = 0 ; i < amountOfPlayers ; i++)
		{
			// x coordinate of the center of the disk representing the player
			centerPlayer[0] = (int) (middleUpper[0] - (i+.5)*delta[0] ); 
			// y coordinate of the center of the disk representing the player
			centerPlayer[1] = (int) (middleUpper[1] - (i+.5)*delta[1]);  

			colorPlayer = obtainColor(base.getPlayer(i).getColor());	
			drawDisk(g, centerPlayer, radius, colorPlayer);
		}
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
	 * @param g The Graphics2D context to draw on
	 * @param move The baseball move that we want to draw
	 * @param radius The distance between the center of the screen and end of the line
	 * @param theta The default angle between two bases
	 * @param xControl The center of the screen in x-axis
	 * @param yControl The center of the screen in y-axis
	 * @param L A coefficient which adapt the length of the arrow to the total amount of bases
	 */
	private void drawLastMove(Graphics2D g, BaseballMove move, int radius, double theta, double xControl, double yControl , int L){
			Stroke s = g.getStroke();
			double xBegin = 0 ;
			double yBegin = 0 ;
			double xEnd = 0 ;
			double yEnd = 0 ;
			int yHead=0;
			int xHead=0;
			
			double thetaSrc = move.getBaseSrc() * theta ;
			double thetaDst = move.getBaseDst() * theta ;
			int radiusEnd = radius-15;
			
			g.setColor(obtainColor(move.getPlayerColor()));
			g.setStroke(new BasicStroke(
					3.0f,						// Width
					BasicStroke.CAP_ROUND,		// End cap
                    BasicStroke.JOIN_BEVEL,		// Join style
                    10.0f,						// Miter limit
                    new float[] {5.0f,5.0f},	// Dash pattern
                    0.0f						// Dash phase
                    ));
			if ( move.getPlayerSrc() == 0)
			{
				xBegin = xControl+radius*Math.cos(thetaSrc)-(L/2)*Math.sin(thetaSrc);
				yBegin = yControl-((L/2)*Math.cos(thetaSrc)+radius*Math.sin(thetaSrc))  ;
			}
			else
			{
				xBegin= xControl+radius*Math.cos(thetaSrc)+(L/2)*Math.sin(thetaSrc);
				yBegin= yControl-(-(L/2)*Math.cos(thetaSrc)+radius*Math.sin(thetaSrc)) ;
			}
			if ( move.getPlayerDst() == 0)
			{
				xEnd =  xControl+radiusEnd*Math.cos(thetaDst)-(L/2)*Math.sin(thetaDst);
				yEnd =  yControl-((L/2)*Math.cos(thetaDst)+radiusEnd*Math.sin(thetaDst));
				
				xHead =  (int) (xControl+radius*Math.cos(thetaDst)-(L/2)*Math.sin(thetaDst));
				yHead =  (int) (yControl-((L/2)*Math.cos(thetaDst)+radius*Math.sin(thetaDst)));
			}
			else
			{
				xEnd= xControl+radiusEnd*Math.cos(thetaDst)+(L/2)*Math.sin(thetaDst);
				yEnd =yControl-(-(L/2)*Math.cos(thetaDst)+radiusEnd*Math.sin(thetaDst)) ;
				
				xHead= (int) (xControl+radius*Math.cos(thetaDst)+(L/2)*Math.sin(thetaDst));
				yHead =(int) (yControl-(-(L/2)*Math.cos(thetaDst)+radius*Math.sin(thetaDst))) ;
			}
			g.draw(new QuadCurve2D.Double(xBegin, yBegin, xControl, yControl, xEnd, yEnd));
			g.setStroke(s);

			drawArrow(g,(int) xEnd, (int) yEnd, xHead, yHead);
	}

	/**
	 * Draw a rectangle representing the base. We can't use drawRectangle here.
	 * @param g The Graphics2D context to draw on
	 * @param xPoints the four x coordinates of the corners of the rectangle 
	 * @param yPoints the four y coordinates of the corners of the rectangle 
	 * @param baseColor the color in which we will draw the base
	 */
	private void drawRectangle(Graphics2D g, int[] xPoints, int[] yPoints, Color baseColor ) {
		g.setColor(Color.BLACK);
		g.drawPolygon(xPoints, yPoints, 4);
		g.setColor(baseColor);
		g.fillPolygon(xPoints, yPoints, 4);
	}
	
	/**
	 * Return the icon of the world
	 * @return the icon for the exercise selection
	 */
	public ImageIcon getIcon()
	{
		return ResourcesCache.getIcon("resources/IconWorld/baseball.png");
	}
	
	/**
	 * Return the color corresponding to colorIndex
	 * ( from : http://en.wikipedia.org/wiki/List_of_colors )
	 * @param colorIndex a 
	 * @return the color corresponding to colorIndex
	 */
	private Color obtainColor(int colorIndex) {
		Color[] colors = {
				Color.BLACK, Color.RED, Color.BLUE, Color.YELLOW, 
				new Color(158,253,56), /* French lime */
				new Color(255,56,0), /* Coquelicot */ 
				new Color(204,204,255), /* Lavender blue */
				new Color(251,206,177), /* Apricot */
				new Color(0,103,165), /* Blue Persian */
				new Color(201,0,22), /* Harvard crimson */
				new Color(111,78,55), /* Coffee */
				new Color(109,7,26), /* Bordeaux */
				new Color(155,150,10),
				new Color(75,0,130), 
				new Color(150,85,120),
				Color.GREEN
		};
		Color colorSent ;
		if ( colorIndex < -1 || colorIndex > colors.length-1 )
		{
			System.out.println("Unexpected colorIndex : "+colorIndex+"\nYou should add some colors in BaseballWorldView.obtainColor");
			colorSent = Color.MAGENTA;
		}
		else
		{
			colorSent = colors[colorIndex+1];
		}
		return colorSent;
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
		g2.setColor(new Color(58,157,35)); // lawn
		g2.fill(new Rectangle2D.Double(0., 0., renderedX, renderedY));
		
		int radius = 120 ;
		int[] fieldCenter = {(int) renderedX/2, (int) renderedY/2};
		drawDisk(g2, fieldCenter , radius+30, new Color(174,74,52)); // draw the play area
		
		BaseballWorld myWorld = (BaseballWorld) this.world ;
		int amountOfBases = myWorld.getAmountOfBases(); // amount of bases
		double theta = 2*Math.PI / amountOfBases; // angle between center of symmetry of two bases
		int amountOfPlayers = myWorld.getLocationsAmount(); // amount of players on a base
		int L = Math.max(3*(20-amountOfBases),10); // adapting the size of the base to the total amount of bases
		radius+=amountOfBases-5; // adapting the position of the base to the total amount of bases
		
		// Draw the bases and the players on each one
		for ( int i=0 ; i < myWorld.getAmountOfBases();i++)
		{
			drawBase(g2, L, radius, theta*i, (int) renderedX/2, (int) renderedY/2 , myWorld.field.getBase(i),amountOfPlayers);
		}
		// Draw the last move made on the field if it exists
		if ( myWorld.getLastMove() != null)
		{
			drawLastMove(g2, myWorld.getLastMove(), radius-L/2-3, theta, renderedX/2, renderedY/2, L);
		}
	}

}
