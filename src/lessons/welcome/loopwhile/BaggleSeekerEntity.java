package lessons.welcome.loopwhile;

import plm.universe.bugglequest.SimpleBuggle;

public class BaggleSeekerEntity extends SimpleBuggle {

  public BaggleSeekerEntity() { forbidMultiStep(); }

  /* BEGIN REMOTE */
  @Override public void run()
  {
    /* BEGIN TEMPLATE */
    /* BEGIN SOLUTION */
    while (!isOverBaggle()) {
      stepForward();
    }
    /* END SOLUTION */
    /* END TEMPLATE */
  }
  /* END REMOTE */
}
