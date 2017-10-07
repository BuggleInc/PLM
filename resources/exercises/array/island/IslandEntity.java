package array.island;

import plm.universe.bat.BatEntity;
import plm.universe.bat.BatTest;

public class IslandEntity extends BatEntity {
	public void run(BatTest t) {
		t.setResult( island((int[])t.getParameter(0)) );
	}
	
	/* BEGIN TEMPLATE */
	int island(int[] num) {
		/* BEGIN SOLUTION */
		int nbisland=0;
		for(int i=0;i<num.length-1;i++){
			if(num[i]<num[i+1]){
				nbisland++;
			}
		}
		return nbisland;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
