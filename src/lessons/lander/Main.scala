package lessons.lander

import plm.core.model.lesson.Lesson
import lessons.lander.lvl1_lander_101.Level1Lander
import lessons.lander.lvl2_locate_landing_zone.Level2Lander
import lessons.lander.lvl3_fly_the_lander.Level3Lander

class Main extends Lesson {
  override def loadExercises = {
    addExercise(new Level1Lander(this));
    addExercise(new Level2Lander(this));
    addExercise(new Level3Lander(this));
  }
}
