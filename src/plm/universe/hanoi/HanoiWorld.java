package plm.universe.hanoi;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Vector;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.svggen.SVGGraphics2DIOException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.xnap.commons.i18n.I18n;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import plm.core.lang.LangPython;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.I18nManager;
import plm.core.utils.FileUtils;
import plm.universe.World;

public class HanoiWorld extends World {
	
	private Vector<HanoiDisk> slots[];
	public int moveCount = 0;
	
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
	public HanoiWorld(FileUtils fileUtils, String name, Vector<HanoiDisk> A, Vector<HanoiDisk> B, Vector<HanoiDisk> C) {
		super(fileUtils, name);
		slots = new Vector[] {A, B, C};
	}

	@SuppressWarnings("unchecked")
	public HanoiWorld(FileUtils fileUtils, String name, Vector<HanoiDisk> A, Vector<HanoiDisk> B, Vector<HanoiDisk> C, Vector<HanoiDisk> D) {
		super(fileUtils, name);
		slots = new Vector[] {A, B, C, D};		
	}
	
	@JsonCreator
	public HanoiWorld(FileUtils fileUtils, @JsonProperty("name")String name) {
		super(fileUtils, name);
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
		slots = new Vector[other.slots.length];
		for (int slot=0;slot<other.slots.length;slot++) {
			slots[slot] = new Vector<HanoiDisk>();
			for (int i=0; i<other.slots[slot].size(); i++) {
				HanoiDisk copy = other.slots[slot].elementAt(i).copy();
				slots[slot].add(copy);
			}
		}
		moveCount = other.moveCount;
		super.reset(w);		
	}

	@Override
	protected void draw() {
		// Get a DOMImplementation.
		DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();

		// Create an instance of org.w3c.dom.Document.
		String svgNS = "http://www.w3.org/2000/svg";
		Document document = domImpl.createDocument(svgNS, "svg", null);

		// Create an instance of the SVG Generator.
		SVGGraphics2D svgGenerator = new SVGGraphics2D(document);

		HanoiWorldView test = new HanoiWorldView(this);
		test.paintComponent(svgGenerator);

		boolean useCSS = true; // we want to use CSS style attributes
		Writer out = null;
		try {
			out = new OutputStreamWriter(System.out, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		try {
			svgGenerator.stream(out, useCSS);
		} catch (SVGGraphics2DIOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("HanoiWorld "+getName()+": ");
		for (int s=0;s<slots.length;s++) {
		sb.append("slot "+s+" [");
		for (Object i:slots[s].toArray()) 
			sb.append(i+" ");
		sb.append("]");
		}
		return sb.toString();
	}

	/** Used to check whether the student code changed the world in the right state -- see exercise 4 */
	@Override 
	public boolean equals(Object o) {
		/* BEGIN HIDDEN */
		if (o == null || !(o instanceof HanoiWorld))
			return false;
		HanoiWorld other = (HanoiWorld) o;
		if (this.moveCount != other.moveCount)
			return false;
		if (this.slots.length != other.slots.length)
			return false;
		for (int i=0;i<slots.length;i++) {
			if (!(this.slots[i].equals(other.slots[i])))
				return false;
		}
		/* END HIDDEN */
		return getName().equals(other.getName());
	}
	
	@Override
	public String diffTo(World o) {
		I18n i18n = I18nManager.getI18n(getLocale());
		StringBuffer res = new StringBuffer();
		if (o == null || !(o instanceof HanoiWorld))
			return "This is not a world of Hanoi";

		HanoiWorld other = (HanoiWorld) o;
		if (slots.length != other.slots.length)
			return "The worlds don't have the same amount of pegs";

		if (other.moveCount != moveCount)
			res.append(i18n.tr("The disks were not moved the same amount of time: {0} vs. {1}\n",moveCount,other.moveCount));

		for (int slot=0; slot<slots.length; slot++)
			for ( int pos = 0;pos< Math.max(slots[slot].size(),other.slots[slot].size()) ; pos++) {
				String thisVal = pos >=  this.slots[slot].size()?"--": this.slots[slot].get(pos).toString();
				String otherVal= pos >= other.slots[slot].size()?"--":other.slots[slot].get(pos).toString();
				if (!thisVal.equals(otherVal)) {
					res.append(i18n.tr(" Disk #{0} of slot #{1} differs: {2} vs. {3}\n",(pos+1),slot,thisVal,otherVal));
				}
			}
		return res.toString();
	}
	
	/* Here comes the world logic */
	
	/** This is the main function of the public interface 
	 * @throws IllegalArgumentException if your move is not valid */
	public void move(Integer src, Integer dst) {
		I18n i18n = I18nManager.getI18n(getLocale());
		if (src < 0 || src >= slots.length || dst < 0 || dst >= slots.length) 
			throw new IllegalArgumentException(i18n.tr("Cannot move from slot {0} to {1}: the only existing slots are numbered from 0 to {2}",src,dst,getSlotsAmount()-1));
		if (src == dst)
			throw new IllegalArgumentException(i18n.tr("Cannot move from slot {0} to itself",src));
		if (slots[src].size() == 0)
			throw new IllegalArgumentException(i18n.tr("No disc to move from slot {0}",src));
		
		if (slots[dst].size() > 0 &&
				slots[src].lastElement().getSize() > slots[dst].lastElement().getSize())
			throw new IllegalArgumentException(
					i18n.tr("Cannot move disc from slot {0} to {1} small disk must remain over large ones but {2} > {3}",
							src,dst,slots[src].lastElement().getSize(),slots[dst].lastElement().getSize()));
		
		moveCount  ++;
		slots[dst].add(slots[src].remove(slots[src].size()-1));
	}

	@JsonIgnore
	public int getSlotsAmount() {
		return slots.length;
	}
	public int getSlotSize(int slot) {
		return slots[slot].size();
	}
	public int getRadius(int slot) {
		return slots[slot].isEmpty()?99:slots[slot].lastElement().getSize();
	}
	
	public Vector<HanoiDisk>[] getSlots()
	{
		return slots;
	}

	public void setSlots(Vector<HanoiDisk>[] slots)
	{
		this.slots = slots;
	}

	public int getMoveCount()
	{
		return this.moveCount;
	}
	
	public Color getColor(int slot, int pos) {
		if (slot >= getSlotsAmount() || slot < 0)
			throw new RuntimeException("Invalid slot: "+slot);
		if (pos>= slots[slot].size())
			throw new RuntimeException("Slot "+slot+" is only "+slots[slot].size()+" high; cannot take position "+pos);
		return slots[slot].get(pos).getColor();		
	}
	public void setColor(int slot, int pos, Color c) {
		slots[slot].get(pos).setColor(c);
	}
	@Override
	public void setupBindings(ProgrammingLanguage lang, ScriptEngine e) throws ScriptException {
		if (lang instanceof LangPython) {
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