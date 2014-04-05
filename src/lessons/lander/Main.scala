package lessons.lander

import plm.core.model.lesson.Lesson
import lessons.lander.level1.Level1Lander
import lessons.lander.level2.Level2Lander
import lessons.lander.level3.Level3Lander

class Main extends Lesson {
  override def loadExercises = {
    addExercise(new Level1Lander(this));
    addExercise(new Level2Lander(this));
    addExercise(new Level3Lander(this));
  }
}
