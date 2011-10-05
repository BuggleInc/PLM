package jlm.universe.hanoi;

import java.util.Vector;

import jlm.core.model.ProgrammingLanguage;
import jlm.core.ui.WorldView;
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
	@SuppressWarnings("unchecked")
	public HanoiWorld(String name, Integer[] A, Integer[] B, Integer[] C) {
		super(name);
		setDelay(200); /* Delay (in ms) in default animations */
		/* Your code here */
		/* BEGIN HIDDEN */
		slots = new Vector[] {new Vector<Integer>(), new Vector<Integer>(), new Vector<Integer>()};
		
		for (int i=0; i<A.length; i++) 
			slots[0].add(A[i]);
		for (int i=0; i<B.length; i++) 
			slots[1].add(B[i]);
		for (int i=0; i<C.length; i++) 
			slots[2].add(C[i]);
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
	@SuppressWarnings("unchecked")
	@Override
	public void reset(World w) {
		HanoiWorld other = (HanoiWorld)w;
		/* Your code here */
		/* BEGIN HIDDEN */
		slots = new Vector[] {new Vector<Integer>(), new Vector<Integer>(), new Vector<Integer>()};
		for (int slot=0;slot<3;slot++)
			for (int i=0; i<other.slots[slot].size(); i++)
				slots[slot].add( other.slots[slot].elementAt(i));
		/* END HIDDEN */
		super.reset(w);		
	}

	/* BEGIN HIDDEN */
	/** Returns a component able of displaying the world -- will be used in third exercise 
	 * You should comment this for the first exercises */
	@Override
	public WorldView[] getView() {
		return new WorldView[] { new jlm.universe.hanoi.HanoiWorldView(this) } ;
	}
	/* END HIDDEN */
	
	/* BEGIN HIDDEN */
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("HanoiWorld "+getName()+": ");
		sb.append("A: [");
		for (Object i:slots[0].toArray()) 
			sb.append(i+" ");
		sb.append("] B: [");
		for (Object i:slots[1].toArray()) 
			sb.append(i+" ");
		sb.append("] C: [");
		for (Object i:slots[2].toArray()) 
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
	private Vector<Integer> slots[];
	
	/** This function is used by the view to retrieve the data to display */
	protected Integer[] values(Integer	 i) {
		return slots[i].toArray(new Integer[slots[i].size()]);
	}
	
	/** This is the main function of the public interface */
	public void move(Integer src, Integer dst) throws HanoiInvalidMove {
		if (src < 0 || src > 2 || dst < 0 || dst > 2)
			throw new HanoiInvalidMove("Cannot move from slot #"+src+" to #"+dst+": the only existing slots are 0, 1 and 2");
		if (src == dst)
			throw new HanoiInvalidMove("Cannot move from slot #"+src+" to itself");
		if (slots[src].size() == 0)
			throw new HanoiInvalidMove("No disc to move from slot #"+src);
		
		if (slots[dst].size() > 0 &&
				slots[src].lastElement() > slots[dst].lastElement())
			throw new HanoiInvalidMove("Cannot move disc from slot #"+src+" to #"+dst+": small disk must remain over large ones but "+
					slots[src].lastElement() +" > "+slots[dst].lastElement());
		
		slots[dst].add( slots[src].remove(slots[src].size()-1) );
	}
	public int getSlotSize(int slot) {
		return slots[slot].size();
	}
	@Override
	public String getBindings(ProgrammingLanguage lang) {
		throw new RuntimeException("No binding of HanoiWorld for "+lang);
	}
	/* END HIDDEN */

	@Override
	public String diffTo(World world) {
		return null; // FIXME: how to represent this textually?
	}
}
/* END TEMPLATE */