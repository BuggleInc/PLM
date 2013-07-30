package lessons.sort.baseball.universe;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import jlm.core.ui.WorldView;
import jlm.universe.World;

public class BaseballWorldView extends WorldView
{
	private static final long serialVersionUID = 1L;
	private Vector<Integer[]> playersCoordinate;
	private double radius;
	private double displayRatio;
	private double virtualSize = 300.; // we display on a field that is 300x300 and dynamically resized (to ease our computations)

	public BaseballWorldView(World w){
		super(w);
		addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				/* Recompute the coordinates of the click on our strange virtual coordinates system */
				int x=e.getX()-(int)(Math.abs(getWidth() - displayRatio * virtualSize)/2.);
				int y=e.getY()-(int)(Math.abs(getHeight() - displayRatio * virtualSize)/2.);
				
				x /= displayRatio;
				y /= displayRatio;

				double minDist = Double.MAX_VALUE;
				Integer[] min = null;
				for (Integer[] c : playersCoordinate) {
					int xComponent = x-c[0];
					int yComponent = y-c[1];
					double dist = Math.sqrt( xComponent*xComponent+yComponent*yComponent ); 
					if (dist < minDist) {
						minDist = dist;
						min = c;
					}
				}
				
				if (minDist < 2*radius) {
					//System.out.println("Minimal distance: "+minDist+" (radius was: "+radius+"). That's "+min[2]+","+min[3]);
					((BaseballWorld) world).setPlayer(min[2],min[3]);
					if (e.getClickCount()==2)
						((BaseballWorld) world).doMove();
				}
			}
		});
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
	private void drawArrow( Graphics2D g, int xTail, int yTail, int xHead, int yHead) {
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
	 * Compute the corners of the base located at polar coordinates ( radius, theta ) and return an array 
	 * which contains the coordinates of the corners of the base
	 * @param L A coefficient which adapt the length of the arrow to the total amount of bases
	 * @param radius the distance between the symmetry center of the rectangle and the point of Cartesian coordinates ( xCenter , yCenter )
	 * @param theta the angle made between the x-axis of the Cartesian system of origin ( xCenter , yCenter ) and the symmetry center of the rectange
	 * @param xCenter the x coordinate in the classic coordinate system in graphics of the origin of our Cartesian system
	 * @param yCenter the y coordinate in the classic coordinate system in graphics of the origin of our Cartesian system
	 * @return an array containing the coordinates of the four corners of the base
	 */
	private int[][] computeCorners(int L, int radius, double theta , int xCenter, int yCenter ) {
		int[][] points = new int[4][2];
		// Prevent some redundancies during the computing
		double commonPart = L*Math.sin(theta);
		double rightPart = (radius+L/2)*Math.cos(theta);
		double leftPart = (radius-L/2)*Math.cos(theta) ;

		points[0][0] = (int) (xCenter - commonPart + rightPart); // x coordinate of the upper-right point when theta = 0
		points[1][0] = (int) (xCenter + commonPart + rightPart); // x coordinate of the lower-right point when theta = 0
		points[2][0] = (int) (xCenter + commonPart + leftPart); // x coordinate of the lower-left point when theta = 0
		points[3][0] = (int) (xCenter - commonPart + leftPart); // x coordinate of the upper-left point when theta = 0

		commonPart = L*Math.cos(theta);
		rightPart = -(radius+L/2)*Math.sin(theta);
		leftPart = -(radius-L/2)*Math.sin(theta) ;

		points[0][1] = (int) (yCenter - commonPart + rightPart); // y coordinate of the upper-right point when theta = 0
		points[1][1] = (int) (yCenter + commonPart + rightPart); // y coordinate of the lower-right point when theta = 0
		points[2][1] = (int) (yCenter + commonPart + leftPart); // y coordinate of the lower-left point when theta = 0
		points[3][1] = (int) (yCenter - commonPart + leftPart); // y coordinate of the upper-left point when theta = 0

		return points;
	}

	/**
	 * Draw the base and the players
	 * @param g The Graphics2D context to draw on
	 * @param L A coefficient which adapt the length of the arrow to the total amount of bases
	 * @param dist the distance between the symmetry center of the rectangle and the point of Cartesian coordinates ( xCenter , yCenter )
	 * @param theta the angle made between the x-axis of the two-dimensional Cartesian system of origin ( xCenter , yCenter ) and the symmetry center of the rectangle
	 * @param xCenter the x coordinate in the classic coordinate system in graphics of the origin of our Cartesian system
	 * @param yCenter the y coordinate in the classic coordinate system in graphics of the origin of our Cartesian system
	 * @param base the base that we want to draw
	 * @param amountOfPlayers the total amount of players per base
	 */
	private void drawBase(Graphics2D g, int L, int dist, double theta, int xCenter, int yCenter, int base, int amountOfPlayers) {
		BaseballWorld field = (BaseballWorld) world; 
		int[][] points = computeCorners( L, dist, theta, xCenter, yCenter );

		// draw the base
		drawRectangle(g,points,obtainColor(base));

		// the radius of the disk representing the player. the graphically depicted is a bit smaller than the clickable
		this.radius = (L/amountOfPlayers-1); // clickable
		int radius = (int) (this.radius *.8);// graphically depicted


		/* 
		 * This array will contain the coordinates of the middle of the lower segment ( when theta = 0 ) of the base
		 * 0 => x coordinate ; 1 => y coordinate
		 */
		int[] middleLower = new int[2];
		/* 
		 * This array will contain the coordinates of the middle of the upper segment ( when theta = 0 ) of the base
		 * 0 => x coordinate ; 1 => y coordinate
		 */
		int[] middleUpper = new int[2];
		// This array will contain the coordinates of the "step" between two disks -- 0 => x step ; 1 => y step
		int[] delta = new int[2] ;

		middleUpper[0] = ( points[1][0] + points[2][0] ) /2 ;
		middleUpper[1] = ( points[1][1] + points[2][1] ) /2 ;
		middleLower[0] = ( points[0][0] + points[3][0] ) /2 ;
		middleLower[1] = ( points[0][1] + points[3][1] ) /2 ;

		delta[0] = (middleUpper[0] - middleLower[0])/amountOfPlayers ;
		delta[1] = (middleUpper[1] - middleLower[1])/amountOfPlayers ;

		/*
		 *  This array contains the coordinates of the center of the disk representing the player 
		 *	0 => x coordinate ; 1 => y coordinate
		 */
		int centerPlayer[] = new int[2]; 
		// color of the player
		Color colorPlayer = null;
		// Loop which computes the coordinates of the center of the disk, store it for the clicks and draws the resulting disk
		for ( int pos = 0 ; pos < amountOfPlayers ; pos++) {
			int player =  field.getPlayerColor(base,pos);
			centerPlayer[0] = (int) (middleUpper[0] - (pos+.5)*delta[0]); 
			centerPlayer[1] = (int) (middleUpper[1] - (pos+.5)*delta[1]);  

			colorPlayer = obtainColor(player);	

			playersCoordinate.add(new Integer[] { 
				centerPlayer[0], centerPlayer[1],
				base,pos
			});
			//System.out.println("center: "+centerPlayer[0]+","+centerPlayer[1]+" radius: "+this.radius+" player "+base+","+pos+" color:"+colorPlayer);
			drawPlayer(g, centerPlayer, radius, colorPlayer, theta, base==player || player == -1);
		}
	}

	/**
	 * Draw a disk which represent the player
	 * @param g The Graphics2D context to draw on
	 * @param centerPlayerOne the center of the disk which will represent the player
	 * @param color the color with which the disk shall be filled
	 */
	private void drawPlayer(Graphics2D g, int[] center, int radius, Color color, double theta, boolean isHome) {
		g.setColor(Color.WHITE);
		g.fillOval(center[0]-radius, center[1]-radius, radius*2, radius*2);
		
		if (!isHome) {
			g.setColor(Color.RED);
			Stroke s = g.getStroke();
			g.setStroke(new BasicStroke(
					2f,						// Width
					BasicStroke.CAP_ROUND,		// End cap
					BasicStroke.JOIN_BEVEL,		// Join style
					10.0f,						// Miter limit
					null,	// Dash pattern
					0.0f						// Dash phase
					));
			g.drawOval(center[0]-radius, center[1]-radius, radius*2, radius*2);
			g.setStroke(s);
		} else {
			g.setColor(Color.BLACK);
			g.drawOval(center[0]-radius, center[1]-radius, radius*2, radius*2);
		}
		
		if (!color.equals(Color.WHITE)) { // Don't draw a buggle on the hole
			Image img = getBuggleImage(color);
			AffineTransform t = new AffineTransform(1.0, 0, 0, 1.0, 
					center[0]-radius, center[1]-radius);
			double scale = 2*radius/((double)img.getWidth(null));
			t.scale(scale,scale);
			t.rotate(theta+Math.PI/2, img.getHeight(null)/2.,img.getHeight(null)/2.);
			g.drawImage(img, t, null);
		}
	}

	/**
	 * Draw an arrow which represent the last movement
	 * @param g The Graphics2D context to draw on
	 * @param move The baseball move that we want to draw
	 * @param r The distance between the center of the screen and end of the line
	 * @param theta The default angle between two bases
	 * @param xControl The center of the screen in x-axis
	 * @param yControl The center of the screen in y-axis
	 * @param L A coefficient which adapt the length of the arrow to the total amount of bases
	 */
	private void drawLastMove(Graphics2D g, BaseballMove move, int r, double theta, double xControl, double yControl , int L, int amountOfPlayers){
		// Save the previous stroke -- we will need it later
		Stroke s = g.getStroke();
		// Set the color to the color of the player who moved
		g.setColor(obtainColor(move.getPlayerColor()));
		// Modifies the stroke so the drawing is a dotted line
		g.setStroke(new BasicStroke(
				3.0f,						// Width
				BasicStroke.CAP_ROUND,		// End cap
				BasicStroke.JOIN_BEVEL,		// Join style
				10.0f,						// Miter limit
				new float[] {5.0f,5.0f},	// Dash pattern
				0.0f						// Dash phase
				));

		/*
		 * This array will contains the coordinates (x,y) of :
		 * -> the beginning of the arrow ( index 0 )
		 * -> the end of the tail of the arrow ( index 1 )
		 * -> the end of the head of the arrow 
		 */
		int[][] arrow = new int[3][2];
		// the step between two players of the same base
		int[] delta = new int[2] ;
		// The angle that will be used at each loop iteration
		double[] thetaBase = { move.getBaseSrc() * theta, move.getBaseDst() * theta, move.getBaseDst() * theta };
		// The radius that will be used at each loop iteration
		int[] radius = { r , r-2*L/3, r-L/4};
		// The players locations within its base
		int[] players = { move.getPlayerSrc() , move.getPlayerDst() , move.getPlayerDst() } ;
		// will contains the coordinates of the corners of the current base
		int[][] points = new int[4][2];
		// will contains the coordinates of the middle of the lower segment ( when theta = 0 ) of the current base 
		int[] middleLower = new int[2];
		// will contains the coordinates of the middle of the upper segment ( when theta = 0 ) of the current base 
		int[] middleUpper = new int[2];

		for ( int i = 0 ; i < 3 ; i++) {
			points = computeCorners(L, radius[i], thetaBase[i] , (int)(xControl),(int) (yControl) );

			middleUpper[0] = ( points[1][0] + points[2][0] ) /2 ;
			middleUpper[1] = ( points[1][1] + points[2][1] ) /2 ;
			middleLower[0] = ( points[0][0] + points[3][0] ) /2 ;
			middleLower[1] = ( points[0][1] + points[3][1] ) /2 ;

			delta[0] = (middleUpper[0] - middleLower[0])/amountOfPlayers ;
			delta[1] = (middleUpper[1] - middleLower[1])/amountOfPlayers ;

			arrow[i][0]= (int) (middleUpper[0] - (players[i]+.5)*delta[0] ); 
			arrow[i][1]= (int) (middleUpper[1] - (players[i]+.5)*delta[1]) ;  
		}

		// Draw the tail of the arrow
		g.draw(new QuadCurve2D.Double(arrow[0][0], arrow[0][1], xControl, yControl, arrow[1][0], arrow[1][1]));
		g.setStroke(s);
		// Draw the head of the arrow
		drawArrow(g,arrow[1][0], arrow[1][1], arrow[2][0], arrow[2][1]);
	}

	/**
	 * Draw a rectangle representing the base. We can't use drawRectangle here.
	 * @param g The Graphics2D context to draw on
	 * @param points the four points representing the corners of the rectangle 
	 * @param baseColor the color in which we will draw the base
	 */
	private void drawRectangle(Graphics2D g, int[][] points, Color baseColor ) {
		// will contains the x coordinates of the points
		int[] xPoints = new int[4];
		// will contains the y coordinates of the points 
		int[] yPoints = new int[4];
		// fill the arrays mentioned earlier
		for (int i = 0 ; i < 4 ; i++) {
			xPoints[i] = points[i][0];
			yPoints[i] = points[i][1];
		}
		// draw the shape of the base
		g.setColor(Color.BLACK);
		g.drawPolygon(xPoints, yPoints, 4);
		// fill this shape
		g.setColor(baseColor);
		g.fillPolygon(xPoints, yPoints, 4);
	}

	/** Returns the color corresponding to colorIndex */
	private Color obtainColor(int colorIndex) {
		Color[] colors = {
				Color.WHITE, Color.BLUE, new Color(255,0,255), //Color.YELLOW, 
				new Color(255,204,0), /* gold */
				new Color(158,253,56), /* French lime */
				new Color(255,56,0), /* Coquelicot */ 
				new Color(204,204,255), /* Lavender blue */
				new Color(0,103,165), /* Blue Persian */
				new Color(201,0,22), /* Harvard crimson */
				new Color(111,78,55), /* Coffee */
				new Color(251,206,177), /* Apricot */
				new Color(109,7,26), /* Bordeaux */
				new Color(155,150,10),
				new Color(75,0,130), 
				new Color(150,85,120),
				Color.GREEN
		};
		Color colorSent ;
		if ( colorIndex < -1 || colorIndex > colors.length-1 ) {
			System.out.println("Unexpected colorIndex : "+colorIndex+"\nYou should add some colors in BaseballWorldView.obtainColor");
			colorSent = Color.MAGENTA;
		} else {
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

		displayRatio = Math.min(((double) getWidth()) / virtualSize, ((double) getHeight()) / virtualSize);


		g2.translate(Math.abs(getWidth() - displayRatio * virtualSize)/2., Math.abs(getHeight() - displayRatio * virtualSize)/2.);
		g2.scale(displayRatio, displayRatio);

		/* drawn the field */
		g2.setColor(new Color(58,157,35)); // lawn
		g2.fill(new Rectangle2D.Double(0., 0., virtualSize, virtualSize));

		int radius = 120 ;
		
		// draw the play area
		g.setColor(Color.BLACK);
		g.drawOval((int)virtualSize/2 - 150, (int)virtualSize/2-150, 300, 300);
		g.setColor(new Color(174,74,52));
		g.fillOval((int)virtualSize/2 - 150, (int)virtualSize/2-150, 300, 300);

		
		
		BaseballWorld myWorld = (BaseballWorld) this.world ;
		int amountOfBases = myWorld.getBasesAmount(); // amount of bases
		double theta = 2*Math.PI / amountOfBases; // angle between center of symmetry of two bases
		int amountOfPlayers = myWorld.getPositionsAmount(); // amount of players on a base
		int L = Math.max(3*(20-amountOfBases),10); // adapting the size of the base to the total amount of bases
		radius+=amountOfBases-5; // adapting the position of the base to the total amount of bases

		// Draw the bases and the players on each base
		playersCoordinate = new Vector<Integer[]>();
		for ( int i=0 ; i < myWorld.getBasesAmount();i++)
			drawBase(g2, L, radius, theta*i, (int) virtualSize/2, (int) virtualSize/2 , i, amountOfPlayers);

		// Draw the last move made on the field if it exists
		if ( myWorld.getLastMove() != null)
			drawLastMove(g2, myWorld.getLastMove(), radius-L/amountOfPlayers, theta, virtualSize/2, virtualSize/2, L, amountOfPlayers);
		
		// Display the amount of moves so far
		g2.setColor(Color.black);
		g2.drawString(""+((BaseballWorld) world).getMoveCount()+" moves", 0,15);
	}

	private Map<Color,Image> buggleCache = new HashMap<Color, Image>();
	private Image getBuggleImage(Color c) {
		if (buggleCache.containsKey(c))
			return buggleCache.get(c);
		
		BufferedImage res = new BufferedImage(130, 130, BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g = res.createGraphics();
		g.setColor(new Color(1, 1, 1, 0));
		g.fillRect(0, 0, 130, 130);		
		
		int[][] SPRITE = {
				{ 0,0,0,0,0,0,0,0,0,0,0,0,0 },
				{ 0,0,0,0,0,0,0,0,0,0,0,0,0 },
				{ 0,0,0,1,0,0,0,0,0,1,0,0,0 },
				{ 0,0,0,0,1,0,0,0,1,0,0,0,0 },
				{ 0,0,0,1,1,1,1,1,1,1,0,0,0 },
				{ 0,0,1,1,0,1,1,1,0,1,1,0,0 },
				{ 0,1,1,1,1,1,1,1,1,1,1,1,0 },
				{ 0,1,0,1,1,1,1,1,1,1,0,1,0 },
				{ 0,1,0,1,0,0,0,0,0,1,0,1,0 },
				{ 0,0,0,0,1,1,0,1,1,0,0,0,0 },
				{ 0,0,0,0,0,0,0,0,0,0,0,0,0 },
				{ 0,0,0,0,0,0,0,0,0,0,0,0,0 },
				{ 0,0,0,0,0,0,0,0,0,0,0,0,0 },
			};
		g.setColor(c);
		for (int dy=0; dy<13; dy++) 
			for (int dx=0; dx<13; dx++) 
				if (SPRITE[dy][dx] == 1) 
					g.fill(new Rectangle2D.Double(dx*10, dy*10, 10,10));

		buggleCache.put(c, res);
		return res;
	}

}
