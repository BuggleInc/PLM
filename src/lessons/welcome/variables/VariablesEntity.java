package lessons.welcome.variables;

public class VariablesEntity extends plm.universe.bugglequest.SimpleBuggle {
  public VariablesEntity() { forbidMultiStep(); }

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
    while (cpt > 0) {
      stepBackward();
      cpt--;
    }
    dropBaggle();
    /* END SOLUTION */
  }
  /* END TEMPLATE */
  /* END REMOTE */
}
