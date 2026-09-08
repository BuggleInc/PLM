package lessons.turmites.langtoncolors;

import java.awt.Color;
import lessons.turmites.universe.TurmiteEntity;

public class LangtonColorsEntity extends TurmiteEntity {

  /* BEGIN TEMPLATE */
  public void step(char[] rule, Color[] colors)
  {
    /* BEGIN SOLUTION */
    Color current = getGroundColor();
    for (int i = 0; i < colors.length; i++) {
      if (current == (colors[i])) {
        switch (rule[i]) {
          case 'L':
            left();
            break;
          case 'R':
            right();
            break;
          default:
            System.out.println("Unknown command associated to i=" + i + ": " + rule[i]);
        }

        setBrushColor(colors[(i + 1) % colors.length]);
        brushDown();
        brushUp();

        stepForward();

        return;
      }
    }
    /* END SOLUTION */
  }
  /* END TEMPLATE */

  public void run()
  {
    Color[] allColors = {Color.white, Color.black, Color.blue,    Color.cyan,     Color.green, Color.orange,
                         Color.red,   Color.gray,  Color.magenta, Color.darkGray, Color.pink,  Color.lightGray};

    int nbSteps = getParamInt(0);
    char[] rule = getParamString(1).toCharArray();

    Color[] colors = new Color[rule.length];
    for (int i = 0; i < rule.length; i++)
      colors[i] = allColors[i];

    for (int i = 0; i < nbSteps; i++) {
      stepDone();
      step(rule, colors);
    }
  }
}
