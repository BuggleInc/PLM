package lessons.lander.level2

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

class Level2Lander(lesson: Lesson) extends ExerciseTemplated(lesson, null) {
  setup(Array(
      CHALLENGING_TERRAIN_SIMPLE_CONFIG,
      SIMPLE_TERRAIN_TRIVIAL_CONFIG))
}
