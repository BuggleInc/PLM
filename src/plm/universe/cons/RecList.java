package plm.universe.cons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Recursive sequence of integers */

public class RecList {
	@JsonIgnore
	public int head;
	@JsonIgnore
	public RecList tail;

	public RecList (int car, RecList cdr) {    // constructor
		head = car;  tail = cdr; 
	}

	@JsonCreator
	public RecList(@JsonProperty("list")int[] list) {
		int length = list.length;
		if(length > 0) {
			this.head = list[0];
			if(length > 1) {
				this.tail = new RecList(Arrays.copyOfRange(list, 1, length));
			}
		}
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static scala.collection.immutable.List<scala.Int> fromArraytoScalaList(int[] list) {
		return (scala.collection.immutable.List) RecListWrapper$.MODULE$.toList(list);
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

	@JsonProperty("list")
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

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof RecList)) {
			return false;
		}
		RecList other = (RecList) o;

		int[] list1 = this.toArray();
		int[] list2 = other.toArray();

		return Arrays.equals(list1, list2);
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
