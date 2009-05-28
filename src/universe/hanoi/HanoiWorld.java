package universe.hanoi;

import java.util.NoSuchElementException;
import java.util.Vector;

import jlm.ui.WorldView;
import jlm.universe.World;

public class HanoiWorld extends World {
	private HanoiSlot slots[];
	
	protected Integer[] values(int i) {
		return slots[i].values();
	}
	public int getSlotSize(int slot) {
		return slots[slot].size();
	}
	public void move(int src, int dst) throws HanoiInvalidMove {
		if (src < 0 || src > 2 || dst < 0 || dst > 2)
			throw new HanoiInvalidMove("Cannot move from slot #"+src+" to #"+dst+": invalid parameters");
		if (src == dst)
			throw new HanoiInvalidMove("Cannot move from slot #"+src+" itself: invalid parameters");
		
		if (slots[src].size() == 0)
			throw new HanoiInvalidMove("No disc to move from slot #"+src);
		if (slots[dst].size() > 0 &&
				slots[src].top() > slots[dst].top())
			throw new HanoiInvalidMove("Cannot move disc from slot #"+src+" to #"+dst+": "+
					slots[src].top()+" > "+slots[dst].top());
		slots[dst].push(slots[src].pop());
	}
	
	public HanoiWorld(String name, Integer  size) {
		this(name,new Integer[0],new Integer[0],new Integer[0]);
		for (int i=size;i>0;i--)
			slots[0].push(i);
	}	

	public HanoiWorld(String name, Integer[] A, Integer[] B, Integer[] C) {
		super(name);
		setDelay(200);
		slots = new HanoiSlot[3];
		for (int i=0;i<3;i++)
			slots[i] = new HanoiSlot();
		
		for (int i=A.length-1; i>=0; i--) 
			slots[0].push(A[i]);
		for (int i=B.length-1; i>=0; i--) 
			slots[1].push(B[i]);
		for (int i=C.length-1; i>=0; i--) 
			slots[2].push(C[i]);
	}
	public HanoiWorld(HanoiWorld other) {
		super(other);
	}
	@Override
	public void reset(World w) {
		HanoiWorld other = (HanoiWorld)w;
		slots = new HanoiSlot[3];
		for (int i=0;i<3;i++)
			slots[i] = other.slots[i].copy(); 		
		super.reset(w);		
	}

	@Override
	public WorldView[] getView() {
		return new WorldView[] { new HanoiWorldView(this) } ;
	}
	
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("HanoiWorld "+getName()+": ");
		sb.append("A: [");
		for (int i:slots[0].values()) 
			sb.append(i+" ");
		sb.append("] B: [");
		for (int i:slots[1].values()) 
			sb.append(i+" ");
		sb.append("] C: [");
		for (int i:slots[2].values()) 
			sb.append(i+" ");
		sb.append("]");
		return sb.toString();
	}

	@Override 
	public boolean equals(Object o) {
		if (o == null || !(o instanceof HanoiWorld))
			return false;
		HanoiWorld other = (HanoiWorld) o;
		for (int i=0;i<3;i++) {
			if (!(this.slots[i].equals(other.slots[i])))
				return false;
		}
		return true;
	}
	
	class HanoiSlot {
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
}
