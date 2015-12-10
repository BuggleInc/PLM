package lessons.lander.lvl2_locate_landing_zone

import plm.core.model.lesson.ExerciseTemplated
import plm.core.model.lesson.Lesson
import lessons.lander.universe.DelegatingLanderWorld
import lessons.lander.universe.Point
import lessons.lander.universe.LanderEntity
import lessons.lander.universe.Configurations._
import Math.PI
import scala.collection.JavaConversions._
import lessons.lander.universe.LanderWorld
import plm.universe.World
import plm.core.model.Game

class LocateLandingZone(game: Game, lesson: Lesson) extends ExerciseTemplated(game, lesson, null) {
  tabName = "Lander"
  setup(Array(
      makeWorld(game, "CHALLENGING_TERRAIN_SIMPLE_CONFIG"),
      makeWorld(game, "SIMPLE_TERRAIN_TRIVIAL_CONFIG")))
}
