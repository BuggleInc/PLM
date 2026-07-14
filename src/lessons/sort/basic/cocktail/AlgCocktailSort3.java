package lessons.sort.basic.cocktail;

import java.util.Arrays;
import lessons.sort.basic.bubble.AlgBubbleSort1Entity;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.World;
import plm.universe.sort.SortingWorld;

public class AlgCocktailSort3 extends ExerciseTemplated {

  public AlgCocktailSort3(Lesson lesson)
  {
    super(lesson);

    World[] myWorlds = new SortingWorld[2];
    myWorlds[0]      = new SortingWorld("Functional test", 10);
    myWorlds[1]      = new SortingWorld("Performance test (150 elms)", 150);

    myWorlds = Arrays.stream(myWorlds).map(s -> s.replaceEntities(AlgCocktailSort3Entity::new)).toList().toArray(new World[0]);
    setup(myWorlds);
  }
}
