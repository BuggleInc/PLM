package lessons.recursion.hanoi;

import java.io.BufferedWriter;
import java.io.IOException;

import lessons.recursion.hanoi.universe.HanoiEntity;

public class SplitHanoi1Entity extends HanoiEntity {
	
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
		solve((Integer)getParam(0),(Integer) getParam(1),(Integer) getParam(2),(Integer) getParam(3));
	}

	public void solve(int src, int other, int dst1, int dst2) {
		splitHanoi(getSlotSize(src)/2, src,other, dst1, dst2);
	}

	/* BEGIN TEMPLATE */
	public void splitHanoi(int height, int src,int other, int dst1, int dst2) {
		/* BEGIN SOLUTION */
		//for (int i=4;i>height;i--) System.out.print(" ");
		//getGame().getLogger().log("solve("+height+","+src1+","+src2+","+other+","+dst+")");
		if (height > 0) {
			moveDouble(height-1, src,dst1,dst2,other);
			move(src,dst2);
			move(src,dst1);
			splitHanoi(height-1, other,src,dst1, dst2);
		}
	}
	private void moveDouble(int height, int src, int other1, int other2, int dst) {
		//for (int i=4;i>height;i--) System.out.print(" ");
		//getGame().getLogger().log("hanoi("+height+","+src+","+other+","+dst+")");
		if (height>0) {
			moveDouble(height-1, src,other1,dst,other2);
			move(src,other1);
			move(src,dst);
			move(other1,dst);
			moveDouble(height-1, other2, src, other1,dst);
		}
		/* END SOLUTION */
	}
	/* END TEMPLATE */

}
