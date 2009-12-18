package universe.hanoi;

import java.util.NoSuchElementException;
import java.util.Vector;

import jlm.ui.WorldView;
import jlm.universe.World;

/* BEGIN TEMPLATE */
public class HanoiWorld extends World {	
	/** A copy constructor (mandatory for the internal compilation mechanism to work)
	 * 
	 * There is normally no need to change it, but it must be present. 
	 */ 
	public HanoiWorld(HanoiWorld other) {
		super(other);
	}
	
	/** The constructor that the exercises will use to setup the world.
	 *  
	 * It must begin by super(name), and the rest is free (depending on the state describing your world).
	 * It is a good idea to use setDelay to specify the default animation delay, but this is not mandatory.
	 * 
	 * You can perfectly have several such constructor. 
	 * 
	 * In general, you could even have none of them, but writing exercises will be harder. 
	 * The metalesson, use this specific constructor, so please don't change its arguments.
	 */
	public HanoiWorld(String name, Integer[] A, Integer[] B, Integer[] C) {
		super(name);
		setDelay(200);
		/* Your code here */
		/* BEGIN HIDDEN */
		slots = new HanoiSlot[3];
		for (int i=0;i<3;i++)
			slots[i] = new HanoiSlot();
		
		for (int i=0; i<A.length; i++) 
			slots[0].push(A[i]);
		for (int i=0; i<B.length; i++) 
			slots[1].push(B[i]);
		for (int i=0; i<C.length; i++) 
			slots[2].push(C[i]);
		/* END HIDDEN */
	}
	
	/** Reset the state of the current world to the one passed in argument
	 * 
	 * This is mandatory for the JLM good working. Even if the prototype says that the passed object can be 
	 * any kind of world, you can be sure that it's of the same type than the current world. So, there is 
	 * no need to check before casting your argument.
	 * 
	 * Do not forget to call super.reset(w) afterward, or some internal world fields may not get reset.
	 */
	@Override
	public void reset(World w) {
		HanoiWorld other = (HanoiWorld)w;
		/* Your code here */
		/* BEGIN HIDDEN */
		slots = new HanoiSlot[3];
		for (int i=0;i<3;i++)
			slots[i] = other.slots[i].copy(); 		
		/* END HIDDEN */
		super.reset(w);		
	}

	/* BEGIN HIDDEN */
	/** Returns a component able of displaying the world -- will be used in third exercise 
	 * You should comment this for the first exercises */
	@Override
	public WorldView[] getView() {
		return new WorldView[] { new universe.hanoi.HanoiWorldView(this) } ;
	}
	/* END HIDDEN */
	
	/* BEGIN HIDDEN */
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
	/* END HIDDEN */

	/** Used to check whether the student code changed the world in the right state -- see exercise 4 */
	@Override 
	public boolean equals(Object o) {
		/* BEGIN HIDDEN */
		if (o == null || !(o instanceof HanoiWorld))
			return false;
		HanoiWorld other = (HanoiWorld) o;
		for (int i=0;i<3;i++) {
			if (!(this.slots[i].equals(other.slots[i])))
				return false;
		}
		/* END HIDDEN */
		return true;
	}
	
	/* Here comes the world logic */
	/* BEGIN HIDDEN */
	private HanoiSlot slots[];
	
	public Integer[] values(Integer	 i) {
		return slots[i].values();
	}
	public int getSlotSize(int slot) {
		return slots[slot].size();
	}
	public void move(Integer src, Integer dst) throws HanoiInvalidMove {
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
	/* END HIDDEN */
}
/* END TEMPLATE */