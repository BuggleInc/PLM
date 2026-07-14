package lessons.turmites.turmitecreator;

import lessons.turmites.universe.TurmiteWorld;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;

public class TurmiteCreator extends ExerciseTemplated {

  public TurmiteCreator(Lesson lesson)
  {
    super(lesson);
    tabName = "Turmite";

    setup(new TurmiteWorld("blah", 1000, null, 100, 100, 50, 50));
  }
}
