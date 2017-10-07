package recursion.cons.last;

import plm.universe.bat.BatTest;
import plm.universe.cons.ConsEntity;
import plm.universe.cons.RecList;

public class LastEntity extends ConsEntity {

	@Override
	public void run(BatTest t) {
		RecList parameter = RecList.fromArray( (int[]) t.getParameter(0) );
		t.setResult( last( parameter ) );
	}

	/* BEGIN TEMPLATE */
	int last(RecList seq) {
		/* BEGIN SOLUTION */
		if (seq.tail == null)
			return seq.head;
		return last(seq.tail);
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
