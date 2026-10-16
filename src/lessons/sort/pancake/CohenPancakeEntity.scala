package lessons.sort.pancake;

import lessons.sort.pancake.universe.PancakeEntity;

class CohenPancakeEntity extends PancakeEntity {

	/* BEGIN REMOTE */
	override def run(): Unit = {
		solve();
	}

	/* BEGIN SOLUTION */
	def getRankOf(radius: Int): Int =
		(0 until getStackSize()).find(getPancakeRadius(_) == radius).getOrElse(-1)

	/** Whether the pancakes at these consecutive ranks are joined: p above p+1, or -(p+1) above -p */
	def joined(above: Int, below: Int): Boolean = {
		val top = if (isPancakeUpsideDown(above)) -getPancakeRadius(above) else getPancakeRadius(above)
		val bottom = if (isPancakeUpsideDown(below)) -getPancakeRadius(below) else getPancakeRadius(below)
		bottom == top + 1
	}
	/* END SOLUTION */

	/* BEGIN TEMPLATE */
	def solve(): Unit = {
		/* BEGIN SOLUTION */
		// Joined pancakes are never separated: each block of joined pancakes behaves as a single pancake.
		// The pancakes from maxPos to the bottom are joined to the floor, and never moved again.
		var maxPos = getStackSize()
		while (true) {
			while (maxPos > 0 && getPancakeRadius(maxPos - 1) == maxPos && !isPancakeUpsideDown(maxPos - 1))
				maxPos -= 1
			if (maxPos == 0)
				return

			val ups = (0 until maxPos).filter(!isPancakeUpsideDown(_)).map(getPancakeRadius(_))
			if (ups.nonEmpty) { // Case 1: p is the block ending with the largest rightside up pancake
				val largestUp = ups.max
				val p = getRankOf(largestUp)
				if (largestUp == maxPos) { // Case 1.C
					flip(p + 1)
					flip(maxPos)
				} else {
					val pPlus1 = getRankOf(largestUp + 1) // bottom of the next block, which is upside down
					if (pPlus1 > p) { // Case 1.A
						flip(pPlus1 + 1)
						flip(pPlus1 - p)
					} else { // Case 1.B
						flip(p + 1)
						flip(p - pPlus1)
					}
				}
			} else { // Case 2: all pancakes are upside down
				// Case 2.A: the largest radius such that -(radius+1) is above -radius without being joined to it
				var radius = maxPos - 1
				while (radius > 0 && getRankOf(radius + 1) >= getRankOf(radius) - 1)
					radius -= 1
				if (radius > 0) {
					flip(getRankOf(radius + 1) + 1)
					flip(getRankOf(radius))
				} else { // Case 2.B: the blocks form -I. Once per block, flip them all, then all but the bottom one
					val blocks = 1 + (1 until maxPos).count(rank => !joined(rank - 1, rank))
					for (i <- 0 until blocks) {
						flip(maxPos)
						var bottom = maxPos - 1
						while (bottom > 0 && joined(bottom - 1, bottom))
							bottom -= 1
						if (bottom > 0)
							flip(bottom)
					}
				}
			}
		}
		/* END SOLUTION */
	}
	/* END TEMPLATE */
	/* END REMOTE */
}
