package lessons.welcome.array.search;

import static plm.core.ValueSerializer.*;

import plm.universe.bat.BatEntity;

public class IndexOfMaxValueEntity extends BatEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(indexOfMaximum((int[])param[0])));
    }
  }

  /* BEGIN TEMPLATE */
  // computes the index of the maximum of the values contained in tab variable
  public int indexOfMaximum(int[] tab)
  {
    /* BEGIN SOLUTION */
    int max   = Integer.MIN_VALUE;
    int index = 0;
    for (int i = 0; i < tab.length; i++) {
      if (tab[i] > max) { // we are looking for the first occurence
        max   = tab[i];
        index = i;
      }
    }
    return index;
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
