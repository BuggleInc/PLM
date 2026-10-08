package lessons.welcome.loopfor;

public class LoopForEntity extends plm.universe.bugglequest.SimpleBuggle {
  public LoopForEntity() { forbidMultiStep(); }

  @Override
  /* BEGIN REMOTE */
  /* BEGIN TEMPLATE */
  public void run()
  {
    /* BEGIN SOLUTION */
    int cpt = 0;
    while (!isOverBaggle()) {
      cpt++;
      stepForward();
    }
    pickupBaggle();
    for (int cpt2 = 0; cpt2 < cpt; cpt2++) {
      stepBackward();
    }
    dropBaggle();
    /* END SOLUTION */
  }
  /* END TEMPLATE */
  /* END REMOTE */
}
