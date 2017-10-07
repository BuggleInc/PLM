package recursion.cons.nfirst;

import plm.universe.bat.BatTest;
import plm.universe.cons.ConsEntity;
import plm.universe.cons.RecList;

public class NfirstEntity extends ConsEntity {

	@Override
	public void run(BatTest t) {
		RecList parameter = RecList.fromArray( (int[]) t.getParameter(0) );
		t.setResult( nfirst( parameter, (int) t.getParameter(1) ) );
	}

	/* BEGIN TEMPLATE */
	RecList nfirst(RecList seq, int n) {
		/* BEGIN SOLUTION */
		if (n == 0) return null;
		return new RecList( seq.head, nfirst(seq.tail, n-1));
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
