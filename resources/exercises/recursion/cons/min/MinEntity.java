package recursion.cons.min;

import plm.universe.bat.BatTest;
import plm.universe.cons.ConsEntity;
import plm.universe.cons.RecList;

public class MinEntity extends ConsEntity {

	@Override
	public void run(BatTest t) {
		RecList parameter = RecList.fromArray( (int[]) t.getParameter(0) );
		t.setResult( min( parameter ) );
	}

	/* BEGIN TEMPLATE */
	int min(RecList seq) {
		/* BEGIN SOLUTION */
		int v = seq.head;
		RecList ptr = seq;
		while (ptr != null) {
			if (ptr.head < v)
				v = ptr.head;
			ptr = ptr.tail;
		}
		return v;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
