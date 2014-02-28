package lessons.lander.universe

import plm.universe.World
import plm.core.model.ProgrammingLanguage
import javax.script.ScriptEngine
import javax.swing.ImageIcon
import plm.core.ui.ResourcesCache

case class Point(x: Double, y: Double) {
  def +(p: Point): Point = new Point(x + p.x, y + p.y)
  def -(p: Point): Point = new Point(x - p.x, y - p.y)
  def *(l: Double): Point = new Point(x * l, y * l)
  def /(l: Double): Point = new Point(x / l, y / l)
  def unary_- = this * -1

  def length: Double = Math.sqrt(x * x + y * y)
  def normed = this / length
  def dot(p: Point) = x * p.x + y * p.y
  def cross(p: Point) = x * p.y - y * p.x
}

case class Segment(p1: Point, p2: Point) {
  def intesection(s: Segment): Option[Point] = {
    val v1 = p2 - p1
    val v2 = s.p2 - s.p1
    val v1v2 = v1.cross(v2);
    if (v1v2 == 0) {
      None
    } else {
      val f1 = (s.p1 - p1).cross(v2) / v1v2
      val f2 = (s.p1 - p1).cross(v1) / v1v2
      if (f1 >= 0 && f1 <= 1 && f2 >= 0 && f2 <= 1) {
        Some(p1 + v1 * f1)
      } else {
        None
      }
    }
  }
}

class LanderWorld(val parent: DelegatingLanderWorld) {
  // this needs to be a var because of reset, which is required
  var width: Int = 0
  var height: Int = 0
  var ground: List[Point] = List()

  parent.addEntity(new LanderEntity())

  def getIcon = ResourcesCache.getIcon("img/world_lander.png");

  def setupBindings(lang: ProgrammingLanguage, engine: ScriptEngine): Unit = ()

  def diffTo(world: World): String = null

  def reset(initialWorld: LanderWorld): Unit = {
    width = initialWorld.width
    height = initialWorld.height
    ground = initialWorld.ground
  }

  def getView() = new LanderWorldView(LanderWorld.this)

  override def toString() = "scala lander world"
}