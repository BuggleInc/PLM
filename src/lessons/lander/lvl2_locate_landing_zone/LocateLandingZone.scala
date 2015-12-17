package lessons.lander.lvl2_locate_landing_zone

import plm.universe.lander._
import plm.universe.lander.Configurations._
import plm.core.model.Game
import plm.core.model.lesson.ExerciseTemplated
import plm.core.model.lesson.Lesson

class LocateLandingZone(game: Game, lesson: Lesson) extends ExerciseTemplated(game, lesson, null) {
  tabName = "Lander"
  setup(Array(
      makeWorld(game, "CHALLENGING_TERRAIN_SIMPLE_CONFIG"),
      makeWorld(game, "SIMPLE_TERRAIN_TRIVIAL_CONFIG")))
}
