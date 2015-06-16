package lessons.recursion.hanoi.universe;

import java.awt.Color;
import java.util.Vector;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
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
	public HanoiWorld(Game game, String name, Integer[] A, Integer[] B, Integer[] C) {
		super(game, name);
		setDelay(200); /* Delay (in ms) in default animations */
		slotsVal = new Vector[] {new Vector<Integer>(), new Vector<Integer>(), new Vector<Integer>()};
		slotsColor = new Vector[] {new Vector<Color>(), new Vector<Color>(), new Vector<Color>()};
		
		for (int i=0; i<A.length; i++) {
			slotsVal[0].add(A[i]);
			slotsColor[0].add(Color.yellow);
		}
		for (int i=0; i<B.length; i++) { 
			slotsVal[1].add(B[i]);
			slotsColor[1].add(Color.yellow);
		}
		for (int i=0; i<C.length; i++) { 
			slotsVal[2].add(C[i]);
			slotsColor[2].add(Color.yellow);
		}
	}

	@SuppressWarnings("unchecked")
	public HanoiWorld(Game game, String name, Integer[] A, Integer[] B, Integer[] C, Integer[] D) {
		super(game, name);
		setDelay(200); /* Delay (in ms) in default animations */
		slotsVal = new Vector[] {new Vector<Integer>(), new Vector<Integer>(), new Vector<Integer>(), new Vector<Integer>()};
		slotsColor = new Vector[] {new Vector<Color>(), new Vector<Color>(), new Vector<Color>(), new Vector<Color>()};
		
		for (int i=0; i<A.length; i++) {
			slotsVal[0].add(A[i]);
			slotsColor[0].add(Color.yellow);
		}
		for (int i=0; i<B.length; i++) { 
			slotsVal[1].add(B[i]);
			slotsColor[1].add(Color.yellow);
		}
		for (int i=0; i<C.length; i++) { 
			slotsVal[2].add(C[i]);
			slotsColor[2].add(Color.yellow);
		}
		for (int i=0; i<D.length; i++) {
			slotsVal[3].add(D[i]);
			slotsColor[3].add(Color.yellow);
		}

	}
	
	/** Reset the state of the current world to the one passed in argument
	 * 
	 * This is mandatory for the PLM good working. Even if the prototype says that the passed object can be 
	 * any kind of world, you can be sure that it's of the same type than the current world. So, there is 
	 * no need to check before casting your argument.
	 * 
	 * Do not forget to call super.reset(w) afterward, or some internal world fields may not get reset.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void reset(World w) {
		HanoiWorld other = (HanoiWorld)w;
		slotsVal = new Vector[other.slotsVal.length];
		slotsColor = new Vector[other.slotsColor.length];
		for (int slot=0;slot<other.slotsVal.length;slot++) {
			slotsVal[slot] = new Vector<Integer>();
			slotsColor[slot] = new Vector<Color>();
			for (int i=0; i<other.slotsVal[slot].size(); i++) {
				slotsVal[slot].add( other.slotsVal[slot].elementAt(i));
				slotsColor[slot].add( other.slotsColor[slot].elementAt(i));
			}
		}
		moveCount = other.moveCount;
		super.reset(w);		
	}
	
	/* BEGIN HIDDEN */
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("HanoiWorld "+getName()+": ");
		for (int s=0;s<slotsVal.length;s++) {
		sb.append("slot "+s+" [");
		for (Object i:slotsVal[s].toArray()) 
			sb.append(i+" ");
		sb.append("]");
		}
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
		if (this.moveCount != other.moveCount)
			return false;
		if (this.slotsVal.length != other.slotsVal.length)
			return false;
		for (int i=0;i<slotsVal.length;i++) {
			if (!(this.slotsVal  [i].equals(other.slotsVal[i])) ||
				!(this.slotsColor[i].equals(other.slotsColor[i])))
				return false;
		}
		/* END HIDDEN */
		return getName().equals(other.getName());
	}
	
	@Override
	public String diffTo(World o) {
		StringBuffer res = new StringBuffer();
		if (o == null || !(o instanceof HanoiWorld))
			return "This is not a world of Hanoi";

		HanoiWorld other = (HanoiWorld) o;
		if (slotsVal.length != other.slotsVal.length)
			return "The worlds don't have the same amount of pegs";

		if (other.moveCount != moveCount)
			res.append(getGame().i18n.tr("The disks were not moved the same amount of time: {0} vs. {1}\n",moveCount,other.moveCount));

		for (int slot=0; slot<slotsVal.length; slot++)
			for ( int pos = 0;pos< Math.max(slotsVal[slot].size(),other.slotsVal[slot].size()) ; pos++) {
				String thisVal = pos >=  this.slotsVal[slot].size()?"--": this.slotsVal[slot].get(pos).toString();
				String otherVal= pos >= other.slotsVal[slot].size()?"--":other.slotsVal[slot].get(pos).toString();
				if (!thisVal.equals(otherVal)) {
					res.append(getGame().i18n.tr(" Disk #{0} of slot #{1} differs: {2} vs. {3}\n",(pos+1),slot,thisVal,otherVal));
				} else {
					Color thisColor = pos >=   this.slotsColor[slot].size()?null: this.slotsColor[slot].get(pos);
					Color otherColor= pos >=  other.slotsColor[slot].size()?null:other.slotsColor[slot].get(pos);
					if (!thisColor.equals(otherColor)) 
						res.append(getGame().i18n.tr(" Color of disk #{0} of slot #{1} differs: {2} vs. {3}\n",(pos+1),slot,thisColor,otherColor));						
				}
			}
		return res.toString();
	}
	
	/* Here comes the world logic */
	/* BEGIN HIDDEN */
	private Vector<Integer> slotsVal[];
	private Vector<Color> slotsColor[];
	public int moveCount = 0;
	
	/** This function is used by the view to retrieve the data to display */
	protected Integer[] values(Integer	 i) {
		return slotsVal[i].toArray(new Integer[slotsVal[i].size()]);
	}
	
	/** This is the main function of the public interface 
	 * @throws IllegalArgumentException if your move is not valid */
	public void move(Integer src, Integer dst) {
		if (src < 0 || src >= slotsVal.length || dst < 0 || dst >= slotsVal.length) 
			throw new IllegalArgumentException(getGame().i18n.tr("Cannot move from slot {0} to {1}: the only existing slots are numbered from 0 to {2}",src,dst,getSlotAmount()-1));
		if (src == dst)
			throw new IllegalArgumentException(getGame().i18n.tr("Cannot move from slot {0} to itself",src));
		if (slotsVal[src].size() == 0)
			throw new IllegalArgumentException(getGame().i18n.tr("No disc to move from slot {0}",src));
		
		if (slotsVal[dst].size() > 0 &&
				slotsVal[src].lastElement() > slotsVal[dst].lastElement())
			throw new IllegalArgumentException(
					getGame().i18n.tr("Cannot move disc from slot {0} to {1} small disk must remain over large ones but {2} > {3}",
							src,dst,slotsVal[src].lastElement(),slotsVal[dst].lastElement()));
		
		moveCount  ++;
		slotsVal[dst]  .add( slotsVal[src]  .remove(slotsVal[src].size()-1) );
		slotsColor[dst].add( slotsColor[src].remove(slotsColor[src].size()-1) );
	}
	public int getSlotAmount() {
		return slotsVal.length;
	}
	public int getSlotSize(int slot) {
		return slotsVal[slot].size();
	}
	public int getRadius(int slot) {
		return slotsVal[slot].isEmpty()?99:slotsVal[slot].lastElement();
	}
	
	public Vector<Integer>[] getSlot()
	{
		return slotsVal;
	}
	
	public int getMoveCount()
	{
		return this.moveCount;
	}
	
	public Color getColor(int slot, int pos) {
		if (slot >= getSlotAmount() || slot < 0)
			throw new RuntimeException("Invalid slot: "+slot);
		if (pos>= slotsColor[slot].size())
			throw new RuntimeException("Slot "+slot+" is only "+slotsColor[slot].size()+" high; cannot take position "+pos);
		return slotsColor[slot].get(pos);		
	}
	public void setColor(int slot, int pos, Color c) {
		slotsColor[slot].set(pos,c);
	}
	@Override
	public void setupBindings(ProgrammingLanguage lang, ScriptEngine e) throws ScriptException {
		if (lang.equals(Game.PYTHON)) {
			e.eval( "def move(src,dst):\n"+
					"  entity.move(src,dst)\n"+
					"def getSlotSize(slot):\n"+
					"  return entity.getSlotSize(slot)\n"+
					"def getSlotAmount():\n"+
					"  return entity.getSlotAmount()\n"+
					"def getSlotRadius(slot):\n"+
					"  return entity.getSlotRadius(slot)\n"+
					
					"def errorMsg(str):\n"+/* don't translate this one, there is no need*/
					"  entity.seenError(str)\n"+

					/* BINDINGS TRANSLATION: French */
					"def deplace(src,dst):\n"+
					"  entity.move(src,dst)\n"+
					"def getTaillePiquet(slot):\n"+
					"  return entity.getSlotSize(slot)\n"+
					"def getNbPiquet():\n"+
					"  return entity.getSlotAmount()\n"+
			        "def getRayonPiquet(piquet):\n"+
			        "  return entity.getSlotRadius(piquet)\n");
		} else {
			throw new RuntimeException("No binding of HanoiWorld for "+lang);
		}
	}
	
	/* END HIDDEN */

}
/* END TEMPLATE */