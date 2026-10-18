package lessons.sort.pancake;

import lessons.sort.pancake.universe.PancakeEntity;
import lessons.sort.pancake.universe.PancakeWorld;



/* This is not exactly the gates algorithm. Here is the original text:
 * <ul><li><b>Case f</b>: <code>t</code> is in a block of length k+1 (the last element is <code>t+ko</code>), <code>t-o</code>
 * is the last element of another block and <code>t+(k+1)o</code> is free (there is two differing situation, depending on the relative order of 
 * <code>t-o</code> and <code>t+(k+1)o</code>. They are merged in 4 flip with the corresponding sequence below.<br/>
 * <div align="center"><img src="lessons/sort/pancake/gates-f1.png"/></div><br/>
 * Other possibility when <code>t-o &gt; t+(k+1)o</code>:<br/>
 * <div align="center"><img src="lessons/sort/pancake/gates-f2.png"/></div></li></ul>

 */


class GatesPancakeEntity extends PancakeEntity {

	/* BEGIN REMOTE */
	override def run(): Unit = {
		solve();
	}

	/* BEGIN SOLUTION */
	def getRankOf(size:Int ): Int =
		(0 until getStackSize()).find(getPancakeRadius(_) == size).getOrElse(-99) // Be robust to border cases
	def isFree(pos:Int):Boolean = {
		if (pos == -99)
			return false;
		val radius = getPancakeRadius(pos);
		if (pos>0) {
			val nextRadius = getPancakeRadius(pos-1);
			if (nextRadius == radius-1 || nextRadius == radius+1)
				return false;
		}
		if (pos<getStackSize()-1) {
			val nextRadius = getPancakeRadius(pos+1);
			if (nextRadius == radius-1 || nextRadius == radius+1)
				return false;
		}
		return true;
	}
	def isFirst(pos:Int):Boolean = {
		if (pos == -99)
			return false;
		val radius = getPancakeRadius(pos);
		if (pos>0) {
			val nextRadius = getPancakeRadius(pos-1);
			if (nextRadius == radius-1 || nextRadius == radius+1)
				return false;
		}
		if (pos<getStackSize()-1) {
			val nextRadius = getPancakeRadius(pos+1);
			if (nextRadius == radius-1 || nextRadius == radius+1)
				return true;
		}
		return false;
	}
	def isLast(pos:Int):Boolean = {
		if (pos == -99)
			return false;
		val radius = getPancakeRadius(pos);
		if (pos<getStackSize()-1) {
			val nextRadius = getPancakeRadius(pos+1);
			if (nextRadius == radius-1 || nextRadius == radius+1)
				return false;
		}
		if (pos>0) {
			val nextRadius = getPancakeRadius(pos-1);
			if (nextRadius == radius-1 || nextRadius == radius+1)
				return true;
		}
		return false;
	}
	def blockLength():Int = {
		var pos = 0;
		var radius = getPancakeRadius(pos);
		val o = getPancakeRadius(pos+1) - radius;
		
		if (o != -1 && o != 1) {
			System.out.println("Asked to compute the block length, but the step o is "+o+" instead of +1 or -1. " +
					"The length is then 1, but you are violating a precondition somehow");
			return 1;
		}
		
		while (pos < getStackSize()-1 && getPancakeRadius(pos+1) == radius + o) {
			pos+=1;
			radius += o;
		}
		return pos+1;
	}
	/* END SOLUTION */
	
	/* BEGIN TEMPLATE */
	def solve(): Unit = {
		/* BEGIN SOLUTION */
		val stackSize = getStackSize();
		
		while (true) {
			val tRadius = getPancakeRadius(0);
			val posTPlus  = getRankOf(tRadius+1); // returns -99 if non-existent, that is then ignored
			val posTMinus = getRankOf(tRadius-1); 
			val posT = 0;
			
			if (isFree(posT)) {			
				if (isFree(posTPlus)) { /* CASE A: t and t+o free */
					flip(posTPlus);
				} else if (isFree(posTMinus)) { /* CASE A: t and t-o free */
					flip(posTMinus);
					
				} else if (isFirst(posTPlus)) { /* CASE B: t free, t+o first element */
					flip(posTPlus);
				} else if (isFirst(posTMinus)) { /* CASE B: t free, t-o first element */
					flip(posTMinus);

				} else if (Math.min(posTPlus,posTMinus) != -99) { /* CASE C: t free, but both t+o and t-o are last elements */
					flip(Math.min(posTPlus,posTMinus) );
					flip(Math.min(posTPlus,posTMinus) - 1);
					flip(Math.max(posTPlus,posTMinus) + 1);
					flip(Math.min(posTPlus,posTMinus) - 1);
					
				} else {
					flip(Math.max(posTPlus,posTMinus) + 1);
					flip(Math.max(posTPlus,posTMinus) );
				}
				
			} else { // t is in a block
				if (blockLength() == stackSize) { // Done!
					if (tRadius != 1) // all reverse 
						flip(stackSize);
					return;
				}
				
				if (isFree(posTPlus)) {          /* CASE D: t in a block, t+1 free */
					flip(posTPlus);

				} else if (isFree(posTMinus)) {  /* CASE D: t in a block, t-1 free */
					flip(posTMinus);

				} else if (isFirst(posTPlus)) {  /* CASE E: t in a block, t+1 first element */
					flip(posTPlus);

				} else if (isFirst(posTMinus)) { /* CASE E: t in a block, t-1 first element */
					flip(posTMinus);

				} else if (isLast(posTPlus) && posTPlus != 1) { /* CASE F+: t in a block, t+1 last element */
					flip(blockLength());
					flip(posTPlus + 1);
					val newPos = getRankOf(tRadius);
					if (newPos>0)
						flip(newPos);
					
				} else if (isLast(posTMinus) && posTMinus != 1) { /* CASE F-: t in a block, t-1 last element */
					flip(blockLength());
					flip(posTMinus + 1);
					val newPos = getRankOf(tRadius);
					if (newPos>0)
						flip(newPos);
				} else {
					val k = blockLength()-1;
					val o = getPancakeRadius(1) - tRadius;
					val pos = getRankOf(tRadius+(k+1)*o);
					if (isFree(pos) || isFirst(pos)) {
						flip(k+1);
						flip(pos);
					} else {
						flip(pos+1);
						flip(getRankOf(tRadius+k*o));
					}
				}
			}
		}
		/* END SOLUTION */
	}
	/* END TEMPLATE */
	/* END REMOTE */


}
 