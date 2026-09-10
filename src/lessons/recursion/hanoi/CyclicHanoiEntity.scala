package lessons.recursion.hanoi;

import lessons.recursion.hanoi.universe.HanoiEntity;

class CyclicHanoiEntity extends HanoiEntity {
  override def move(from:Int, to:Int) = cyclicMove(from, to)
  
	override def run() {
    val src=getParamInt(0)
    val mid=getParamInt(1)
    val dst=getParamInt(2)
		clockwise(getSlotSize(src), src,mid,dst);
	}

	/* BEGIN TEMPLATE */
	def clockwise(height:Int, src:Int, mid:Int, dst:Int) {
		/* BEGIN SOLUTION */
		if (height>0) {
      anti(height-1,src,dst,mid);
      move(src,dst);
      anti(height-1,mid,src,dst);
		}
  }
  def anti(height:Int, src:Int, mid:Int, dst:Int) {
	  if (height > 0) {   
      //System.err.println("beg counterclockwise("+height+","+src+","+mid+","+dst+")");
      anti(height-1,src,mid,dst);
      move(src,mid);
      clockwise(height-1,dst,mid,src);      
      move(mid,dst);
      anti(height-1,src,mid,dst);
      //System.err.println("end counterclockwise("+height+","+src+","+mid+","+dst+")");
    }
		/* END SOLUTION */
	}
	/* END TEMPLATE */

}
