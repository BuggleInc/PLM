package universe.hanoi;

import java.util.NoSuchElementException;
import java.util.Vector;

public class HanoiSlot {
	private Vector<Integer> data;
	
	public HanoiSlot() {
		data = new Vector<Integer>();
	}
	public HanoiSlot(Integer[] content) {
		data = new Vector<Integer>();
		for (int i:content) {
			data.add(i);
		}
	}
	public HanoiSlot copy() {
		HanoiSlot res = new HanoiSlot();
		for (int i:data)
			res.push(i);
		return res;		
	}
	
	public int top() {
		if (data.size()==0)
			throw new NoSuchElementException("Slot is empty");
		return data.lastElement();
	}
	public int pop() {
		if (data.size()==0)
			throw new NoSuchElementException("Slot is empty");
		int res = data.lastElement();
		data.remove(data.size()-1);
		return res;
	}
	public void push(int elm) {
		data.add(elm);
	}
	public int size() {
		return data.size();
	}
	public Integer[] values() {
		return data.toArray(new Integer[data.size()]);
	}
	
	@Override 
	public boolean equals(Object o) {
		if (!(o instanceof HanoiSlot))
			return false;
		HanoiSlot other = (HanoiSlot) o;
		if (other.data.size() != this.data.size())
			return false;
		for (int pos=0;pos<this.data.size();pos++)
			if (other.data.get(pos) != this.data.get(pos))
				return false;
		return true;
	}
}
