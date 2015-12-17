package lessons.sort.pancake;

import plm.universe.pancake.PancakeEntity;

public class CohenPancakeEntity extends PancakeEntity {
	
	public void run() {
		this.solve();
	}

	/* BEGIN HIDDEN */
	int getRankOf(int size) {
		for (int rank=0;rank<getStackSize();rank++)
			if (getPancakeRadius(rank) == size)
				return rank;
		return -99; // Well, be robust to border cases 
	}
	int debug=0; // 0: silence; 1: all details
	void showStack(boolean nl) {
		if (debug>0) {
			System.out.print("{");
			for (int rank=0; rank < getStackSize(); rank++) 
				System.out.print( (isPancakeUpsideDown(rank)?"-":"") + getPancakeRadius(rank)+", ");
			System.out.print("}  ");
			if (nl)
				getGame().getLogger().log("");
		}
	}
	/* END HIDDEN */

	/* BEGIN TEMPLATE */
	public void solve() {
		/* BEGIN SOLUTION */
		int maxPos = getStackSize();
		while (true) {
					
			if (debug>0)
				System.out.print("maxPos:"+maxPos+" ");
			showStack(false);
			
			int maxupside = -1, maxupsidePos = -1;
			boolean sorted = true;
			for (int pos=0; pos<getStackSize(); pos++) {
				if (getPancakeRadius(pos) != pos+1 || isPancakeUpsideDown(pos))
					sorted = false;
				
				// Search if we are in case 1 on the considered interval
				if (pos<maxPos && !isPancakeUpsideDown(pos) && (maxupside < getPancakeRadius(pos))) {
					maxupside = getPancakeRadius(pos);
					maxupsidePos = pos;
				}
			}
			
			if (sorted) { // we are done, no need to continue
				if (debug > 0) 
					getGame().getLogger().log("It's sorted now. Get out of here\n");
				break;
			}
			
			if (maxupside != -1) { // Case 1. 
				if (maxupside == maxPos) { // Case 1.C
					if (debug > 0) 
						getGame().getLogger().log("Case 1.C; maxupsidePos = "+maxupsidePos+", maxupside = "+maxupside);
					if (maxupsidePos+1 != maxPos) {
						flip(maxupsidePos+1);
						flip(maxPos);
					}
					maxPos--;
				} else {
					int pPlus1 = getRankOf(maxupside+1);
					if (pPlus1 > maxupsidePos) {
						if (debug > 0) 
							getGame().getLogger().log("Case 1.A; maxupsidePos = "+maxupsidePos+", maxupside = "+maxupside+", pPlus1 = "+pPlus1);
						flip(pPlus1+1);showStack(true);
						flip(pPlus1-maxupsidePos);
					} else {
						if (debug > 0) 
							getGame().getLogger().log("Case 1.B; maxupsidePos = "+maxupsidePos+", maxupside = "+maxupside+", pPlus1 = "+pPlus1);						
						flip(maxupsidePos+1);
						flip(maxupsidePos-pPlus1);
					}
				}
			} else { // Case 2. All pancakes are upside down.
				boolean reverted = true;
				for (int pos=0; reverted && pos<maxPos; pos++) 
					if (getPancakeRadius(pos)!=pos+1)
						reverted = false;
				
				if (reverted) {
					if (debug > 0) 
						getGame().getLogger().log("Case 2.B");
					for (int i=0; i<maxPos; i++) {
						flip(maxPos);
						if (maxPos>1)
							flip(maxPos-1);
						showStack(true);
					}
				} else { 
					
					int pPlus1 = getRankOf(getStackSize()+1);
					int p;
					for (int radius=maxPos; radius > 0; radius--) {
						p = getRankOf(radius);
						if (p>maxPos)
							p=-99;
						if (pPlus1!=-99 && pPlus1<p) { // we've got the larger p such that p+1 is above p and both are upsideof
							if (debug > 0) 
								getGame().getLogger().log("Case 2.A; p="+p+", radius="+radius+", pPlus1="+pPlus1);
							flip(p+1);
							if (pPlus1!=0)
								flip(pPlus1+1);
							radius = -1; // We're done with this iteration of the loop
						}
						pPlus1 = p; // shift downward
					}
				}
			}
		}
		
		/* END SOLUTION */
	}
	/* END TEMPLATE */

}
