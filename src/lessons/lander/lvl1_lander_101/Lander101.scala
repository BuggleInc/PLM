package lessons.lander.lvl1_lander_101

import plm.core.model.lesson.ExerciseTemplated
import plm.core.model.lesson.Lesson
import plm.universe.lander.Point
import plm.universe.lander.LanderEntity
import plm.universe.lander.Configurations._
import Math.PI
import scala.collection.JavaConversions._
import plm.universe.lander.LanderWorld
import plm.universe.World
import plm.core.model.Game

class Lander101(game: Game, lesson: Lesson) extends ExerciseTemplated(game, lesson, null) {
  tabName = "Lander"
  setup(makeWorld(game, "SIMPLE_TERRAIN_TRIVIAL_CONFIG"))
}
