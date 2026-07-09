package lessons.welcome.bat.bool2;

import static plm.core.ValueSerializer.deserialize;
import static plm.core.ValueSerializer.serialize;

import plm.universe.bat.BatEntity;

public class FizzBuzzEntity extends BatEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(fizzBuzz((Integer)param[0])));
    }
  }

  /* BEGIN TEMPLATE */
  String fizzBuzz(int a)
  {
    /* BEGIN SOLUTION */
    if (a % 5 == 0 && a % 3 == 0)
      return "Fizz Buzz";
    else if (a % 5 == 0)
      return "Buzz";
    else if (a % 3 == 0)
      return "Fizz";
    return "" + a;
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
