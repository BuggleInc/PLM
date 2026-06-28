package lessons.welcome.methods.slug;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.IOException;

import plm.core.lang.primitives.EntityPrimitives;
import plm.core.lang.primitives.Primitive;
import plm.core.utils.ColorMapper;

@EntityPrimitives(SlugSnailEntity.class)
public class SlugSnailEntity extends plm.universe.bugglequest.SimpleBuggle {

  public void command(String command, BufferedWriter out) throws Exception
  {
    int num = Integer.parseInt((String)command.subSequence(0, 3));
    switch (num) {
      case 215:
        try {
          out.write(Integer.toString(getColorIntParam()));
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

  @Primitive(215)
  public int getColorIntParam() { return ColorMapper.color2int((Color)getParam(0)); }

  @Override public void run() { hunt((Color)getParam(0)); }

  /* BEGIN TEMPLATE */
  public void hunt(Color c)
  {
    // Write your code here
    /* BEGIN SOLUTION */
    while (!isOverBaggle()) {
      if (isFacingTrail(c)) {
        brushDown();
        forward();
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

    forward();
    boolean res = getGroundColor().equals(c);
    backward();
    return res;
  }
  /* END HIDDEN */
  /* END TEMPLATE */
}
