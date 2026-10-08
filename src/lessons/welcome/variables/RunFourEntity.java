package lessons.welcome.variables;

public class RunFourEntity extends plm.universe.bugglequest.SimpleBuggle {
  public RunFourEntity() { forbidMultiStep(); }

  @Override
  /* BEGIN REMOTE */
  /* BEGIN TEMPLATE */
  public void run()
  {
    /* BEGIN SOLUTION */
    int cpt = 0;
    while (cpt != 4) {
      stepForward();
      if (isOverBaggle())
        cpt++;
    }
    /* END SOLUTION */
  }
  /* END TEMPLATE */
  /* END REMOTE */
}
