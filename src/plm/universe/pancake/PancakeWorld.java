package plm.universe.pancake;

import java.io.*;
import java.util.Random;

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
import plm.universe.hanoi.HanoiWorldView;

public class PancakeWorld extends World {

	private int lastMove = -1;
	protected int moveCount = 0;
	private int selected = -1;
	private boolean burnedWorld ;
	public boolean wasRandom = false;
	private Pancake[] pancakeStack; // The stack of pancakes

	/** Copy constructor */ 
	public PancakeWorld(PancakeWorld world) {
		super(world);
	}
	
	/** Passes the mouse selection from view to the control panel */ 
	public void setSelectedPancake(int rank) {
		if (rank < 1 || rank > getStackSize() || (rank==1&&!burnedWorld) ) {
			this.selected = -1;
		} else {
			this.selected = rank;
		}
		
	}
	
	/** 
	 * Regular PancakeWorld constructor that creates a random plate
	 * @param name : the name of the world
	 * @param size : the amount of pancakes in the stack
	 * @param burnedPancake : if we take care of the fact that the pancake is burned on one side
	 */
	public PancakeWorld(FileUtils fileUtils, String name, int size, boolean burnedPancake) {
		super(fileUtils, name);
		Random r = new Random(0);
		
		/* Create the pancakes */
		this.pancakeStack =  new Pancake[size];
		for (int i = 0; i < size; i++) 
			pancakeStack[i] = new Pancake(i + 1);
		
		/* Mix them */
		wasRandom = true;
		while (isSorted()) 
			for ( int rank = 0 ; rank < size ; rank++) {			
				if ( r.nextDouble() > 0.5) // Flipping time !
					pancakeStack[rank].flip(); 

				if ( Math.random() > 0.5) // Swapping time !
					swap(rank, (int)(r.nextDouble()*size));
			}
		
		this.burnedWorld = burnedPancake;
	}
	/** 
	 * Regular PancakeWorld constructor that takes the pancake sizes from the provided parameter
	 * @param name the name of the world
	 * @param sizes the size of each pancake. If negative, the pancake is upside down
	 * @param burnedPancake if we take care of the fact that the pancake is burned on one side
	 */
	public PancakeWorld(FileUtils fileUtils, String name, int[] sizes, boolean burnedPancake) {
		super(fileUtils, name);
		
		/* Create the pancakes */
		this.pancakeStack =  new Pancake[sizes.length];
		for (int i = 0; i < sizes.length; i++) 
			pancakeStack[i] = new Pancake(sizes[i]);
		
		this.burnedWorld = burnedPancake;
	}
	
	@JsonCreator
	public PancakeWorld(FileUtils fileUtils, @JsonProperty("name")String name) {
		super(fileUtils, name);
	}

	/** Returns a textual description of the differences between the caller and the parameter */
	@Override
	public String diffTo(World o) {
		I18n i18n = I18nManager.getI18n(getLocale());
		if (o == null || !(o instanceof PancakeWorld))
			return i18n.tr("This is not a world of pancakes");

		PancakeWorld other = (PancakeWorld) o;
		if (pancakeStack.length != other.pancakeStack.length)
			return i18n.tr("The two worlds are of differing size");

		StringBuffer res = new StringBuffer();
		if (other.moveCount != moveCount)
			res.append(i18n.tr("Stacks were not flipped the same amount of time: {0} vs. {1}\n",moveCount,other.moveCount));

		for ( int i = 0;i< pancakeStack.length;i++) 
			if ( !pancakeStack[i].equals(other.pancakeStack[i], burnedWorld)) 
				res.append(i18n.tr(" Pancake #{0} differs: {1} vs. {2}\n",(i+1),pancakeStack[i].toString(),other.pancakeStack[i].toString()));

		return res.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof PancakeWorld))
			return false;
		PancakeWorld other = (PancakeWorld) o;
		if (burnedWorld != other.burnedWorld)
			return false;
		if (pancakeStack.length != other.pancakeStack.length)
			return false;
		for ( int i = 0;i< pancakeStack.length;i++) 
			if ( !pancakeStack[i].equals(other.pancakeStack[i], burnedWorld))
				return false;
		return true;
	}
	/** 
	 * Reset the state of the current world to the one passed in argument
	 * @param world the new start of your current world
	 */
	@Override
	public void reset(World world) {
		PancakeWorld other = (PancakeWorld) world;
		this.pancakeStack = new Pancake[other.pancakeStack.length];
		for (int i=0;i<pancakeStack.length;i++)
			pancakeStack[i] = other.pancakeStack[i].copy();

		this.burnedWorld = other.burnedWorld;
		this.lastMove = other.lastMove;
		this.moveCount = other.moveCount;
		super.reset(world);
	}

	@JsonProperty("pancakeStack")
	public Pancake[] getStack() {
		return pancakeStack;
	}

	public void setPancakeStack(Pancake[] pancakeStack) {
		this.pancakeStack = pancakeStack;
	}

	public int getMoveCount() {
		return moveCount;
	}

	public int getSelected() {
		return selected;
	}

	public boolean isBurnedWorld() {
		return burnedWorld;
	}

	@JsonIgnore
	public boolean isWasRandom() {
		return wasRandom;
	}

	/** Ensures that the provided engine can be used to solve Pancake exercises */ 
	@Override
	public void setupBindings(ProgrammingLanguage lang, ScriptEngine e) throws ScriptException {
		if (lang instanceof LangPython) {
			e.eval(
				"def getStackSize():\n" +
				"  return entity.getStackSize()\n" +
				"def getPancakeRadius(rank):\n" +
				"  return entity.getPancakeRadius(rank)\n" +
				"def isPancakeUpsideDown(pancakeNumber):\n"+
				"  return entity.isPancakeUpsideDown(pancakeNumber)\n" +
				"def flip(numberOfPancakes):\n" +
				"  entity.flip(numberOfPancakes)\n"	+
				/* BINDINGS TRANSLATION: French */
				"def getTaillePile():\n"+
				"  return getStackSize()\n"+
				"def getRayonCrepe(rank):\n"+
				"  return getPancakeRadius(rank)\n"+
				"def estCrepeRetournee(rank):\n"+
				"  return isPancakeUpsideDown(rank)\n"+
				"def retourne(nb):\n"+
				"  return flip(nb)\n"
				);
		} else {
			throw new RuntimeException("No binding of PancakeWorld for "+lang);
		}
		
		
	}

	/* --------------------------------------- */

	private void swap(int from, int to) {
		Pancake temp = pancakeStack[from];
		pancakeStack[from] = pancakeStack[to];
		pancakeStack[to] = temp;
	}
	/**
	 * Flip a certain amount of pancakes in the stack
	 * @param numberOfPancakes : the number of pancakes, beginning from the top of the stack, that you want to flip.
	 */
	public void flip(int numberOfPancakes) {
		I18n i18n = I18nManager.getI18n(getLocale());
		if (numberOfPancakes < 1) 
			throw new IllegalArgumentException(i18n.tr("Asked to flip {0} pancakes, but you must flip at least one", numberOfPancakes));
		if (numberOfPancakes > this.getStackSize()) 
			throw new IllegalArgumentException(i18n.tr("Asked to flip {0} pancakes, but there is only {1} pancakes on this stack", numberOfPancakes,getStackSize()));
		
		//System.err.println("Flip("+numberOfPancakes+")");
		/* Invert the pancakes' position */
		int firstPancake = 0 ;
		int lastPancake = numberOfPancakes-1;
		while ( firstPancake < lastPancake )
			this.swap(firstPancake++, lastPancake--);
		
		/* Change their orientation */
		for (int i = 0 ;i<numberOfPancakes;i++)
			pancakeStack[i].flip();
		
		this.lastMove = numberOfPancakes ;
		this.selected = -1;
		this.moveCount++;
	}
	
	
	/** Returns the index of the last flipped pancake */
	@JsonIgnore
	protected int getLastMove() {
		return lastMove;
	}
	@JsonIgnore
	protected int getSelectedPancake() {
		return selected;
	}
	
	/**
	 * Give the size of a specific pancake among others
	 * @param rank : the number of the pancake, beginning from the top of the stack, that you want to get.
	 * @return The radius of the expected pancake
	 */
	public int getPancakeRadius(int rank) {
		I18n i18n = I18nManager.getI18n(getLocale());
		if ( rank < 0 || rank >= getStackSize())
			throw new IllegalArgumentException(i18n.tr("Cannot get the radius of pancake #{0} because it''s not between 0 and {1}",rank, getStackSize()-1));

		return pancakeStack[rank].getRadius();
	}
	
	/**
	 * Give the size of the stack of pancakes
	 * @return The number of pancakes in the stack
	 */
	@JsonIgnore
	public int getStackSize() {
		return pancakeStack.length;
	}
	

	/**
	 * Returns if the specified pancake (counting from the stack top) is upside down
	 */
	public boolean isPancakeUpsideDown(int rank) {
		I18n i18n = I18nManager.getI18n(getLocale());
		if ( rank < 0 || rank >= getStackSize())
			throw new IllegalArgumentException(i18n.tr("Cannot get the orientation of pancake #{0} because it''s not between 0 and {1}",rank, getStackSize()));

		return pancakeStack[rank].isUpsideDown();
	}
	
	/** Returns whether the stack of pancakes is correctly sorted according to the control freak pancake seller */
	@JsonIgnore
	public boolean isSorted() {
		for ( int rank = 0 ; rank < pancakeStack.length ; rank++) {
			Pancake pancake = pancakeStack[rank];
			if (pancake.getRadius() != rank+1)
				return false;
			if (burnedWorld && pancake.isUpsideDown())
				return false;
		}
		return true;
	}
	
	/** Returns a string representation of the world */
	public String toString(){
		StringBuffer res = new StringBuffer("<PancakeWorld name:"+getName()+" size:"+getStackSize()+">");
		for (Pancake p : pancakeStack) 
			res.append(p.toString());
		res.append("</PancakeWorld>");
		return res.toString();
	}

	/** Returns whether we pay attention to the burned side or not */
	@JsonIgnore
	public boolean isBurnedPancake() {
		return burnedWorld;
	}
	@Override
	protected String draw() {
		String svg =null;
		svg = PancakeWorldView.draw(this, 400,400);
		return svg;

	}
}

