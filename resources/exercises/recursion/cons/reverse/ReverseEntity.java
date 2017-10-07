package recursion.cons.reverse;

import plm.universe.bat.BatTest;
import plm.universe.cons.ConsEntity;
import plm.universe.cons.RecList;

public class ReverseEntity extends ConsEntity {

	@Override
	public void run(BatTest t) {
		RecList parameter = RecList.fromArray( (int[]) t.getParameter(0) );
		t.setResult( reverse( parameter ) );
	}

	/* BEGIN TEMPLATE */
	RecList reverse(RecList seq) {
		/* BEGIN SOLUTION */
		RecList A = null;
		RecList B = seq;
		while (B != null) {
			A = new RecList(B.head, A);
			B = B.tail;
		}
		return A;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
