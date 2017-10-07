package recursion.cons.length;

import plm.universe.bat.BatTest;
import plm.universe.cons.ConsEntity;
import plm.universe.cons.RecList;

public class LengthEntity extends ConsEntity {

	@Override
	public void run(BatTest t) {
		RecList parameter = RecList.fromArray( (int[]) t.getParameter(0) );
		t.setResult( length( parameter ) );
	}

	/* BEGIN TEMPLATE */
	int length(RecList seq) {
		/* BEGIN SOLUTION */
		if (seq == null)
			return 0;
		return 1+length(seq.tail);
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
