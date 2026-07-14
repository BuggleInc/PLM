package lessons.welcome.methods.slug;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.IOException;
import plm.core.lang.primitives.EntityPrimitives;
import plm.core.lang.primitives.Primitive;
import plm.core.utils.ColorMapper;

@EntityPrimitives(lessons.welcome.methods.slug.SlugSnailEntity.class)
public class SlugSnailEntity extends plm.universe.bugglequest.SimpleBuggle {

  public void command(String command, BufferedWriter out) throws Exception
  {
    int num = Integer.parseInt((String)command.subSequence(0, 3));
    switch (num) {
      case 215:
        try {
          out.write(Integer.toString(ColorMapper.color2int(getColorIntParam())));
          out.write("\n");
          out.flush();
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      default:
        super.command(command, out);
        break;
    }
  }

  @Primitive(215) public Color getColorIntParam() { return (Color)getParam(0); }

  @Override public void run() { hunt(getColorIntParam()); }

  /* BEGIN TEMPLATE */
  public void hunt(Color c)
  {
    // Write your code here
    /* BEGIN SOLUTION */
    while (!isOverBaggle()) {
      if (isFacingTrail(c)) {
        brushDown();
        stepForward();
        brushUp();
      } else {
        left();
      }
    }
    pickupBaggle();
    /* END SOLUTION */
  }

  // here comes your isFacingTrail method

  /* BEGIN HIDDEN */
  boolean isFacingTrail(Color c)
  {
    if (isFacingWall())
      return false;

    stepForward();
    boolean res = getGroundColor() == c;
    stepBackward();
    return res;
  }
  /* END HIDDEN */
  /* END TEMPLATE */
}
