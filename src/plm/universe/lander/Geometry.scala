package plm.universe.lander

import Math.PI
import Math.cos
import Math.sin

object NumUtil {
  def clamp(min: Double, max: Double, value: Double): Double = {
    if (value < min) min else if (value > max) max else value
  }
  def clamp(min: Int, max: Int, value: Int): Int = {
    if (value < min) min else if (value > max) max else value
  }

  def radianToVector(angle: Double) = Point(cos(angle), sin(angle))
  
  def gameAngleToRadian(angle: Double) = (angle + 90) * PI / 180
}

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

case class Segment(start: Point, end: Point)

case class Ray(origin: Point, direction: Point) {
  def intersects(s: Segment): Boolean = {
    val v = s.end - s.start
    val cross = direction.cross(v);
    if (cross == 0) {
      false
    } else {
      val f1 = (s.start - origin).cross(v) / cross
      val f2 = (s.start - origin).cross(direction) / cross
      f1 >= 0 && f2 >= 0 && f2 <= 1
    }
  }
}
