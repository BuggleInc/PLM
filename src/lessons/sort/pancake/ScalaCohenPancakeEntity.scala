package lessons.sort.pancake;

import plm.core.model.Game
import plm.universe.pancake.PancakeEntity

class ScalaCohenPancakeEntity extends PancakeEntity {

	override def run() {
		solve();
	}

	/* BEGIN HIDDEN */
	def getRankOf(size: Integer):Integer = {
			for (rank <- 0 to getStackSize()-1)
				if (getPancakeRadius(rank) == size)
					return rank;
					return -99; // Well, be robust to border cases 
	}
	var debug=false; 
	def showStack(nl:Boolean) {
		if (debug) {
			System.out.print("{");
			for (rank <- 0 to getStackSize()-1) {
				if (isPancakeUpsideDown(rank))
					System.out.print("-")
					System.out.print(getPancakeRadius(rank)+", ");
			}  
			System.out.print("}  ");
			if (nl)
				getGame().getLogger().log("");
		}
	}
	/* END HIDDEN */

	/* BEGIN TEMPLATE */
	def solve() {
		/* BEGIN SOLUTION */
		var maxPos = getStackSize();
		while (true) {

			if (debug)
				System.out.print("maxPos:"+maxPos+" ");
			showStack(false);

			var maxupside = -1
					var maxupsidePos = -1
					var sorted = true
					for (pos <- 0 to getStackSize()-1) {
						if (getPancakeRadius(pos) != pos+1 || isPancakeUpsideDown(pos))
							sorted = false;

						// Search if we are in case 1 on the considered interval
						if (pos<maxPos && !isPancakeUpsideDown(pos) && (maxupside < getPancakeRadius(pos))) {
							maxupside = getPancakeRadius(pos);
							maxupsidePos = pos;
						}
					}

			if (sorted) { // we are done, no need to continue
				if (debug) 
					getGame().getLogger().log("It's sorted now. Get out of here\n");
				return;
			}

			if (maxupside != -1) { // Case 1. 
				if (maxupside == maxPos) { // Case 1.C
					if (debug) 
						getGame().getLogger().log("Case 1.C; maxupsidePos = "+maxupsidePos+", maxupside = "+maxupside);
					if (maxupsidePos+1 != maxPos) {
						flip(maxupsidePos+1);
						flip(maxPos);
					}
					maxPos = maxPos - 1;
				} else {
					val pPlus1 = getRankOf(maxupside+1);
					if (pPlus1 > maxupsidePos) {
						if (debug) 
							getGame().getLogger().log("Case 1.A; maxupsidePos = "+maxupsidePos+", maxupside = "+maxupside+", pPlus1 = "+pPlus1);
						flip(pPlus1+1);showStack(true);
						flip(pPlus1-maxupsidePos);
					} else {
						if (debug) 
							getGame().getLogger().log("Case 1.B; maxupsidePos = "+maxupsidePos+", maxupside = "+maxupside+", pPlus1 = "+pPlus1);						
						flip(maxupsidePos+1);
						flip(maxupsidePos-pPlus1);
					}
				}
			} else { // Case 2. All pancakes are upside down.
				var reverted = true;
				for (pos <- 0 to maxPos-1) 
					if (getPancakeRadius(pos)!=pos+1)
						reverted = false;

						if (reverted) {
							if (debug) 
								getGame().getLogger().log("Case 2.B");
							for (i <- 1 to maxPos) {
								flip(maxPos);
								showStack(true)
								if (maxPos>1)
									flip(maxPos-1);
								showStack(true);
							}
						} else { 

							var pPlus1 = getRankOf(getStackSize()+1)
									var p = -1
									var found = false
									for (radius <- maxPos to 0 by -1) {
										if (!found) {
											p = getRankOf(radius);
											if (p>maxPos)
												p = -99
												if (pPlus1 != -99 && pPlus1<p) { // we've got the larger p such that p+1 is above p and both are upsideof
													if (debug) 
														getGame().getLogger().log("Case 2.A; p="+p+", radius="+radius+", pPlus1="+pPlus1);
													flip(p+1);
													if (pPlus1!=0)
														flip(pPlus1+1);
													found = true
												}
											pPlus1 = p; // shift downward
										}
									}
						}
			}
		}		
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
