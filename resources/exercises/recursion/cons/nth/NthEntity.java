package recursion.cons.nth;

import plm.universe.bat.BatTest;
import plm.universe.cons.ConsEntity;
import plm.universe.cons.RecList;

public class NthEntity extends ConsEntity {

	@Override
	public void run(BatTest t) {
		RecList parameter = RecList.fromArray( (int[]) t.getParameter(0) );
		t.setResult( nth( parameter, (int) t.getParameter(1) ) );
	}

	/* BEGIN TEMPLATE */
	int nth(RecList seq, int n) {
		/* BEGIN SOLUTION */
		if (n == 1) return seq.head;
		return nth(seq.tail, n-1);
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
