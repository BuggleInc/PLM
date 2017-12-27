package plm.universe.bugglequest;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;


import org.jfree.graphics2d.svg.SVGGraphics2D;
import plm.universe.Entity;
import plm.universe.World;


import plm.universe.WorldView;

public class BuggleWorldView  extends WorldView {

    //static BuggleWorld buggleWorld = (BuggleWorld) world;
    public BuggleWorldView(BuggleWorld world2) {
        super(world2);
    }

    private static Color DARK_CELL_COLOR = new Color(0.93f,0.93f,0.93f);
    private static Color LIGHT_CELL_COLOR = new Color(0.95f,0.95f,0.95f);
    private static Color GRID_COLOR = new Color(0.8f,0.8f,0.8f);
    private static Color WALL_COLOR = new Color(0.0f,0.0f,0.5f);


    protected static double getCellWidth(BuggleWorld buggleWorld, int height, int width) {
        return (double) Math.min(height / buggleWorld.getHeight() , width /  buggleWorld.getWidth());

    }

    protected static double getPadX(BuggleWorld buggleWorld, int height, int width) {
        return (width - getCellWidth(buggleWorld,width,height) * buggleWorld.getWidth()) / 2;
    }
    protected static double getPadY(BuggleWorld buggleWorld, int height, int width) {
        return (height - getCellWidth(buggleWorld,height, width) * buggleWorld.getHeight()) / 2;
    }

    public static void paintComponent(Graphics g, BuggleWorld buggleWorld,int width, int height) {
        //super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawBackground(g2, buggleWorld,width, height);

        for (Entity ent: buggleWorld.getEntities()) {
            AbstractBuggle b = (AbstractBuggle)ent;
            drawBuggle(g2, b,buggleWorld,width,height);
        }

        drawWalls(g2,buggleWorld,width,height);
    }

    // return the color of the cell located at position (x,y)
    private static Color getCellColor(int x, int y, BuggleWorld buggleWorld) {
        BuggleWorldCell cell = buggleWorld.getCell(x, y);

        if (BuggleWorldCell.DEFAULT_COLOR.equals(cell.getColor())) {
            if (buggleWorld.getVisibleGrid()) {
                if ((x + y) % 2 == 0)
                    return DARK_CELL_COLOR;
                else
                    return LIGHT_CELL_COLOR;
            } else {
                return Color.WHITE;
            }
        } else {
            return cell.getColor();
        }
    }


    private static void drawBackground(Graphics2D g, BuggleWorld buggleWorld, int width, int height) {
        double cellW = getCellWidth(buggleWorld, width, height);
        double padx = getPadX(buggleWorld, width, height);
        double pady = getPadY(buggleWorld, width, height);

        if (buggleWorld.getVisibleGrid() == false) {
            g.setColor(Color.white);
            g.fill(new Rectangle2D.Double(padx,pady ,(buggleWorld.getWidth()-1)*cellW,(buggleWorld.getHeight()-1)*cellW));
        }
        for (int x=0; x<buggleWorld.getWidth(); x++) {
            for (int y=0; y<buggleWorld.getHeight(); y++) {

                g.setColor(getCellColor(x, y,buggleWorld));

                BuggleWorldCell cell = buggleWorld.getCell(x, y);

                g.fill(new Rectangle2D.Double(padx+x*cellW, pady+y*cellW, cellW, cellW));

                if (cell.hasBaggle())
                    drawBaggle(g, cell,buggleWorld,width,height);
                if (cell.hasContent())
                    drawMessage(g, cell, cell.getContent(),buggleWorld,width,height);
            }
        }

        if (buggleWorld.getVisibleGrid()) {
            g.setColor(GRID_COLOR);
            for (int x=0; x<=buggleWorld.getWidth(); x++) {
                g.draw(new Line2D.Double(padx+x*cellW, pady, padx+x*cellW, pady+buggleWorld.getWidth()*cellW));
            }
            for (int y=0; y<=buggleWorld.getHeight(); y++) {
                g.draw(new Line2D.Double(padx+0, pady+y*cellW, padx+buggleWorld.getHeight()*cellW, pady+y*cellW));
            }
        }
    }

    private static void drawWalls(Graphics2D g,BuggleWorld buggleWorld,int width,int height) {
        double cellW = getCellWidth(buggleWorld,width,height);
        double padx = getPadX(buggleWorld,width,height);
        double pady = getPadY(buggleWorld,width,height);



        g.setColor(WALL_COLOR);

        for (int x = 0; x < buggleWorld.getWidth(); x++) {
            for (int y = 0; y < buggleWorld.getHeight(); y++) {
                BuggleWorldCell cell = (BuggleWorldCell) buggleWorld.getCell(x, y);

                if (cell.hasTopWall()) {
                    g.draw(new Line2D.Double(padx+x*cellW, pady+y*cellW-1, padx+(x+1)*cellW, pady+y*cellW-1));
                    g.draw(new Line2D.Double(padx+x*cellW, pady+y*cellW, padx+(x+1)*cellW, pady+y*cellW));
                    g.draw(new Line2D.Double(padx+x*cellW, pady+y*cellW+1, padx+(x+1)*cellW, pady+y*cellW+1));
                    //System.out.println(padx+x*cellW+","+ pady+y*cellW+1+"," + padx+(x+1)*cellW+","+ pady+y*cellW+1);
                }

                if (cell.hasLeftWall()) {
                    g.draw(new Line2D.Double(padx+x*cellW-1, pady+y*cellW, padx+x*cellW-1, pady+(y+1)*cellW));
                    g.draw(new Line2D.Double(padx+x*cellW, pady+y*cellW, padx+x*cellW, pady+(y+1)*cellW));
                    g.draw(new Line2D.Double(padx+x*cellW+1, pady+y*cellW, padx+x*cellW+1, pady+(y+1)*cellW));
                }
            }
        }

        // frontier walls (since the world is a torus)
        for (int y = 0; y < buggleWorld.getHeight(); y++) {
            if (((BuggleWorldCell) buggleWorld.getCell(0, y)).hasLeftWall()) {
                g.draw(new Line2D.Double(padx+buggleWorld.getWidth()*cellW-1, pady+y*cellW, padx+buggleWorld.getWidth()*cellW-1, pady+(y+1)*cellW));
                g.draw(new Line2D.Double(padx+buggleWorld.getWidth()*cellW, pady+y*cellW, padx+buggleWorld.getWidth()*cellW, pady+(y+1)*cellW));
                g.draw(new Line2D.Double(padx+buggleWorld.getWidth()*cellW+1, pady+y*cellW, padx+buggleWorld.getWidth()*cellW+1, pady+(y+1)*cellW));
            }
        }

        for (int x = 0; x < buggleWorld.getWidth(); x++) {
            if (((BuggleWorldCell) buggleWorld.getCell(x, 0)).hasTopWall()) {
                g.draw(new Line2D.Double(padx+x*cellW, pady+buggleWorld.getHeight()*cellW-1, padx+(x+1)*cellW, pady+buggleWorld.getHeight()*cellW-1));
                g.draw(new Line2D.Double(padx+x*cellW, pady+buggleWorld.getHeight()*cellW, padx+(x+1)*cellW, pady+buggleWorld.getHeight()*cellW));
                g.draw(new Line2D.Double(padx+x*cellW, pady+buggleWorld.getHeight()*cellW+1, padx+(x+1)*cellW, pady+buggleWorld.getHeight()*cellW+1));
            }
        }
    }

    private static void drawBuggle(Graphics2D g, AbstractBuggle b, BuggleWorld buggleWorld,int width,int height) {
        double scaleFactor = 0.6; // to scale the sprite
        double pixW = scaleFactor * getCellWidth(buggleWorld,width,height) / INVADER_SPRITE_SIZE;  // fake pixel width
        double pad = 0.5*(1.0-scaleFactor)*getCellWidth(buggleWorld,width,height); // padding to center sprite in the cell
        double padx = getPadX(buggleWorld,width,height);
        double pady = getPadY(buggleWorld,width,height);

        double ox = b.getX()*getCellWidth(buggleWorld,width,height); // x-offset of the cell
        double oy = b.getY()*getCellWidth(buggleWorld,width,height); // y-offset of the cell

        if (b.isBrushDown()) {
            if (Color.BLACK.equals(b.getBrushColor()))
                g.setColor(Color.WHITE);
            else
                g.setColor(Color.BLACK);
        } else
            g.setColor(b.getBodyColor());

        if (buggleWorld.easter) {
//            try {
//                InputStream is = getClass().getResourceAsStream("/plm/universe/bugglequest/ui/rabbit.png");
//                ImageIcon ic = new ImageIcon(ImageIO.read(is));
//                g.drawImage(ic.getImage(), (int)(padx+ox),(int)(pady+oy), (int)getCellWidth(),(int)getCellWidth(),null);
//            } catch (IOException e) {
//                // Forget it
//                buggleWorld.easter = false;
//                return;
//            }

        } else {
            for (int dy=0; dy<INVADER_SPRITE_SIZE; dy++) {
                for (int dx=0; dx<INVADER_SPRITE_SIZE; dx++) {
                    int direction = b.getDirection().intValue();
                    if (INVADER_SPRITE[direction][dy][dx] == 1) {
                        g.fill(new Rectangle2D.Double(padx+pad+ox+dx*pixW, pady+pad+oy+dy*pixW, pixW, pixW));
                    }
                }
            }
        }
    }

    private static void drawBaggle(Graphics2D g, BuggleWorldCell cell,BuggleWorld buggleWorld,int width,int height) {
        double padx = getPadX(buggleWorld,width,height);
        double pady = getPadY(buggleWorld,width,height);

        double scaleFactor = 0.8; // to scale the sprite

        double d = scaleFactor*getCellWidth(buggleWorld,width,height);
        double pad = 0.5*(1.0-scaleFactor)*getCellWidth(buggleWorld,width,height); // padding to center sprite in the cell

        double scaleFactor2 = 0.5; // to scale the sprite

        double d2 = scaleFactor2*scaleFactor*getCellWidth(buggleWorld,width,height);
        double pad2 = 0.5*(1.0-scaleFactor*scaleFactor2)*getCellWidth(buggleWorld,width,height); // padding to center sprite in the cell

        double ox = cell.getX()*getCellWidth(buggleWorld,width,height); // x-offset of the cell
        double oy = cell.getY()*getCellWidth(buggleWorld,width,height); // y-offset of the cell

//        if (buggleWorld.easter) {
//            try {
//                InputStream is = getClass().getResourceAsStream("/plm/universe/bugglequest/ui/egg.png");
//                ImageIcon ic = new ImageIcon(ImageIO.read(is));
//                g.drawImage(ic.getImage(), (int)(padx+ox),(int)(pady+oy), (int)getCellWidth(),(int)getCellWidth(),null);
//            } catch (IOException e) {
//                // Forget it
//                buggleWorld.easter = false;
//                return;
//            }
//
//        } else {
        g.setColor(BuggleWorldCell.DEFAULT_BAGGLE_COLOR);
        g.fill(new Arc2D.Double(padx+ox+pad, pady+oy+pad, d, d, 0, 360, Arc2D.CHORD));
        g.setColor(getCellColor(cell.getX(), cell.getY(),buggleWorld));
        g.fill(new Arc2D.Double(padx+ox+pad2, pady+oy+pad2, d2, d2, 0, 360, Arc2D.CHORD));

        g.setColor(BuggleWorldCell.DEFAULT_BAGGLE_COLOR.darker().darker());
        g.draw(new Arc2D.Double(padx+ox+pad, pady+oy+pad, d, d, 0, 360, Arc2D.CHORD));
        g.draw(new Arc2D.Double(padx+ox+pad2, pady+oy+pad2, d2, d2, 0, 360, Arc2D.CHORD));
//        }
    }

    private static void drawMessage(Graphics2D g, BuggleWorldCell cell, String msg, BuggleWorld buggleWorld,int width,int height) {
        double padx = getPadX(buggleWorld,width,height);
        double pady = getPadY(buggleWorld,width,height);
        double ox = cell.getX()*getCellWidth(buggleWorld,width,height); // x-offset of the cell
        double oy = (cell.getY()+1)*getCellWidth(buggleWorld,width,height); // y-offset of the cell


        g.setColor(cell.getMsgColor());
        g.drawString(msg, (float) (padx+ox)+1, (float) (pady+oy)-4);
    }

    // old style ;b
    private static int INVADER_SPRITE_SIZE = 11;
    private static int[][][] INVADER_SPRITE = {
            {
                    { 0,0,0,0,0,0,0,0,0,0,0 },
                    { 0,0,1,0,0,0,0,0,1,0,0 },
                    { 0,0,0,1,0,0,0,1,0,0,0 },
                    { 0,0,1,1,1,1,1,1,1,0,0 },
                    { 0,1,1,0,1,1,1,0,1,1,0 },
                    { 1,1,1,1,1,1,1,1,1,1,1 },
                    { 1,0,1,1,1,1,1,1,1,0,1 },
                    { 1,0,1,0,0,0,0,0,1,0,1 },
                    { 0,0,0,1,1,0,1,1,0,0,0 },
                    { 0,0,0,0,0,0,0,0,0,0,0 },
                    { 0,0,0,0,0,0,0,0,0,0,0 },
            },
            {
                    { 0,0,0,1,1,1,0,0,0,0,0 },
                    { 0,0,0,0,0,1,1,0,0,0,0 },
                    { 0,0,0,1,1,1,1,1,0,1,0 },
                    { 0,0,1,0,1,1,0,1,1,0,0 },
                    { 0,0,1,0,1,1,1,1,0,0,0 },
                    { 0,0,0,0,1,1,1,1,0,0,0 },
                    { 0,0,1,0,1,1,1,1,0,0,0 },
                    { 0,0,1,0,1,1,0,1,1,0,0 },
                    { 0,0,0,1,1,1,1,1,0,1,0 },
                    { 0,0,0,0,0,1,1,0,0,0,0 },
                    { 0,0,0,1,1,1,0,0,0,0,0 }
            },
            {
                    { 0,0,0,0,0,0,0,0,0,0,0 },
                    { 0,0,0,0,0,0,0,0,0,0,0 },
                    { 0,0,0,1,1,0,1,1,0,0,0 },
                    { 1,0,1,0,0,0,0,0,1,0,1 },
                    { 1,0,1,1,1,1,1,1,1,0,1 },
                    { 1,1,1,1,1,1,1,1,1,1,1 },
                    { 0,1,1,0,1,1,1,0,1,1,0 },
                    { 0,0,1,1,1,1,1,1,1,0,0 },
                    { 0,0,0,1,0,0,0,1,0,0,0 },
                    { 0,0,1,0,0,0,0,0,1,0,0 },
                    { 0,0,0,0,0,0,0,0,0,0,0 },
            },
            {
                    { 0,0,0,0,0,1,1,1,0,0,0 },
                    { 0,0,0,0,1,1,0,0,0,0,0 },
                    { 0,1,0,1,1,1,1,1,0,0,0 },
                    { 0,0,1,1,0,1,1,0,1,0,0 },
                    { 0,0,0,1,1,1,1,0,1,0,0 },
                    { 0,0,0,1,1,1,1,0,0,0,0 },
                    { 0,0,0,1,1,1,1,0,1,0,0 },
                    { 0,0,1,1,0,1,1,0,1,0,0 },
                    { 0,1,0,1,1,1,1,1,0,0,0 },
                    { 0,0,0,0,1,1,0,0,0,0,0 },
                    { 0,0,0,0,0,1,1,1,0,0,0 }
            }};

    /**
     *
     * @param buggleWorld
     * @param width
     * @param height
     * @return The BuggleWord's SVG under String Format
     */
    public static String draw(BuggleWorld buggleWorld, int width, int height){
        // Ask the test to render into the SVG Graphics2D implementation.

        SVGGraphics2D svgGenerator  = new SVGGraphics2D(400,400);

        paintComponent(svgGenerator, buggleWorld,width,height);

        String str = svgGenerator.getSVGElement();
        return str;

    }
}
