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
import scala.util.Random
import java.awt.Font
import java.awt.event.MouseListener
import java.awt.event.MouseMotionListener
import java.awt.event.MouseMotionAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseAdapter
import java.awt.geom.Point2D

object LanderWorldView {
  private val LANDER_SHAPE = List(
      List(Point(-2, 3), Point(-3, 4), Point(-3, 7), Point(-2, 8), Point(2, 8), Point(3, 7),
          Point(3, 4), Point(2, 3), Point(-2, 3)),
      List(Point(-6, 0), Point(-4, 0)),
      List(Point(6, 0), Point(4, 0)),
      List(Point(-5, 0), Point(-4, 2.8), Point(-2, 3)),
      List(Point(5, 0), Point(4, 2.8), Point(2, 3)))

  private val EXPLOSION_SHAPE = List(
      Point(-4, 3), Point(-1, 2), Point(0, 4), Point(1, 2), Point(4, 2), Point(2, 0),
      Point(3, -2), Point(0, -1), Point(-3, -3), Point(-2, 0), Point(-4, 3))
}

class LanderWorldView(world: LanderWorld) extends WorldView(world.parent) {

  import LanderWorld.State._

  private var mouseIn : Boolean = false
  private var mousePos : Point = Point(0, 0)

  addMouseListener(new MouseAdapter() {
    override def mouseEntered(e: MouseEvent) {
      mouseIn = true
    }
    override def mouseExited(e: MouseEvent) {
      mouseIn = false
      repaint()
    }
  })

  addMouseMotionListener(new MouseMotionAdapter() {
    override def mouseMoved(event: MouseEvent) = {
      mousePos = Point(event.getX, event.getY)
      repaint()
    }
  })

  override def paintComponent(g: Graphics) = {
    super.paintComponent(g);
    val g2 = g.asInstanceOf[Graphics2D];
    new Painter(g2).paint()
  }

  private class Painter(g2: Graphics2D) {
    def paint() {
      val initialTransform = g2.getTransform()
      setupRendering()
      clearWidgetBackground()
      setupGlobalTransform()
      clearWorldBackground()
      drawGround()
      drawStats(initialTransform)
      world.state match {
        case FLYING => drawLander(drawFlame = true)
        case LANDED => drawLander(drawFlame = false)
        case OUT => drawQuestionMarks()
        case CRASHED => drawExplosion()
      }
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

    def drawPath(path: List[Point], fill: Boolean) {
      if (!path.isEmpty) {
        val polyLine = new GeneralPath(Path2D.WIND_EVEN_ODD, path.length)
        polyLine.moveTo(path.head.x, path.head.y)
        for (point <- path.tail) {
          polyLine.lineTo(point.x, point.y)
        }
        if (fill) {
          g2.fill(polyLine)
        } else {
          g2.draw(polyLine)
        }
      }
    }

    def drawText(text: String, x: Double, y: Double) {
      val oldTransform = g2.getTransform()
      g2.translate(x, y)
      g2.scale(1, -1)
      g2.drawString(text, 0, 0)
      g2.setTransform(oldTransform)
    }

    def drawGround() = {
      resetPen()
      drawPath(world.ground, fill = false)
    }

    def randomScaleFactor() = 1 + Random.nextDouble() * 0.2

    def drawLander(drawFlame: Boolean) = {
      val oldTransform = g2.getTransform()
      g2.translate(world.position.x, world.position.y)
      g2.scale(6, 6)  // the lander shape is small
      resetPen()
      g2.rotate(world.angleRadian - PI/2)
      LanderWorldView.LANDER_SHAPE.foreach(drawPath(_, fill = false))
      val thrust = world.thrust
      if (drawFlame && thrust > 0) {
        val controlX = (0.4 + thrust * 0.1) * randomScaleFactor()
        val endY = (-2 * thrust) * randomScaleFactor()
        val controlY = (endY + 3) / 2
        g2.draw(new QuadCurve2D.Double(-0.25, 3, -controlX, controlY, 0, endY))
        g2.draw(new QuadCurve2D.Double(0.25, 3, controlX, controlY, 0, endY))
      }
      g2.setTransform(oldTransform)
    }

    def drawExplosion() = {
      val oldTransform = g2.getTransform()
      g2.translate(world.position.x, world.position.y)
      g2.scale(15, 15)  // the explosion shape is small
      resetPen()
      drawPath(LanderWorldView.EXPLOSION_SHAPE, fill = true)
      g2.setTransform(oldTransform)
    }

    def drawQuestionMarks() = {
      val x = world.position.x
      val y = world.position.y
      val textX = if (x >= world.width) world.width - 100 else if (x <= 0) 5 else x
      val textY = if (y >= world.height) world.height - 40 else if (y <= 0) 5 else y - 30
      g2.setColor(Color.WHITE)
      g2.setFont(new Font(Font.MONOSPACED, Font.BOLD, 40))
      drawText("???", textX, textY)
    }

    def drawStats(initialTransform: AffineTransform) {
      g2.setColor(Color.LIGHT_GRAY)
      g2.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 30))
      drawText(f"x: ${world.position.x}%.2f", 5, world.height - 30)
      drawText(f"y: ${world.position.y}%.2f", 5, world.height - 2 * 30)
      drawText(f"speed x: ${world.speed.y}%.2f", 5, world.height - 3 * 30)
      drawText(f"speed y: ${world.speed.y}%.2f", 5, world.height - 4 * 30)
      drawText(f"angle: ${world.angle}%.2f°", 5, world.height - 5 * 30)
      drawText(f"thrust: ${world.thrust}", 5, world.height - 6 * 30)

      if (mouseIn) {
        val deltaTransform = new AffineTransform(g2.getTransform())
        deltaTransform.invert()
        deltaTransform.concatenate(initialTransform)
        val coord = deltaTransform.transform(new Point2D.Double(mousePos.x, mousePos.y), null)
        drawText(f"x: ${coord.getX.round}", world.width - 130, world.height - 30)
        drawText(f"y: ${coord.getY.round}", world.width - 130, world.height - 2 * 30)
      }
    }
  }
}
