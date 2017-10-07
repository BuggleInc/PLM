package recursion.cons.increasing;

import plm.universe.bat.BatTest;
import plm.universe.cons.ConsEntity;
import plm.universe.cons.RecList;

public class IncreasingEntity extends ConsEntity {

	@Override
	public void run(BatTest t) {
		RecList parameter = RecList.fromArray( (int[]) t.getParameter(0) );
		t.setResult( increasing( parameter ) );
	}

	/* BEGIN TEMPLATE */
	boolean increasing(RecList seq) {
		/* BEGIN SOLUTION */
		if (seq == null || seq.tail == null)
			return true;
		if (seq.head > seq.tail.head)
			return false;
		return increasing(seq.tail);
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
