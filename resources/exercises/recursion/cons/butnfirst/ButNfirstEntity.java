package recursion.cons.butnfirst;

import plm.universe.bat.BatTest;
import plm.universe.cons.ConsEntity;
import plm.universe.cons.RecList;

public class ButNfirstEntity extends ConsEntity {

	@Override
	public void run(BatTest t) {
		RecList parameter = RecList.fromArray( (int[]) t.getParameter(0) );
		t.setResult( butNfirst( parameter, (Integer)t.getParameter(1) ) );
	}

	/* BEGIN TEMPLATE */
	RecList butNfirst(RecList seq, int n) {
		/* BEGIN SOLUTION */
		if (seq == null || n==0)
			return seq;
		return butNfirst(seq.tail, n-1);
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
