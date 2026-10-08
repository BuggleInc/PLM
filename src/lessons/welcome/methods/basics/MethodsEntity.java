package lessons.welcome.methods.basics;

public class MethodsEntity extends plm.universe.bugglequest.SimpleBuggle {
  public MethodsEntity() { forbidMultiStep(); }

  /* BEGIN REMOTE */
  /* BEGIN TEMPLATE */
  /* BEGIN SOLUTION */
  public void goAndGet()
  {
    int i = 0;
    while (!isOverBaggle()) {
      i++;
      stepForward();
    }
    pickupBaggle();
    while (i > 0) {
      stepBackward();
      i--;
    }
    dropBaggle();
  }
  /* END SOLUTION */
  /* END TEMPLATE */

  @Override public void run()
  {
    for (int i = 0; i < 7; i++) {
      goAndGet();
      right();
      stepForward();
      left();
    }
  }
  /* END REMOTE */
}
