package lessons.lander.universe;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Random;
import lessons.lander.universe.LanderWorld.Point;
import plm.core.ui.WorldView;

public class LanderWorldView extends WorldView {

  private static final long serialVersionUID = 1L;

  private static final List<List<Point>> LANDER_SHAPE =
      List.of(List.of(new Point(-2, 3), new Point(-3, 4), new Point(-3, 7), new Point(-2, 8), new Point(2, 8), new Point(3, 7), new Point(3, 4),
                      new Point(2, 3), new Point(-2, 3)),
              List.of(new Point(-6, 0), new Point(-4, 0)), List.of(new Point(6, 0), new Point(4, 0)),
              List.of(new Point(-5, 0), new Point(-4, 2.8), new Point(-2, 3)), List.of(new Point(5, 0), new Point(4, 2.8), new Point(2, 3)));

  private static final List<Point> EXPLOSION_SHAPE =
      List.of(new Point(-4, 3), new Point(-1, 2), new Point(0, 4), new Point(1, 2), new Point(4, 2), new Point(2, 0), new Point(3, -2), new Point(0, -1),
              new Point(-3, -3), new Point(-2, 0), new Point(-4, 3));

  private static final Random RANDOM = new Random();

  private boolean mouseIn = false;
  private Point mousePos  = new Point(0, 0);

  public LanderWorldView(LanderWorld world)
  {
    super(world);

    addMouseListener(new MouseAdapter() {
      @Override public void mouseEntered(MouseEvent e)
      {
        mouseIn = true;
      }
      @Override public void mouseExited(MouseEvent e)
      {
        mouseIn = false;
        repaint();
      }
    });

    addMouseMotionListener(new MouseMotionAdapter() {
      @Override public void mouseMoved(MouseEvent e)
      {
        mousePos = new Point(e.getX(), e.getY());
        repaint();
      }
    });
  }

  private LanderWorld realWorld() { return (LanderWorld)world; }

  @Override protected void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    new Painter((Graphics2D)g).paint();
  }

  private class Painter {
    private final Graphics2D g2;

    Painter(Graphics2D g2) { this.g2 = g2; }

    void paint()
    {
      AffineTransform initialTransform = g2.getTransform();
      setupRendering();
      clearWidgetBackground();
      setupGlobalTransform();
      clearWorldBackground();
      drawGround();
      drawStats(initialTransform);
      switch (realWorld().state) {
        case FLYING -> drawLander(true);
        case LANDED -> drawLander(false);
        case OUT -> drawQuestionMarks();
        case CRASHED -> drawExplosion();
      }
    }

    void setupRendering() { g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); }

    void clearWidgetBackground()
    {
      g2.setColor(Color.gray);
      g2.fill(new Rectangle2D.Double(0, 0, getWidth(), getHeight()));
    }

    void setupGlobalTransform()
    {
      double scale      = Math.min(getWidth() / (double)realWorld().width, getHeight() / (double)realWorld().height);
      double translateX = (getWidth() - realWorld().width * scale) / 2;
      double translateY = (getHeight() + realWorld().height * scale) / 2;
      g2.translate(translateX, translateY);
      g2.scale(scale, -scale);
    }

    void clearWorldBackground()
    {
      g2.setColor(Color.black);
      g2.fill(new Rectangle2D.Double(0, 0, realWorld().width, realWorld().height));
    }

    /** Sets the line width to 1px no matter the transform, and the color to white. */
    void resetPen()
    {
      AffineTransform transform = g2.getTransform();
      double scale              = Math.abs(Math.min(transform.getScaleX(), transform.getScaleY()));
      g2.setStroke(new BasicStroke((float)(1 / scale)));
      g2.setColor(Color.white);
    }

    void drawPath(List<Point> path, boolean fill)
    {
      if (path.isEmpty()) {
        return;
      }
      GeneralPath polyLine = new GeneralPath(Path2D.WIND_EVEN_ODD, path.size());
      Point head           = path.get(0);
      polyLine.moveTo(head.x(), head.y());
      for (Point point : path.subList(1, path.size())) {
        polyLine.lineTo(point.x(), point.y());
      }
      if (fill) {
        g2.fill(polyLine);
      } else {
        g2.draw(polyLine);
      }
    }

    void drawText(String text, double x, double y)
    {
      AffineTransform oldTransform = g2.getTransform();
      g2.translate(x, y);
      g2.scale(1, -1);
      g2.drawString(text, 0, 0);
      g2.setTransform(oldTransform);
    }

    void drawGround()
    {
      resetPen();
      drawPath(realWorld().ground, false);
    }

    double randomScaleFactor() { return 1 + RANDOM.nextDouble() * 0.2; }

    void drawLander(boolean drawFlame)
    {
      AffineTransform oldTransform = g2.getTransform();
      g2.translate(realWorld().position.x(), realWorld().position.y());
      g2.scale(6, 6); // the lander shape is small
      resetPen();
      g2.rotate(realWorld().angleRadian() - Math.PI / 2);
      for (List<Point> path : LANDER_SHAPE) {
        drawPath(path, false);
      }
      int thrust = realWorld().thrust;
      if (drawFlame && thrust > 0) {
        double controlX = (0.4 + thrust * 0.1) * randomScaleFactor();
        double endY     = (-2 * thrust) * randomScaleFactor();
        double controlY = (endY + 3) / 2;
        g2.draw(new QuadCurve2D.Double(-0.25, 3, -controlX, controlY, 0, endY));
        g2.draw(new QuadCurve2D.Double(0.25, 3, controlX, controlY, 0, endY));
      }
      g2.setTransform(oldTransform);
    }

    void drawExplosion()
    {
      AffineTransform oldTransform = g2.getTransform();
      g2.translate(realWorld().position.x(), realWorld().position.y());
      g2.scale(15, 15); // the explosion shape is small
      resetPen();
      drawPath(EXPLOSION_SHAPE, true);
      g2.setTransform(oldTransform);
    }

    void drawQuestionMarks()
    {
      double x     = realWorld().position.x();
      double y     = realWorld().position.y();
      double textX = x >= realWorld().width ? realWorld().width - 100 : (x <= 0 ? 5 : x);
      double textY = y >= realWorld().height ? realWorld().height - 40 : (y <= 0 ? 5 : y - 30);
      g2.setColor(Color.WHITE);
      g2.setFont(new Font(Font.MONOSPACED, Font.BOLD, 40));
      drawText("???", textX, textY);
    }

    void drawStats(AffineTransform initialTransform)
    {
      g2.setColor(Color.LIGHT_GRAY);
      g2.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 30));
      drawText(String.format("x: %.2f", realWorld().position.x()), 5, realWorld().height - 30);
      drawText(String.format("y: %.2f", realWorld().position.y()), 5, realWorld().height - 2 * 30);
      drawText(String.format("speed x: %.2f", realWorld().speed.x()), 5, realWorld().height - 3 * 30);
      drawText(String.format("speed y: %.2f", realWorld().speed.y()), 5, realWorld().height - 4 * 30);
      drawText(String.format("angle: %.2f\u00b0", realWorld().angle), 5, realWorld().height - 5 * 30);
      drawText(String.format("thrust: %d", realWorld().thrust), 5, realWorld().height - 6 * 30);
      drawText(String.format("fuel: %d", realWorld().fuel), 5, realWorld().height - 7 * 30);

      if (mouseIn) {
        AffineTransform deltaTransform = new AffineTransform(g2.getTransform());
        try {
          deltaTransform.invert();
        } catch (java.awt.geom.NoninvertibleTransformException ex) {
          return;
        }
        deltaTransform.concatenate(initialTransform);
        Point2D coord = deltaTransform.transform(new Point2D.Double(mousePos.x(), mousePos.y()), null);
        drawText(String.format("x: %d", Math.round(coord.getX())), realWorld().width - 130, realWorld().height - 30);
        drawText(String.format("y: %d", Math.round(coord.getY())), realWorld().width - 130, realWorld().height - 2 * 30);
      }
    }
  }
}
