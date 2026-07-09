package lessons.welcome.array.golomb;

import static plm.core.ValueSerializer.*;

import plm.universe.bat.BatEntity;

public class GolombEntity extends BatEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(golomb((Integer)param[0])));
    }
  }

  /* BEGIN TEMPLATE */
  int golomb(int num)
  {
    /* BEGIN SOLUTION */
    if (num == 1) {
      return 1;
    } else {
      return 1 + golomb(num - golomb(golomb(num - 1)));
    }
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
