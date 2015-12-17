package lessons.recursion.hanoi;

import java.io.BufferedWriter;
import java.io.IOException;

import plm.universe.hanoi.HanoiEntity;

public class HanoiBoardEntity extends HanoiEntity {
	
	@Override
	public void command(String command, BufferedWriter out) {
		int num = Integer.parseInt((String) command.subSequence(0, 3));
		int nb;
		try {
			switch(num){
			case 114:
				nb = Integer.parseInt((command.split(" ")[1]));
				out.write(Integer.toString((Integer)getParam(nb)));
				out.write("\n");
				break;
			default:
				super.command(command, out);
				break;
			}
			out.flush();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}

	public void run() {
		solve((Integer)getParam(0),(Integer) getParam(1),(Integer) getParam(2));
	}

	public void solve(int src, int other, int dst) {
		hanoi(getSlotSize(src), src, other, dst);
	}

	/* BEGIN TEMPLATE */
	public void hanoi(int height, int src, int other, int dst) {
		/* BEGIN SOLUTION */
		if (height != 0) {
			hanoi(height-1, src, dst, other);
			move(src,dst);
			hanoi(height-1, other,src, dst);
		}
		/* END SOLUTION */
	}
	/* END TEMPLATE */

}
