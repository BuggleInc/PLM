package lessons.lander.full

import plm.core.model.lesson.ExerciseTemplated
import plm.core.model.lesson.Lesson
import lessons.lander.universe.DelegatingLanderWorld
import lessons.lander.universe.Point
import lessons.lander.universe.LanderEntity
import lessons.lander.universe.Lander
import Math.PI

class FullLander(lesson: Lesson) extends ExerciseTemplated(lesson, null) {
  val width = 2000
  val height = 1000
  val ground = List(Point(0, 100), Point(125, 414), Point(205, 271),
      Point(348, 597), Point(460, 257), Point(534, 438), Point(637, 160),
      Point(760, 371), Point(854, 200), Point(1468, 200), Point(1585, 440),
      Point(1682, 280), Point(1845, 668), Point(2000, 294))

  val lander = Lander(Point(500, 500), Point(0, 0), PI/3, 2)
  val world = new DelegatingLanderWorld("lander", width, height, ground, lander)

  setup(world)
}
