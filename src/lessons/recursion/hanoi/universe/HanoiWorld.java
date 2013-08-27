package lessons.recursion.hanoi.universe;

import java.util.Vector;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.swing.ImageIcon;

import plm.core.model.Game;
import plm.core.model.ProgrammingLanguage;
import plm.core.ui.ResourcesCache;
import plm.core.ui.WorldView;
import plm.universe.EntityControlPanel;
import plm.universe.World;

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
		slots = new Vector[] {new Vector<Integer>(), new Vector<Integer>(), new Vector<Integer>()};
		
		for (int i=0; i<A.length; i++) 
			slots[0].add(A[i]);
		for (int i=0; i<B.length; i++) 
			slots[1].add(B[i]);
		for (int i=0; i<C.length; i++) 
			slots[2].add(C[i]);
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
		slots = new Vector[] {new Vector<Integer>(), new Vector<Integer>(), new Vector<Integer>()};
		for (int slot=0;slot<3;slot++)
			for (int i=0; i<other.slots[slot].size(); i++)
				slots[slot].add( other.slots[slot].elementAt(i));
		super.reset(w);		
	}

	/* BEGIN HIDDEN */
	/** Returns a component able of displaying the world -- will be used in third exercise 
	 * You should comment this for the first exercises */
	@Override
	public WorldView getView() {
		return new HanoiWorldView(this);
	}
	@Override
	public ImageIcon getIcon() {
		return ResourcesCache.getIcon("img/world_hanoi.png");
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
	
	@Override
	public String diffTo(World world) {
		return null; // FIXME: how to represent this textually?
	}
	
	/* Here comes the world logic */
	/* BEGIN HIDDEN */
	private Vector<Integer> slots[];
	
	/** This function is used by the view to retrieve the data to display */
	protected Integer[] values(Integer	 i) {
		return slots[i].toArray(new Integer[slots[i].size()]);
	}
	
	/** This is the main function of the public interface 
	 * @throws IllegalArgumentException if your move is not valid */
	public void move(Integer src, Integer dst) {
		if (src < 0 || src > 2 || dst < 0 || dst > 2) 
			throw new IllegalArgumentException(Game.i18n.tr("Cannot move from slot {0} to {1}: the only existing slots are 0, 1 and 2",src,dst));
		if (src == dst)
			throw new IllegalArgumentException(Game.i18n.tr("Cannot move from slot {0} to itself",src));
		if (slots[src].size() == 0)
			throw new IllegalArgumentException(Game.i18n.tr("No disc to move from slot {0}",src));
		
		if (slots[dst].size() > 0 &&
				slots[src].lastElement() > slots[dst].lastElement())
			throw new IllegalArgumentException(
					Game.i18n.tr("Cannot move disc from slot {0} to {1} small disk must remain over large ones but {2} > {3}",
							src,dst,slots[src].lastElement(),slots[dst].lastElement()));
		
		slots[dst].add( slots[src].remove(slots[src].size()-1) );
	}
	public int getSlotSize(int slot) {
		return slots[slot].size();
	}
	@Override
	public void setupBindings(ProgrammingLanguage lang, ScriptEngine e) throws ScriptException {
		if (lang.equals(Game.PYTHON)) {
			e.eval( "def move(src,dst):\n"+
					"  entity.move(src,dst)\n"+
					"def getSlotSize(slot):\n"+
					"  return entity.getSlotSize(slot)\n"+
					/* BINDINGS TRANSLATION: French */
					"def deplace(src,dst):\n"+
					"  entity.move(src,dst)\n"+
					"def getTaillePiquet(slot):\n"+
					"  return entity.getSlotSize(slot)\n");
		} else {
			throw new RuntimeException("No binding of HanoiWorld for "+lang);
		}
	}
	
	/**
	 * Return the panel which let the user to interact dynamically with the world
	 */
	@Override
	public EntityControlPanel getEntityControlPanel() {
		return new HanoiMovePanel();
	}
	/* END HIDDEN */

}
/* END TEMPLATE */