package recursion.cons.alldifferent;

import plm.universe.bat.BatTest;
import plm.universe.cons.ConsEntity;
import plm.universe.cons.RecList;

public class AllDifferentEntity extends ConsEntity {

	@Override
	public void run(BatTest t) {
		RecList parameter = RecList.fromArray( (int[]) t.getParameter(0) );
		t.setResult( allDifferent( parameter ) );
	}

	/* BEGIN TEMPLATE */
	boolean allDifferent(RecList seq) {
		/* BEGIN SOLUTION */
		if (seq == null)
			return true;
		/* inline compute isMember */
		RecList ptr = seq.tail;
		while (ptr != null && ptr.head != seq.head) 
			ptr = ptr.tail;
		if (ptr != null) 
			return false;
		/* end isMember */
		return allDifferent(seq.tail);
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
