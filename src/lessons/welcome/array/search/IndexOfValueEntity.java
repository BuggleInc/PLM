package lessons.welcome.array.search;

import static plm.core.ValueSerializer.*;

import plm.core.model.Game;
import plm.universe.bat.BatEntity;

public class IndexOfValueEntity extends BatEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(indexOf(toPrimitive(toArrayOfType(param[0], Integer.class)), (Integer)param[1])));
    }
  }

  /* BEGIN TEMPLATE */
  // computes the index of the first value equals to 'lookingFor' contained in tab variable
  public int indexOf(int[] tab, Integer lookingFor)
  {
    /* BEGIN SOLUTION */
    for (int i = 0; i < tab.length; i++)
      if (tab[i] == lookingFor)
        return i;

    return -1;
    /* END SOLUTION */
  }

  /* END TEMPLATE */
}
