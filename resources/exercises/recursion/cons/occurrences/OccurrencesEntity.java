package recursion.cons.occurrences;

import plm.universe.bat.BatTest;
import plm.universe.cons.ConsEntity;
import plm.universe.cons.RecList;

public class OccurrencesEntity extends ConsEntity {

	@Override
	public void run(BatTest t) {
		RecList parameter = RecList.fromArray( (int[]) t.getParameter(0) );
		t.setResult( occurrences( parameter, (int)t.getParameter(1) ) );
	}

	/* BEGIN TEMPLATE */
	int occurrences(RecList seq, int val) {
		/* BEGIN SOLUTION */
		if (seq == null)
			return 0;
		if (seq.head == val)
			return 1 + occurrences(seq.tail, val);
		return occurrences(seq.tail,val);
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
