package recursion.cons.plusone;

import plm.universe.bat.BatTest;
import plm.universe.cons.ConsEntity;
import plm.universe.cons.RecList;

public class PlusOneEntity extends ConsEntity {

	@Override
	public void run(BatTest t) {
		RecList parameter = RecList.fromArray( (int[]) t.getParameter(0) );
		t.setResult( plusOne( parameter ) );
	}

	/* BEGIN TEMPLATE */
	RecList plusOne(RecList seq) {
		/* BEGIN SOLUTION */
		if (seq == null)
			return null;
		return new RecList(seq.head+1, plusOne(seq.tail));
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
