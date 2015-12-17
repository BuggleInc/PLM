package plm.universe.cons;

import java.util.ArrayList;
import java.util.List;


/** Recursive sequence of integers */

public class RecList {
	public int head;
	public RecList tail;

	public RecList (int car, RecList cdr) {    // constructor
		head = car;  tail = cdr; 
	}
	
	/* Constructor from an array of integers. Not a constructor as it returns sometimes null */
	public static RecList fromArray(int[] a) {
		return fromArray(a,0);
	}
	private static RecList fromArray(int[] seq, int rank) {
		if (rank>= seq.length)
			return null;
		return new RecList(seq[rank], fromArray(seq,rank+1) );
	}
	public int[] toArray() {
		int len = 0;
		RecList ptr = this;
		while (ptr != null) {
			ptr=ptr.tail;
			len++;
		}
		if (len == 0)
			return new int[]{};
		int[] res = new int[len];
		int i=0;
		ptr = this;
		while (ptr != null) {
			res[i] = ptr.head;
			ptr=ptr.tail;
			i++;
		}
		return res;
	}

	public List<Integer> toList() {
		List<Integer> res = new ArrayList<Integer>();
		RecList ptr = this;
		while (ptr != null) {
			res.add(ptr.head);
			ptr = ptr.tail;
		}
		return res;
	}
	public String toString() {
		StringBuffer res = new StringBuffer();
		res.append(" [");
		RecList ptr = this;
		boolean first = true;
		while (ptr != null) {
			if (first)
				first = false;
			else 
				res.append(", ");
			res.append(ptr.head);
			ptr = ptr.tail;
		}
		res.append("] ");
		return res.toString();
	}

	public int plmInsiderLength() {
		int res = 0;
		RecList ptr = this;
		while (ptr != null) {
			ptr = ptr.tail;
			res++;
		}
		return res;
	}
}
