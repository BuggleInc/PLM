package lessons.meta;

import java.util.NoSuchElementException;
import java.util.Vector;

/* BEGIN TEMPLATE */
import jlm.universe.World;

public class HanoiCreateWorldAnswer extends World {
	/** A copy constructor (mandatory for the internal compilation mechanism to work)
	 * 
	 * There is normally no need to change it, but it must be present. 
	 */ 
	public HanoiCreateWorldAnswer(HanoiCreateWorldAnswer other) {
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
	public HanoiCreateWorldAnswer(String name, Integer[] slotA, Integer[] slotB, Integer[] slotC) {
		super(name);
		setDelay(200);
		/* Your code here */
	    /* BEGIN SOLUTION */
		slots = new HanoiSlot[3];
		for (int i=0;i<3;i++)
			slots[i] = new HanoiSlot();
		for (int i=slotA.length-1; i>=0; i--) 
			slots[0].push(slotA[i]);
		for (int i=slotB.length-1; i>=0; i--) 
			slots[1].push(slotB[i]);
		for (int i=slotC.length-1; i>=0; i--) 
			slots[2].push(slotC[i]);
	    /* END SOLUTION */
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
		HanoiCreateWorldAnswer other = (HanoiCreateWorldAnswer)w;
		/* Your code here */
		/* BEGIN SOLUTION */
		slots = new HanoiSlot[3];
		for (int i=0;i<3;i++)
			slots[i] = other.slots[i].copy(); 		
		/* END SOLUTION */
		super.reset(w);		
	}

	/* BEGIN SOLUTION */
	private HanoiSlot slots[];
	
	public int getSlotSize(int slot) {
		return slots[slot].size();
	}
    /* END SOLUTION */
	
	/** Get the content of a given slot of the current Hanoi world
	 * 
	 * Gives access to the internal state so that the WorldView class can display it.
	 *   
	 * It's better to mark this method protected in regular universes, since it's 
	 * for internal use only, but it's not possible in this meta-exercise: the view 
	 * is provided by us, and the world is written by you. For technical reasons, 
	 * they thus cannot be in the same package, so the method have to be public for 
	 * our View class to see it. 
	 * 
	 */
	protected Integer[] values(Integer i) {
		/* BEGIN SOLUTION */
		return slots[i].values();
	    /* END SOLUTION */
	}
	
	/* BEGIN SOLUTION */	
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
	/* END SOLUTION */
}
/* END TEMPLATE */