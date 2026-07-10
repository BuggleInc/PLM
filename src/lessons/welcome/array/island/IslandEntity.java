package lessons.welcome.array.island;

import static plm.core.ValueSerializer.*;

import plm.universe.bat.BatEntity;

public class IslandEntity extends BatEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(island((int[]) param[0])));
    }
  }

  /* BEGIN TEMPLATE */
  int island(int[] num)
  {
    /* BEGIN SOLUTION */
    int nbisland = 0;
    for (int i = 0; i < num.length - 1; i++) {
      if (num[i] < num[i + 1]) {
        nbisland++;
      }
    }
    return nbisland;
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
