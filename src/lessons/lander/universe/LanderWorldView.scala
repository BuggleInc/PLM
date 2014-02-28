package lessons.lander.universe

import plm.core.ui.WorldView
import plm.universe.World
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.Color
import java.awt.geom.Rectangle2D
import java.awt.geom.Line2D
import java.awt.BasicStroke
import java.awt.geom.AffineTransform
import java.awt.geom.GeneralPath
import java.awt.geom.Path2D
import Math.PI
import java.awt.geom.QuadCurve2D

object LanderWorldView {
  private val LANDER_SHAPE = List(
      List(Point(-2, 3), Point(-3, 4), Point(-3, 7), Point(-2, 8), Point(2, 8), Point(3, 7),
          Point(3, 4), Point(2, 3), Point(-2, 3)),
      List(Point(-6, 0), Point(-4, 0)),
      List(Point(6, 0), Point(4, 0)),
      List(Point(-5, 0), Point(-4, 2.8), Point(-2, 3)),
      List(Point(5, 0), Point(4, 2.8), Point(2, 3)))
}

class LanderWorldView(world: LanderWorld) extends WorldView(world.parent) {

  override def paintComponent(g: Graphics) = {
    super.paintComponent(g);
    val g2 = g.asInstanceOf[Graphics2D];
    new Painter(g2).paint()
  }

  private class Painter(g2: Graphics2D) {
    def paint() {
      setupRendering()
      clearWidgetBackground()
      setupGlobalTransform()
      clearWorldBackground()
      drawGround()
      drawLander()
    }

    def setupRendering() {
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
    }

    def clearWidgetBackground() = {
      g2.setColor(Color.gray)
      g2.fill(new Rectangle2D.Double(0, 0, getWidth, getHeight))
    }

    def setupGlobalTransform() = {
      val scale = (getWidth.toDouble / world.width) min (getHeight.toDouble / world.height)
      g2.translate((getWidth - world.width * scale) / 2,  (getHeight + world.height * scale) / 2)
      g2.scale(scale, -scale)
    }

    def clearWorldBackground() = {
      g2.setColor(Color.black);
      g2.fill(new Rectangle2D.Double(0, 0, world.width, world.height));
    }

    /** Sets the line width to 1px no matter the transform, and the color to white. */
    def resetPen() {
      val transform = g2.getTransform();
      val scale = (transform.getScaleX() min transform.getScaleY()).abs;
      g2.setStroke(new BasicStroke((1 / scale).toFloat))
      g2.setColor(Color.white)
    }

    def drawPath(path: List[Point]) {
      if (!path.isEmpty) {
        val polyLine = new GeneralPath(Path2D.WIND_EVEN_ODD, path.length)
        polyLine.moveTo(path.head.x, path.head.y)
        for (point <- path.tail) {
          polyLine.lineTo(point.x, point.y)
        }
        g2.draw(polyLine)
      }
    }

    def drawGround() = {
      resetPen()
      drawPath(world.ground)
    }

    def drawLander() = {
      val oldTransform = g2.getTransform()
      g2.translate(world.lander.position.x, world.lander.position.y)
      g2.scale(6, 6)  // the lander shape is small
      resetPen()
      g2.rotate(world.lander.angle - PI/2)
      LanderWorldView.LANDER_SHAPE.foreach(drawPath)
      val thrust = world.lander.thrust
      if (thrust > 0) {
        val controlX = 0.4 + thrust * 0.1
        val endY = -2 * thrust
        val controlY = (3 - endY) / 2
        g2.draw(new QuadCurve2D.Double(-0.1, 3, -controlX, controlY, 0, endY))
        g2.draw(new QuadCurve2D.Double(0.1, 3, controlX, controlY, 0, endY))
      }
      g2.transform(oldTransform)
    }
  }
}
