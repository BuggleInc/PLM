package recursion.cons.nlast;

import plm.universe.bat.BatTest;
import plm.universe.cons.ConsEntity;
import plm.universe.cons.RecList;

public class NlastEntity extends ConsEntity {

	@Override
	public void run(BatTest t) {
		RecList parameter = RecList.fromArray( (int[]) t.getParameter(0) );
		t.setResult( nlast( parameter, (int) t.getParameter(1) ) );
	}

	/* BEGIN TEMPLATE */
	RecList nlast(RecList seq, int n) {
		/* BEGIN SOLUTION */
		if (seq == null || seq.plmInsiderLength() <= n)
			return seq;
		return nlast(seq.tail, n-1);
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
