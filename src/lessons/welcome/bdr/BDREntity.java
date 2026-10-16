package lessons.welcome.bdr;

import plm.core.lang.primitives.EntityPrimitives;

@EntityPrimitives(lessons.welcome.bdr.BDREntity.class)
public class BDREntity extends plm.universe.bugglequest.SimpleBuggle {

  @Override public char getIndicationBdr()
  {
    if (isOverMessage()) {
      return readMessage().charAt(0);
    } else {
      return ' ';
    }
  }

  /* BEGIN REMOTE */
  /* BEGIN TEMPLATE */
  public void run()
  {
    /* BEGIN SOLUTION */
    while (true) {
      char c = getIndicationBdr();

      if (c == 'R') {
        right();
        step();
      } else if (c == 'L') {
        left();
        step();
      } else if (c == 'I') {
        back();
        step();
      } else if (c == 'A')
        step();
      else if (c == 'B')
        forward(2);
      else if (c == 'C')
        forward(3);
      else if (c == 'Z')
        stepBack();
      else if (c == 'Y')
        backward(2);
      else if (c == 'X')
        backward(3);
      else
        return;
    }
    /* END SOLUTION */
  }
  /* END TEMPLATE */
  /* END REMOTE */
}
