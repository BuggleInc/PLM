package lessons.recursion.hanoi;

import lessons.recursion.hanoi.universe.HanoiEntity;
import plm.core.model.Game

class LinearHanoiEntity extends HanoiEntity {
  override def move(from:Int, to:Int) {
    if ((from == 0 && to == 2) || (from == 2 && to == 0)) 
      throw new RuntimeException(Game.i18n.tr(
          "Sorry Dave, I cannot let you move disks between slots 0 and 2 directly. Use the intermediate slot in all moves."));
    super.move(from,to);
  }
  

	override def run() {
    val src= getParamInt(0)
    val mid= getParamInt(1)
    val dst= getParamInt(2)
		linearHanoi(getSlotSize(src), src,mid, dst);
	}

	/* BEGIN TEMPLATE */
  def linearHanoi(height:Int, src:Int, mid:Int, dst:Int) {
	  /* BEGIN SOLUTION */
    if (height > 0) {
      linearHanoi(height-1, src,mid,dst);
      move(src,mid);
      linearHanoi(height-1, dst,mid,src);
      move(mid,dst);
      linearHanoi(height-1, src,mid,dst);
    }    
		/* END SOLUTION */
	}
	/* END TEMPLATE */

}
