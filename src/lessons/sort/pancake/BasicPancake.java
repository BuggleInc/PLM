package lessons.sort.pancake;

import java.util.Arrays;
import lessons.sort.basic.bubble.AlgBubbleSort1Entity;
import lessons.sort.pancake.universe.PancakeEntity;
import lessons.sort.pancake.universe.PancakeWorld;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.World;

public class BasicPancake extends ExerciseTemplated {

  public BasicPancake(Lesson lesson)
  {
    super(lesson);

    World[] myWorlds = new PancakeWorld[4];
    myWorlds[0]      = new PancakeWorld("5 pancakes", 5, false);
    myWorlds[1]      = new PancakeWorld("10 pancakes", 10, false);
    myWorlds[2]      = new PancakeWorld("15 pancakes", 15, false);
    myWorlds[3]      = new PancakeWorld("30 pancakes", 30, false);
    for (int i = 0; i < 4; i++)
      new PancakeEntity("Pancake Seller", myWorlds[i]);

    myWorlds = Arrays.stream(myWorlds).map(s -> s.replaceEntities(BasicPancakeEntity::new)).toList().toArray(new World[0]);
    setup(myWorlds);
  }
}
