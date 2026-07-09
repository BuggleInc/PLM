package lessons.welcome.array.search;

import static plm.core.ValueSerializer.*;

import plm.universe.bat.BatEntity;

public class OccurrenceOfValueEntity extends BatEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(occurrences(toPrimitive(toArrayOfType(param[0], Integer.class)), (Integer)param[1])));
    }
  }

  /* BEGIN TEMPLATE */
  // counts the occurrences of the value 'lookingFor' contained in tab variable
  public int occurrences(int[] tab, int lookingFor)
  {
    /* BEGIN SOLUTION */
    int count = 0;
    for (int i = 0; i < tab.length; i++) {
      if (tab[i] == lookingFor) {
        count++;
      }
    }
    return count;
    /* END SOLUTION */
  }

  /* END TEMPLATE */
}
