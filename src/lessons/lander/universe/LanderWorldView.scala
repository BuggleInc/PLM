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

class LanderWorldView(world: LanderWorld) extends WorldView(world.parent) {

  override def paintComponent(g: Graphics) = {
    super.paintComponent(g);

    val g2 = g.asInstanceOf[Graphics2D];
    clearWidgetBackground(g2)
    setupGlobalTransformation(g2)
    clearWorldBackground(g2)
    drawGround(g2)
  }

  def clearWidgetBackground(g2: Graphics2D) = {
    g2.setColor(Color.gray)
    g2.fill(new Rectangle2D.Double(0, 0, getWidth, getHeight))
  }

  def setupGlobalTransformation(g2: Graphics2D) = {
    val scale = Math.min(getWidth.toDouble / world.width, getHeight.toDouble / world.height)
    g2.translate((getWidth - world.width * scale) / 2,  (getHeight + world.height * scale) / 2)
    g2.scale(scale, -scale)
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setStroke(new BasicStroke((1 / scale).toFloat))
  }

  def clearWorldBackground(g2: Graphics2D) = {
    g2.setColor(Color.black);
    g2.fill(new Rectangle2D.Double(0, 0, world.width, world.height));
  }

  def drawGround(g2: Graphics2D) = {
    if (world.ground.length > 1) {
      for ((p1, p2) <- world.ground zip world.ground.tail) {
        g2.setColor(Color.white);
        g2.draw(new Line2D.Double(p1.x, p1.y, p2.x, p2.y))
      }
    }
  }
}