package lessons.turmites.langton;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.IOException;

import lessons.turmites.LangtonEntityPrimitives;
import plm.core.lang.primitives.EntityPrimitives;
import plm.universe.bugglequest.SimpleBuggle;

@EntityPrimitives(LangtonEntityPrimitives.class)
public class LangtonEntity extends SimpleBuggle implements LangtonEntityPrimitives {
	/* BEGIN TEMPLATE */
	public void step() {
		/* BEGIN SOLUTION */
		if (getGroundColor()== Color.white) {
			right();

			setBrushColor(Color.black);
			brushDown();
			brushUp();

			stepForward();
		} else {
			left();

			setBrushColor(Color.white);
			brushDown();
			brushUp();

			stepForward();
		}
		/* END SOLUTION */
	}
	/* END TEMPLATE */

	@Override
	public void run() { 
		int nbSteps = getParamInt(0);
		for (int i=0;i<nbSteps;i++) {
			step();
			stepDone();
		}
	}

        @Override public void command(String command, BufferedWriter out) throws Exception
        {
          int num = Integer.parseInt((String)command.subSequence(0, 3));
          switch (num) {
            case 200:
              try {
                out.write((getParamInt(0)));
                out.write("\n");
                out.flush();
              } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
              }
              break;
            case 230:
              stepDone();
              break;
            default:
              super.command(command, out);
          }
        }

		@Override
        public void stepDone() { ((lessons.turmites.universe.TurmiteWorld)world).stepDone(); }
}
