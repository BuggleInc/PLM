package lessons.recursion.hanoi;

import java.io.BufferedWriter;
import java.io.IOException;

import lessons.recursion.hanoi.universe.HanoiEntity;

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

	/* BEGIN TEMPLATE */
	public void solve(int src, int dst, int other) {
		solve(src, dst, other, getSlotSize(src));
	}

	public void solve(int src, int dst, int other, int height) {
		/* BEGIN SOLUTION */
		if (height != 0) {
			solve(src,other,dst, height-1);
			move(src,dst);
			solve(other,dst,src, height-1);
		}
		/* END SOLUTION */
	}
	/* END TEMPLATE */

}
