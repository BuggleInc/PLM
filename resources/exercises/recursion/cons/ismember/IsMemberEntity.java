package recursion.cons.ismember;

import plm.universe.bat.BatTest;
import plm.universe.cons.ConsEntity;
import plm.universe.cons.RecList;

public class IsMemberEntity extends ConsEntity {

	@Override
	public void run(BatTest t) {
		RecList parameter = RecList.fromArray( (int[]) t.getParameter(0) );
		t.setResult( isMember( parameter, (int)t.getParameter(1) ) );
	}

	/* BEGIN TEMPLATE */
	boolean isMember(RecList seq, int val) {
		/* BEGIN SOLUTION */
		if (seq == null)
			return false;
		if (seq.head == val)
			return true;
		return isMember(seq.tail,val);
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
