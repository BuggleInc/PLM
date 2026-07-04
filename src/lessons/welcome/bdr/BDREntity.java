package lessons.welcome.bdr;

import plm.core.lang.primitives.EntityPrimitives;

import java.io.BufferedWriter;
import java.io.IOException;

@EntityPrimitives(lessons.welcome.bdr.BDREntity.class)
public class BDREntity extends plm.universe.bugglequest.SimpleBuggle {

  @Override public void command(String command, BufferedWriter out) throws Exception
  {
    int num = Integer.parseInt((String)command.subSequence(0, 3));
    try {
      switch (num) {
        case 148:
          out.write(getIndicationBdr());
          out.write("\n");
          out.flush();
          break;
        default:
          super.command(command, out);
          break;
      }
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }


    @Override
    public char getIndicationBdr() {
        if (isOverMessage()) {
            return readMessage().charAt(0);
        } else {
            return ' ';
        }
    }

        /* BEGIN TEMPLATE */
        public void run() {
		/* BEGIN SOLUTION */
		while (true) {
			char c = getIndicationBdr();

			if (c == 'R') { 
				right(); stepForward();
			} else if (c == 'L') {
				left(); stepForward();
			} else if (c == 'I') {
				back(); stepForward();
			} else if (c == 'A')
				stepForward();
			else if (c == 'B')
				forward(2);
			else if (c == 'C')
				forward(3);
			else if (c == 'Z')
				stepBackward();
			else if (c == 'Y')
				backward(2);
			else if (c == 'X')
				backward(3);
			else 
				return ;
		}		
		/* END SOLUTION */
	}
    /* END TEMPLATE */
}
