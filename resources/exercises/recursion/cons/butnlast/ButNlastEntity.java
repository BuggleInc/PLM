package recursion.cons.butnlast;

import plm.universe.bat.BatTest;
import plm.universe.cons.ConsEntity;
import plm.universe.cons.RecList;

public class ButNlastEntity extends ConsEntity {

	@Override
	public void run(BatTest t) {
		RecList parameter = RecList.fromArray( (int[]) t.getParameter(0) );
		t.setResult( butNlast( parameter, (int) t.getParameter(1) ) );
	}

	/* BEGIN TEMPLATE */
	RecList butNlast(RecList seq, int n) {
		/* BEGIN SOLUTION */
		if (seq == null || seq.plmInsiderLength() <= n)
			return null;
		return new RecList( seq.head, butNlast(seq.tail, n-1));
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
