package recursion.hanoi;


import plm.universe.hanoi.HanoiEntity
import plm.core.model.I18nManager

class ScalaLinearTwinHanoiEntity extends HanoiEntity {
  override def move(from:Int, to:Int) {
    if ((from == 0 && to == 2) || (from == 2 && to == 0)) 
      throw new RuntimeException(I18nManager.getI18n(getWorld().getLocale()).tr(
          "Sorry Dave, I cannot let you use move disks between slots 0 and 2 directly. Use the intermediate slot in all moves."));
    super.move(from,to);
  }
  

	override def run() {
    val src= getParam(0).asInstanceOf[Int]
    val mid= getParam(1).asInstanceOf[Int]
    val dst= getParam(2).asInstanceOf[Int]
		linearTwinHanoi(getSlotSize(src), src,mid, dst);
	}

	/* BEGIN TEMPLATE */
  def linearTwinHanoi(height:Int, src:Int, mid:Int, dst:Int) {
	  /* BEGIN SOLUTION */
    gather(height-1,src,mid,dst);
    move(src,mid);
    moveDouble(height-1, dst, mid, src);
    move(dst,mid);
    moveDouble(height-1, src, mid, dst);
    move(mid, src);
    moveDouble(height-1, dst, mid, src);
    move(mid, dst);
    scatter(height-1, src, mid, dst);
  }
  def gather(height:Int, src:Int, mid:Int, dst:Int) {
    if (height >0) {
      gather(height-1,src,mid,dst);
      move(src,mid);
      moveDouble(height-1, dst,mid,src);
      move(mid,dst);
      moveDouble(height-1, src, mid, dst);
    }
  }
  def scatter(height:Int, src:Int, mid:Int, dst:Int) {
    if (height>0) {
      moveDouble(height-1, src, mid, dst);
      move(src,mid);
      moveDouble(height-1, dst, mid, src);
      move(mid,dst);
      scatter(height-1,src,mid,dst);
    }
  }
  def moveDouble(height:Int, src:Int, mid:Int, dst:Int) {
    if (height>0) {
      moveDouble(height-1, src, mid, dst);
      move(src,mid);
      move(src,mid);
      moveDouble(height-1, dst, mid, src);
      move(mid,dst);
      move(mid,dst);
      moveDouble(height-1, src, mid, dst);
    }

		/* END SOLUTION */
	}
	/* END TEMPLATE */

}
