package recursion.hanoi;

import java.io.BufferedWriter;
import java.io.IOException;

import plm.universe.hanoi.HanoiEntity;

public class SplitHanoi2Entity extends HanoiEntity {

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
		//Logger.debug("solve("+height+","+src1+","+src2+","+other+","+dst+")");
		if (height > 0) {
			splitHanoi(height-1, src,dst1,dst2,other);
			move(src,dst1);
			hanoi(height-1, dst2,src,dst1);
			move(src,dst2);
			hanoi(height-1, other,src,dst2);
		}
	}
	private void hanoi(int height, int src, int other, int dst) {
		//for (int i=4;i>height;i--) System.out.print(" ");
		//Logger.debug("hanoi("+height+","+src+","+other+","+dst+")");
		if (height>0) {
			hanoi(height-1, src,dst,other);
			move(src,dst);
			hanoi(height-1, other, src, dst);
		}
		/* END SOLUTION */
	}
	/* END TEMPLATE */
	
	
	
	
	
	
	
	
	
	
	
	
	

}
