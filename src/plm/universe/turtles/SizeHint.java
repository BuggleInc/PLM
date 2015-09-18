package plm.universe.turtles;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public class SizeHint implements ImageObserver {
	public double x1, y1,  x2, y2;
	static Color color = new Color(255,160,0);
	public String text;
	public SizeHint(double x1, double y1, double x2, double y2, String msg) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		text = msg;
	}
	public void draw(Graphics2D g2) {
		g2.setColor(color);
		
		g2.draw(new Line2D.Double(x1,y1,x2,y2));
		
		int cx = (int) ((x1+x2)/2.);
		int cy = (int) ((y1+y2)/2.);
		
		double hyp = Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
		double theta = Math.acos((y2-y1)/hyp) - Math.PI/2;
		
		drawString(g2, text, cx, cy, theta);
		
	}
    private BufferedImage createStringImage(Graphics g, String msg) {
        int w = g.getFontMetrics().stringWidth(msg) + 5;
        int h = g.getFontMetrics().getHeight();

        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D imageGraphics = image.createGraphics();
        imageGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        imageGraphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        imageGraphics.setColor(color);
        imageGraphics.setFont(g.getFont());
        imageGraphics.drawString(msg, 0, h - g.getFontMetrics().getDescent());
        imageGraphics.dispose();

        return image;
    }
    private void drawString(Graphics2D g2, String s, double tx, double ty, double theta) {
    	BufferedImage img = createStringImage(g2, s); 
        AffineTransform aff = AffineTransform.getTranslateInstance(-((double)img.getWidth() )/2, -((double)img.getHeight() )/2);
        aff.rotate(theta, tx+((double)img.getWidth() )/2, ty+((double)img.getHeight() )/2);
        aff.translate(tx, ty);

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.drawImage(img, aff, this);
    }
	@Override
	public boolean imageUpdate(Image img, int infoflags, int x, int y,
			int width, int height) {
		// TODO Auto-generated method stub
		return false;
	}
}
