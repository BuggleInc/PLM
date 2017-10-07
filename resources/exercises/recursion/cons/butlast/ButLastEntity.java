package recursion.cons.butlast;

import plm.universe.bat.BatTest;
import plm.universe.cons.ConsEntity;
import plm.universe.cons.RecList;

public class ButLastEntity extends ConsEntity {

	@Override
	public void run(BatTest t) {
		RecList parameter = RecList.fromArray( (int[]) t.getParameter(0) );
		t.setResult( last( parameter ) );
	}

	/* BEGIN TEMPLATE */
	RecList last(RecList seq) {
		/* BEGIN SOLUTION */
		if (seq.tail == null)
			return null;
		return new RecList(seq.head, last(seq.tail));
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
