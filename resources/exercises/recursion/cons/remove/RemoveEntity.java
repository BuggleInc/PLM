package recursion.cons.remove;

import plm.universe.bat.BatTest;
import plm.universe.cons.ConsEntity;
import plm.universe.cons.RecList;

public class RemoveEntity extends ConsEntity {

	@Override
	public void run(BatTest t) {
		RecList parameter = RecList.fromArray( (int[]) t.getParameter(0) );
		t.setResult( remove( parameter, (int) t.getParameter(1) ) );
	}

	/* BEGIN TEMPLATE */
	RecList remove(RecList seq, int v) {
		/* BEGIN SOLUTION */
		if (seq == null)
			return null;
		if (seq.head == v)
			return remove(seq.tail, v);
		return new RecList(seq.head, remove(seq.tail, v));
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
