package lessons.sort.pancake;

import java.util.Arrays;
import lessons.sort.basic.bubble.AlgBubbleSort1Entity;
import lessons.sort.pancake.universe.PancakeEntity;
import lessons.sort.pancake.universe.PancakeWorld;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.World;

public class CohenPancake extends ExerciseTemplated {

  public CohenPancake(Lesson lesson)
  {
    super(lesson);

    World[] myWorlds = new PancakeWorld[] {
        new PancakeWorld("5 pancakes", new int[] {2, 4, 5, 3, 1}, true),                            // 1C 1B 1C 2B
        new PancakeWorld("upside down", new int[] {-1, -2, -3, -4, -5, -6, -7, -8, -9, -10}, true), // 2B
        new PancakeWorld("7 pancakes", new int[] {3, 6, 1, 2, 5, 4, 7}, true),                      // 1C 1B 1B 2B
        new PancakeWorld("8 pancakes",
                         new int[] {
                             5,
                             2,
                             7,
                             4,
                             1,
                             6,
                             8,
                             3,
                         },
                         true),                                                      // 1C 1C 1B 1C 2B
        new PancakeWorld("9 pancakes", new int[] {4, 2, 3, 7, 9, 1, 5, 6, 8}, true), // 1C 1B 1B 1C 2B
        new PancakeWorld("15 pancackes",
                         new int[] {
                             7,
                             2,
                             3,
                             14,
                             9,
                             5,
                             1,
                             8,
                             10,
                             11,
                             6,
                             12,
                             15,
                             4,
                             13,
                         },
                         true), // 1C 1C 1B 1B 1C 1C 1B 1C 2A 1A 2B
        new PancakeWorld("random 15 pancakes", 15, true).randomize(),
        new PancakeWorld("30 pancakes",
                         new int[] {
                             1, 5, 17, 8, 24, 27, 14, 4, 11, 10, 28, 2, 29, 25, 15, 20, 3, 18, 19, 7, 21, 12, 23, 22, 16, 26, 6, 9, 13, 30,
                         },
                         true),
        // 1C 1C 1C 1B 1C 1C 1B 1C 1B 1C 1B 1C 1B 1C 1C 1C 1C 1B 1B 1B 1C 1B 1B 1C 1B 1B
        new PancakeWorld("5 pancakes, every case", new int[] {-1, -2, -5, 4, 3}, true), // 1B 1C 2A 1A 2B
    };
    for (int i = 0; i < myWorlds.length; i++)
      new PancakeEntity("Bill", myWorlds[i]);

    myWorlds = Arrays.stream(myWorlds).map(s -> s.replaceEntities(CohenPancakeEntity::new)).toList().toArray(new World[0]);
    setup(myWorlds);
  }
}
