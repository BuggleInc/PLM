package lessons.backtracking;

public class KnapsackElement extends BacktrackingElement {
	public final int value;
	public final int size;
	
	public KnapsackElement(int value,int size) {
		this.value = value;
		this.size = size;
	}
	@Override
	public BacktrackingElement copy() {
		return new KnapsackElement(value, size);
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof KnapsackElement))
			return false;
		KnapsackElement e = (KnapsackElement) o;
		if (e.size!=size || e.value!=value)
			return false;
		return true;
	}
}
