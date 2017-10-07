package recursion.cons.concat;

import plm.universe.bat.BatTest;
import plm.universe.cons.ConsEntity;
import plm.universe.cons.RecList;

public class ConcatEntity extends ConsEntity {

	@Override
	public void run(BatTest t) {
		RecList parameter1 = RecList.fromArray( (int[]) t.getParameter(0) );
		RecList parameter2 = RecList.fromArray( (int[]) t.getParameter(1) );
		t.setResult( concat( parameter1, parameter2 ) );
	}

	/* BEGIN TEMPLATE */
	RecList concat(RecList seq1, RecList seq2) {
		/* BEGIN SOLUTION */
		// Revert seq1 into A
		RecList A = null;
		RecList B = seq1;
		while (B != null) {
			A = new RecList(B.head, A);
			B = B.tail;
		}
		// add A at front of seq2 in B
		B = seq2;
		while (A != null) {
			B = new RecList(A.head, B);
			A = A.tail;
		}
		return B;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
