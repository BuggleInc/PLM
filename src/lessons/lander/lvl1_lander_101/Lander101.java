package lessons.lander.lvl1_lander_101;

import static lessons.lander.universe.Configurations.SIMPLE_TERRAIN_TRIVIAL_CONFIG;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;

public class Lander101 extends ExerciseTemplated {
  public Lander101(Lesson lesson)
  {
    super(lesson, null);
    tabName = "Lander";
    setup(SIMPLE_TERRAIN_TRIVIAL_CONFIG);
  }
}
