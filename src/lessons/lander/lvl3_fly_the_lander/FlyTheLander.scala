package lessons.lander.lvl3_fly_the_lander

import plm.universe.lander._
import plm.universe.lander.Configurations._
import plm.core.model.Game
import plm.core.model.lesson.ExerciseTemplated
import plm.core.model.lesson.Lesson

class FlyTheLander(game: Game, lesson: Lesson) extends ExerciseTemplated(game, lesson, null) {
  tabName = "Lander"
  setup(Array(
      makeWorld(game, "SIMPLE_TERRAIN_CHALLENGING_CONFIG"),
      makeWorld(game, "CHALLENGING_TERRAIN_SIMPLE_CONFIG"),
      makeWorld(game, "SIMPLE_TERRAIN_TRIVIAL_CONFIG") 
  ))
}
