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

        public void run() {
		/* BEGIN SOLUTION */
		while (true) {
			char c = getIndicationBdr();

			if (c == 'R') { 
				right(); forward();
			} else if (c == 'L') {
				left(); forward();
			} else if (c == 'I') {
				back(); forward(); 
			} else if (c == 'A')
				forward();
			else if (c == 'B')
				forward(2);  
			else if (c == 'C')
				forward(3);
			else if (c == 'Z')
				backward();
			else if (c == 'Y')
				backward(2);  
			else if (c == 'X')
				backward(3);
			else 
				return ;
		}		
		/* END SOLUTION */
	}
}
