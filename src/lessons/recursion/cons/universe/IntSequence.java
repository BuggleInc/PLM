package lessons.recursion.cons.universe;

import java.util.List;

/** Recursive sequence of integers */

public class IntSequence {
	public int head;
	public IntSequence tail;

	public IntSequence (int car, IntSequence cdr) {    // constructor
		head = car;  tail = cdr; 
	}
	
	/* Constructor from an array of integers. Not a constructor as it returns sometimes null */
	public static IntSequence fromList(List<Integer> seq) {
		return fromArray((Integer[])seq.toArray(),0);
	}
	private static IntSequence fromArray(Integer[] seq, int rank) {
		if (rank>= seq.length)
			return null;
		return new IntSequence(seq[rank], fromArray(seq,rank+1) );
	}

	public String toString() {
		StringBuffer res = new StringBuffer();
		res.append("[");
		IntSequence ptr = this;
		boolean first = true;
		while (ptr != null) {
			if (first)
				first = false;
			else 
				res.append(", ");
			res.append(ptr.head);
			ptr = ptr.tail;
		}
		res.append("]");
		return res.toString();
	}
}
