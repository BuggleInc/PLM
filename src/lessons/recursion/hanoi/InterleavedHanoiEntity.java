package lessons.recursion.hanoi;

import java.io.BufferedWriter;
import java.io.IOException;

import plm.universe.hanoi.HanoiEntity;

public class InterleavedHanoiEntity extends HanoiEntity {
	
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

	public void solve(int src1,int src2, int other, int dst) {
		interleavedHanoi(getSlotSize(src1), src1,src2, other, dst);
	}

	/* BEGIN TEMPLATE */
	public void interleavedHanoi(int height, int src1,int src2, int other, int dst) {
		/* BEGIN SOLUTION */
		//for (int i=4;i>height;i--) System.out.print(" ");
		//getGame().getLogger().log("solve("+height+","+src1+","+src2+","+other+","+dst+")");
		if (height > 0) {
			hanoi(height-1, src1,dst,other);
			move(src1,dst);
			hanoi(height-1, src2,dst,src1);
			move(src2,dst);
			interleavedHanoi(height-1, other,src1,src2, dst);
		}
	}
	private void hanoi(int height, int src, int other, int dst) {
		//for (int i=4;i>height;i--) System.out.print(" ");
		//getGame().getLogger().log("hanoi("+height+","+src+","+other+","+dst+")");
		if (height>0) {
			hanoi(height-1, src,dst,other);
			move(src,dst);
			hanoi(height-1, other,src,dst);
		}
		/* END SOLUTION */
	}
	/* END TEMPLATE */

}
